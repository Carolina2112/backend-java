package com.javareto.account.controller;

import com.javareto.account.dto.AccountDTO;
import com.javareto.account.dto.TransactionDTO;
import com.javareto.account.dto.AccountStatementDTO;
import com.javareto.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
@Validated
public class AccountController {
    
    private final AccountService accountService;
    
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable("accountNumber") String accountNumber) {
        AccountDTO account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(account);
    }
    
    @GetMapping("/cliente/{clientId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByClient(@PathVariable("clientId") Long clientId) {
        List<AccountDTO> accounts = accountService.getAccountsByClient(clientId);
        return ResponseEntity.ok(accounts);
    }
    
    @PutMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable String accountNumber,
                                                   @Valid @RequestBody AccountDTO accountDTO) {
        AccountDTO updatedAccount = accountService.updateAccount(accountNumber, accountDTO);
        return ResponseEntity.ok(updatedAccount);
    }
    
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/movimientos")
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = accountService.createTransaction(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }
    
    @GetMapping("/{accountNumber}/movimientos")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccount(@PathVariable String accountNumber) {
        List<TransactionDTO> transactions = accountService.getTransactionsByAccount(accountNumber);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/movimientos")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = accountService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/reportes")
    public ResponseEntity<AccountStatementDTO> getAccountStatement(
            @RequestParam("clientId") Long clientId,
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin) {
        
        LocalDateTime startDateTime = fechaInicio.atStartOfDay();
        LocalDateTime endDateTime = fechaFin.atTime(23, 59, 59);
        
        AccountStatementDTO statement = accountService.getAccountStatement(clientId, startDateTime, endDateTime);
        return ResponseEntity.ok(statement);
    }
    
    //alternativo para reporte 
    @GetMapping("/reportes/simple/{clientId}")
    public ResponseEntity<AccountStatementDTO> getSimpleAccountStatement(@PathVariable("clientId") Long clientId) {
        // Usa el mes actual por defecto
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        
        AccountStatementDTO statement = accountService.getAccountStatement(clientId, startOfMonth, endOfMonth);
        return ResponseEntity.ok(statement);
    }
}