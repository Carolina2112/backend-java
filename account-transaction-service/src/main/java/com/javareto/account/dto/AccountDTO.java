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
    
    // Constructor privado para Builder
    private AccountDTO() {}
    
    // Builder Pattern
    public static class Builder {
        private AccountDTO accountDTO;
        
        public Builder() {
            accountDTO = new AccountDTO();
        }
        
        public Builder accountNumber(String accountNumber) {
            accountDTO.accountNumber = accountNumber;
            return this;
        }
        
        public Builder type(String type) {
            accountDTO.type = type;
            return this;
        }
        
        public Builder initialBalance(BigDecimal initialBalance) {
            accountDTO.initialBalance = initialBalance;
            return this;
        }
        
        public Builder status(Boolean status) {
            accountDTO.status = status;
            return this;
        }
        
        public Builder clientId(Long clientId) {
            accountDTO.clientId = clientId;
            return this;
        }
        
        public Builder availableBalance(BigDecimal availableBalance) {
            accountDTO.availableBalance = availableBalance;
            return this;
        }
        
        public AccountDTO build() {
            return accountDTO;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters y Setters
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