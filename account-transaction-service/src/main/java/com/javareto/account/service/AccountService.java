// Servicio principal de cuentas. Ahora extiende CrudFacade para reutilizar lógica CRUD.
// Refactorizado para aplicar el patrón Facade y mantener el código limpio y escalable.
package com.javareto.account.service;

import com.javareto.account.dto.AccountDTO;
import com.javareto.account.dto.TransactionDTO;
import com.javareto.account.dto.AccountStatementDTO;
import com.javareto.account.model.Account;
import com.javareto.account.model.Transaction;
import com.javareto.account.repository.AccountRepository;
import com.javareto.account.repository.TransactionRepository;
import com.javareto.account.exception.ResourceNotFoundException;
import com.javareto.account.exception.InsufficientFundsException;
import com.javareto.account.exception.DuplicateResourceException;
import com.javareto.account.client.ClientServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService extends CrudFacade<Account, String> {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ClientServiceClient clientServiceClient;
    
    @Autowired
    public AccountService(AccountRepository accountRepository,
                         TransactionRepository transactionRepository,
                         ClientServiceClient clientServiceClient) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.clientServiceClient = clientServiceClient;
    }
    
    @Override
    protected AccountRepository getRepository() {
        return accountRepository;
    }
    
    public AccountDTO createAccount(AccountDTO accountDTO) {

        try {
            clientServiceClient.getClientById(accountDTO.getClientId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Client not found with id: " + accountDTO.getClientId());
        }
       
        // Generar número de cuenta único
        String accountNumber = generateAccountNumber();
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = generateAccountNumber();
        }
        
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setType(accountDTO.getType());
        account.setInitialBalance(accountDTO.getInitialBalance());
        account.setClientId(accountDTO.getClientId());
        account.setStatus(true);
        
        Account savedAccount = accountRepository.save(account);
        return mapToDTO(savedAccount);
    }
    
    public AccountDTO updateAccount(String accountNumber, AccountDTO accountDTO) {
        Account account = accountRepository.findById(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));
        
        account.setType(accountDTO.getType());
        if (accountDTO.getStatus() != null) {
            account.setStatus(accountDTO.getStatus());
        }
        
        Account updatedAccount = accountRepository.save(account);
        return mapToDTO(updatedAccount);
    }
    
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));
        
        // Soft delete
        account.setStatus(false);
        accountRepository.save(account);
    }
    
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    public AccountDTO getAccount(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("La cuenta con número " + accountNumber + " no fue encontrada en el sistema"));
        
        return mapToDTO(account);
    }
    
    public List<AccountDTO> getAccountsByClient(Long clientId) {
        List<Account> accounts = accountRepository.findByClientId(clientId);
        return accounts.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Account account = accountRepository.findByAccountNumberAndStatus(
            transactionDTO.getAccountNumber(), true)
            .orElseThrow(() -> new ResourceNotFoundException("La cuenta activa con número " + 
                transactionDTO.getAccountNumber() + " no fue encontrada"));
        
        BigDecimal currentBalance = account.calculateAvailableBalance();
        BigDecimal transactionValue = transactionDTO.getValue();
        
        // Determinar si es depósito o retiro basado en el tipo
        if (transactionDTO.getType().toLowerCase().contains("retiro")) {
            transactionValue = transactionValue.negate(); // Hacer negativo
            
            // Validar fondos suficientes
            if (currentBalance.add(transactionValue).compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException("Saldo insuficiente para realizar la transacción. Saldo disponible: " + currentBalance);
            }
        }
        
        BigDecimal newBalance = currentBalance.add(transactionValue);
        
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setType(transactionDTO.getType());
        transaction.setValue(transactionValue);
        transaction.setBalance(newBalance);
        transaction.setAccount(account);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        return mapTransactionToDTO(savedTransaction);
    }
    
    public List<TransactionDTO> getTransactionsByAccount(String accountNumber) {
        List<Transaction> transactions = transactionRepository
            .findByAccountAccountNumberOrderByDateDesc(accountNumber);
        return transactions.stream()
            .map(this::mapTransactionToDTO)
            .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
            .map(this::mapTransactionToDTO)
            .collect(Collectors.toList());
    }
    
    public AccountStatementDTO getAccountStatement(Long clientId, LocalDateTime startDate, LocalDateTime endDate) {
        // Validaciones de entrada
        if (clientId == null) {
            throw new IllegalArgumentException("El ID del cliente no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        
        // Obtener información del cliente
        Map<String, Object> clientInfo;
        try {
            clientInfo = clientServiceClient.getClientById(clientId);
        } catch (Exception e) {
            // Si no se puede obtener la información del cliente, usar un valor por defecto
            clientInfo = new HashMap<>();
            clientInfo.put("name", "Cliente " + clientId);
        }
        
        // Obtener todas las cuentas del cliente
        List<Account> clientAccounts = accountRepository.findByClientId(clientId);
        
        // Obtener transacciones del cliente en el período
        List<Transaction> transactions = transactionRepository.findByClientIdAndDateBetween(
            clientId, startDate, endDate);
        
        // Agrupar transacciones por cuenta
        Map<String, List<Transaction>> transactionsByAccount = transactions.stream()
            .collect(Collectors.groupingBy(t -> t.getAccount().getAccountNumber()));
        
        // Construir el reporte
        AccountStatementDTO statement = new AccountStatementDTO();
        statement.setStartDate(startDate);
        statement.setEndDate(endDate);
        statement.setClientName((String) clientInfo.get("name"));
        
        List<AccountStatementDTO.AccountStatementDetail> accountDetails = new ArrayList<>();
        
        // Procesar cada cuenta del cliente
        for (Account account : clientAccounts) {
            AccountStatementDTO.AccountStatementDetail detail = new AccountStatementDTO.AccountStatementDetail();
            detail.setAccountNumber(account.getAccountNumber());
            detail.setType(account.getType());
            detail.setInitialBalance(account.getInitialBalance());
            detail.setStatus(account.getStatus());
            detail.setAvailableBalance(account.calculateAvailableBalance());
            
            // Obtener transacciones para esta cuenta en el período
            List<Transaction> accountTransactions = transactionsByAccount.getOrDefault(account.getAccountNumber(), new ArrayList<>());
            
            List<AccountStatementDTO.TransactionDetail> transactionDetails = accountTransactions.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())) // Ordenar por fecha descendente
                .map(this::mapToTransactionDetail)
                .collect(Collectors.toList());
            
            detail.setTransactions(transactionDetails);
            accountDetails.add(detail);
        }
        
        statement.setAccounts(accountDetails);
        return statement;
    }
    
    // Métodos auxiliares
    private String generateAccountNumber() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
    
    private AccountDTO mapToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setType(account.getType());
        dto.setInitialBalance(account.getInitialBalance());
        dto.setStatus(account.getStatus());
        dto.setClientId(account.getClientId());
        dto.setAvailableBalance(account.calculateAvailableBalance());
        return dto;
    }
    
    private TransactionDTO mapTransactionToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setDate(transaction.getDate());
        dto.setType(transaction.getType());
        dto.setValue(transaction.getValue());
        dto.setBalance(transaction.getBalance());
        dto.setAccountNumber(transaction.getAccount().getAccountNumber());
        return dto;
    }
    
    private AccountStatementDTO.TransactionDetail mapToTransactionDetail(Transaction transaction) {
        AccountStatementDTO.TransactionDetail detail = new AccountStatementDTO.TransactionDetail();
        detail.setDate(transaction.getDate());
        detail.setType(transaction.getType());
        detail.setValue(transaction.getValue());
        detail.setBalance(transaction.getBalance());
        return detail;
    }
}