package com.javareto.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AccountStatementDTO {
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String clientName;
	private List<AccountStatementDetail> accounts;

	public static class AccountStatementDetail {
		private String accountNumber;
		private String type;
		private BigDecimal initialBalance;
		private Boolean status;
		private List<TransactionDetail> transactions;
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

		public List<TransactionDetail> getTransactions() {
			return transactions;
		}

		public void setTransactions(List<TransactionDetail> transactions) {
			this.transactions = transactions;
		}

		public BigDecimal getAvailableBalance() {
			return availableBalance;
		}

		public void setAvailableBalance(BigDecimal availableBalance) {
			this.availableBalance = availableBalance;
		}
	}

	public static class TransactionDetail {
		private LocalDateTime date;
		private String type;
		private BigDecimal value;
		private BigDecimal balance;

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
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<AccountStatementDetail> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountStatementDetail> accounts) {
		this.accounts = accounts;
	}
}