package com.javareto.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javareto.account.dto.AccountDTO;
import com.javareto.account.dto.TransactionDTO;
import com.javareto.account.controller.TestGlobalExceptionHandler;
import com.javareto.account.exception.InsufficientFundsException;
import com.javareto.account.exception.ResourceNotFoundException;
import com.javareto.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private AccountDTO accountDTO;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new TestGlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules(); // Para manejar LocalDateTime

        // Setup AccountDTO
        accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456");
        accountDTO.setType("Saving");
        accountDTO.setInitialBalance(new BigDecimal("1000"));
        accountDTO.setClientId(1L);
        accountDTO.setStatus(true);

        // Setup TransactionDTO
        transactionDTO = new TransactionDTO();
        transactionDTO.setAccountNumber("123456");
        transactionDTO.setType("Depósito");
        transactionDTO.setValue(new BigDecimal("500"));
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        // Given
        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(accountDTO);

        // When & Then
        mockMvc.perform(post("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.type").value("Saving"))
                .andExpect(jsonPath("$.initialBalance").value(1000));

        verify(accountService, times(1)).createAccount(any(AccountDTO.class));
    }

    @Test
    void getAccount_ShouldReturnAccount_WhenAccountExists() throws Exception {
        // Given
        when(accountService.getAccount("123456")).thenReturn(accountDTO);

        // When & Then
        mockMvc.perform(get("/cuentas/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.type").value("Saving"));

        verify(accountService, times(1)).getAccount("123456");
    }

    @Test
    void getAccount_ShouldReturn404_WhenAccountDoesNotExist() throws Exception {
        // Given
        when(accountService.getAccount("999999"))
                .thenThrow(new ResourceNotFoundException("La cuenta con número 999999 no fue encontrada en el sistema"));

        // When & Then
        mockMvc.perform(get("/cuentas/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACC-001"));

        verify(accountService, times(1)).getAccount("999999");
    }

    @Test
    void getAllAccounts_ShouldReturnAllAccounts() throws Exception {
        // Given
        List<AccountDTO> accounts = Arrays.asList(accountDTO);
        when(accountService.getAllAccounts()).thenReturn(accounts);

        // When & Then
        mockMvc.perform(get("/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("123456"))
                .andExpect(jsonPath("$[0].type").value("Saving"));

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount() throws Exception {
        // Given
        when(accountService.updateAccount(eq("123456"), any(AccountDTO.class))).thenReturn(accountDTO);

        // When & Then
        mockMvc.perform(put("/cuentas/123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("123456"));

        verify(accountService, times(1)).updateAccount(eq("123456"), any(AccountDTO.class));
    }

    @Test
    void updateAccount_ShouldReturn404_WhenAccountDoesNotExist() throws Exception {
        // Given
        when(accountService.updateAccount(eq("999999"), any(AccountDTO.class)))
                .thenThrow(new ResourceNotFoundException("La cuenta con número 999999 no fue encontrada en el sistema"));

        // When & Then
        mockMvc.perform(put("/cuentas/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACC-001"));

        verify(accountService, times(1)).updateAccount(eq("999999"), any(AccountDTO.class));
    }

    @Test
    void deleteAccount_ShouldReturn204_WhenAccountExists() throws Exception {
        // Given
        doNothing().when(accountService).deleteAccount("123456");

        // When & Then
        mockMvc.perform(delete("/cuentas/123456"))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount("123456");
    }

    @Test
    void deleteAccount_ShouldReturn404_WhenAccountDoesNotExist() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("La cuenta con número 999999 no fue encontrada en el sistema"))
                .when(accountService).deleteAccount("999999");

        // When & Then
        mockMvc.perform(delete("/cuentas/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACC-001"));

        verify(accountService, times(1)).deleteAccount("999999");
    }

    @Test
    void createTransaction_ShouldReturnCreatedTransaction() throws Exception {
        // Given
        TransactionDTO createdTransaction = new TransactionDTO();
        createdTransaction.setId(1L);
        createdTransaction.setAccountNumber("123456");
        createdTransaction.setType("Depósito");
        createdTransaction.setValue(new BigDecimal("500"));

        when(accountService.createTransaction(any(TransactionDTO.class))).thenReturn(createdTransaction);

        // When & Then
        mockMvc.perform(post("/cuentas/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.type").value("Depósito"))
                .andExpect(jsonPath("$.value").value(500));

        verify(accountService, times(1)).createTransaction(any(TransactionDTO.class));
    }

    @Test
    void createTransaction_ShouldReturn400_WhenInsufficientFunds() throws Exception {
        // Given
        transactionDTO.setType("Retiro");
        transactionDTO.setValue(new BigDecimal("2000"));

        when(accountService.createTransaction(any(TransactionDTO.class)))
                .thenThrow(new InsufficientFundsException("Saldo insuficiente para realizar la transacción. Saldo disponible: 1000"));

        // When & Then
        mockMvc.perform(post("/cuentas/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("TRX-001"));

        verify(accountService, times(1)).createTransaction(any(TransactionDTO.class));
    }

    @Test
    void getTransactionsByAccount_ShouldReturnTransactions() throws Exception {
        // Given
        List<TransactionDTO> transactions = Arrays.asList(transactionDTO);
        when(accountService.getTransactionsByAccount("123456")).thenReturn(transactions);

        // When & Then
        mockMvc.perform(get("/cuentas/123456/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("123456"))
                .andExpect(jsonPath("$[0].type").value("Depósito"));

        verify(accountService, times(1)).getTransactionsByAccount("123456");
    }

    @Test
    void getAccountsByClient_ShouldReturnClientAccounts() throws Exception {
        // Given
        List<AccountDTO> accounts = Arrays.asList(accountDTO);
        when(accountService.getAccountsByClient(1L)).thenReturn(accounts);

        // When & Then
        mockMvc.perform(get("/cuentas/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("123456"))
                .andExpect(jsonPath("$[0].clientId").value(1));

        verify(accountService, times(1)).getAccountsByClient(1L);
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() throws Exception {
        // Given
        List<TransactionDTO> transactions = Arrays.asList(transactionDTO);
        when(accountService.getAllTransactions()).thenReturn(transactions);

        // When & Then
        mockMvc.perform(get("/cuentas/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("123456"))
                .andExpect(jsonPath("$[0].type").value("Depósito"));

        verify(accountService, times(1)).getAllTransactions();
    }
} 