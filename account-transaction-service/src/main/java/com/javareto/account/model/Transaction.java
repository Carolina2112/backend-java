package com.javareto.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Date is required")
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    
    @NotBlank(message = "Transaction type is required")
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    
    @NotNull(message = "Value is required")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;
    
    @NotNull(message = "Balance is required")
    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;
    
    // Constructors
    public Transaction() {
        this.date = LocalDateTime.now();
    }
    
    public Transaction(String type, BigDecimal value, BigDecimal balance, Account account) {
        this.date = LocalDateTime.now();
        this.type = type;
        this.value = value;
        this.balance = balance;
        this.account = account;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public Account getAccount() {
        return account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
}