package com.javareto.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

/**
 * Configuración de la aplicación usando Singleton Pattern
 * Centraliza la configuración de la aplicación
 */
@Configuration
public class ApplicationConfig {
    
    // Configuraciones de la aplicación
    public static final BigDecimal MINIMUM_BALANCE = new BigDecimal("0.00");
    public static final BigDecimal MAXIMUM_TRANSACTION_AMOUNT = new BigDecimal("1000000.00");
    public static final int ACCOUNT_NUMBER_LENGTH = 6;
    public static final String ACCOUNT_NUMBER_PREFIX = "ACC";
    
    /**
     * Bean singleton para configuración de la aplicación
     */
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ApplicationSettings applicationSettings() {
        return new ApplicationSettings();
    }
    
    /**
     * Clase interna para configuración de la aplicación
     */
    public static class ApplicationSettings {
        
        private BigDecimal minimumBalance = MINIMUM_BALANCE;
        private BigDecimal maximumTransactionAmount = MAXIMUM_TRANSACTION_AMOUNT;
        private int accountNumberLength = ACCOUNT_NUMBER_LENGTH;
        private String accountNumberPrefix = ACCOUNT_NUMBER_PREFIX;
        
        public BigDecimal getMinimumBalance() {
            return minimumBalance;
        }
        
        public void setMinimumBalance(BigDecimal minimumBalance) {
            this.minimumBalance = minimumBalance;
        }
        
        public BigDecimal getMaximumTransactionAmount() {
            return maximumTransactionAmount;
        }
        
        public void setMaximumTransactionAmount(BigDecimal maximumTransactionAmount) {
            this.maximumTransactionAmount = maximumTransactionAmount;
        }
        
        public int getAccountNumberLength() {
            return accountNumberLength;
        }
        
        public void setAccountNumberLength(int accountNumberLength) {
            this.accountNumberLength = accountNumberLength;
        }
        
        public String getAccountNumberPrefix() {
            return accountNumberPrefix;
        }
        
        public void setAccountNumberPrefix(String accountNumberPrefix) {
            this.accountNumberPrefix = accountNumberPrefix;
        }
    }
} 