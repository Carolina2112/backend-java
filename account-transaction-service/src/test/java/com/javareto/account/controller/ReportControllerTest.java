package com.javareto.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javareto.account.dto.AccountStatementDTO;
import com.javareto.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new TestGlobalExceptionHandler())
                .build();
    }

    @Test
    void getAccountStatement_ShouldReturnStatement_WhenValidParameters() throws Exception {
        // Given
        Long clientId = 1L;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        AccountStatementDTO statement = createSampleStatement(clientId, startDate, endDate);
        when(accountService.getAccountStatement(eq(clientId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(statement);

        // When & Then
        mockMvc.perform(get("/cuentas/reportes")
                        .param("clientId", clientId.toString())
                        .param("fechaInicio", "2024-01-01")
                        .param("fechaFin", "2024-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Juan Pérez"))
                .andExpect(jsonPath("$.accounts").isArray())
                .andExpect(jsonPath("$.accounts[0].accountNumber").value("123456789"))
                .andExpect(jsonPath("$.accounts[0].availableBalance").value(1500.00));
    }

    @Test
    void getAccountStatement_ShouldReturn400_WhenInvalidDateFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/cuentas/reportes")
                        .param("clientId", "1")
                        .param("fechaInicio", "invalid-date")
                        .param("fechaFin", "2024-01-31"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAccountStatement_ShouldReturn400_WhenMissingParameters() throws Exception {
        // When & Then
        mockMvc.perform(get("/cuentas/reportes")
                        .param("clientId", "1")
                        .param("fechaInicio", "2024-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSimpleAccountStatement_ShouldReturnStatement_WhenValidClientId() throws Exception {
        // Given
        Long clientId = 1L;
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfMonth = currentDate.withDayOfMonth(1);
        LocalDate endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        AccountStatementDTO statement = createSampleStatement(clientId, startOfMonth, endOfMonth);
        when(accountService.getAccountStatement(eq(clientId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(statement);

        // When & Then
        mockMvc.perform(get("/cuentas/reportes/simple/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Juan Pérez"))
                .andExpect(jsonPath("$.accounts").isArray());
    }

    private AccountStatementDTO createSampleStatement(Long clientId, LocalDate startDate, LocalDate endDate) {
        AccountStatementDTO statement = new AccountStatementDTO();
        statement.setClientName("Juan Pérez");
        statement.setStartDate(startDate.atStartOfDay());
        statement.setEndDate(endDate.atTime(23, 59, 59));

        List<AccountStatementDTO.AccountStatementDetail> accounts = new ArrayList<>();
        AccountStatementDTO.AccountStatementDetail accountDetail = new AccountStatementDTO.AccountStatementDetail();
        accountDetail.setAccountNumber("123456789");
        accountDetail.setType("Saving");
        accountDetail.setInitialBalance(new BigDecimal("1000.00"));
        accountDetail.setAvailableBalance(new BigDecimal("1500.00"));
        accountDetail.setStatus(true);

        List<AccountStatementDTO.TransactionDetail> transactions = new ArrayList<>();
        AccountStatementDTO.TransactionDetail transaction = new AccountStatementDTO.TransactionDetail();
        transaction.setDate(LocalDateTime.now());
        transaction.setType("Depósito");
        transaction.setValue(new BigDecimal("500.00"));
        transaction.setBalance(new BigDecimal("1500.00"));
        transactions.add(transaction);

        accountDetail.setTransactions(transactions);
        accounts.add(accountDetail);
        statement.setAccounts(accounts);

        return statement;
    }
} 