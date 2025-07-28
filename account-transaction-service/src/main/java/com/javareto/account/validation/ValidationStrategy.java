package com.javareto.account.validation;

import com.javareto.account.dto.AccountDTO;
import com.javareto.account.dto.TransactionDTO;

/**
 * Interfaz para el patrón Strategy de validaciones
 * Permite implementar diferentes estrategias de validación
 */
public interface ValidationStrategy {
    
    /**
     * Valida una cuenta
     * @param accountDTO DTO de la cuenta a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validateAccount(AccountDTO accountDTO);
    
    /**
     * Valida una transacción
     * @param transactionDTO DTO de la transacción a validar
     * @throws IllegalArgumentException si la validación falla
     */
    void validateTransaction(TransactionDTO transactionDTO);
} 