package com.javareto.account.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AccountDTO {
	
    private String accountNumber;
    
    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(Saving|Checking)$", message = "Type account must be Saving or Checking")
    private String type;
    
    @NotNull(message = "Initial balance is required")
    @DecimalMin("0.0")
    private BigDecimal initialBalance;
    
    private Boolean status;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    private BigDecimal availableBalance;
    
   
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

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
}