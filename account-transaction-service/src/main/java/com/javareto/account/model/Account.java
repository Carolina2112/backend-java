package com.javareto.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {
    
    @Id
    @Column(name = "account_number", length = 20)
    private String accountNumber;
    
    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(Saving|Checking)$", 
             message = "Type must be Saving or Checking")
    @Column(name = "type", nullable = false, length = 20)
    private String type;
    
    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", message = "Initial balance must be greater than or equal to 0")
    @Column(name = "initial_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal initialBalance;
    
    @NotNull(message = "Status is required")
    @Column(name = "status", nullable = false)
    private Boolean status = true;
    
    @NotNull(message = "Client is required")
    @Column(name = "client_id", nullable = false)
    private Long clientId;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
    
    // Calculated field
    @Transient
    private BigDecimal availableBalance;
    
    // Constructors
    public Account() {}
    
    public Account(String accountNumber, String type, BigDecimal initialBalance, Long clientId) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.initialBalance = initialBalance;
        this.clientId = clientId;
        this.status = true;
    }
    
    // Method to calculate available balance
    public BigDecimal calculateAvailableBalance() {
        if (transactions == null || transactions.isEmpty()) {
            return initialBalance;
        }
        
        BigDecimal totalTransactions = transactions.stream()
            .map(Transaction::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        return initialBalance.add(totalTransactions);
    }
    
    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
    
    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
    
    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
    
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    public BigDecimal getAvailableBalance() {
        return calculateAvailableBalance();
    }
}