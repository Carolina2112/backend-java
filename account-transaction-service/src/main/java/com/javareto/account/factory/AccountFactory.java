package com.javareto.account.factory;

import com.javareto.account.dto.AccountDTO;
import com.javareto.account.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Factory Pattern para crear objetos Account
 * Centraliza la lógica de creación de entidades
 */
@Component
public class AccountFactory {
    
    /**
     * Crea una entidad Account desde un DTO
     * @param accountDTO DTO con los datos de la cuenta
     * @return Entidad Account creada
     */
    public Account createAccountFromDTO(AccountDTO accountDTO) {
        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setType(accountDTO.getType());
        account.setInitialBalance(accountDTO.getInitialBalance());
        account.setClientId(accountDTO.getClientId());
        account.setStatus(accountDTO.getStatus() != null ? accountDTO.getStatus() : true);
        return account;
    }
    
    /**
     * Crea una cuenta de ahorros por defecto
     * @param clientId ID del cliente
     * @param initialBalance Saldo inicial
     * @return Entidad Account creada
     */
    public Account createSavingAccount(Long clientId, BigDecimal initialBalance) {
        Account account = new Account();
        account.setType("Saving");
        account.setInitialBalance(initialBalance);
        account.setClientId(clientId);
        account.setStatus(true);
        return account;
    }
    
    /**
     * Crea una cuenta corriente por defecto
     * @param clientId ID del cliente
     * @param initialBalance Saldo inicial
     * @return Entidad Account creada
     */
    public Account createCheckingAccount(Long clientId, BigDecimal initialBalance) {
        Account account = new Account();
        account.setType("Checking");
        account.setInitialBalance(initialBalance);
        account.setClientId(clientId);
        account.setStatus(true);
        return account;
    }
} 