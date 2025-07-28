package com.javareto.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javareto.client.dto.ClientDTO;
import com.javareto.client.exception.DuplicateResourceException;
import com.javareto.client.controller.TestGlobalExceptionHandler;
import com.javareto.client.exception.ResourceNotFoundException;
import com.javareto.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setControllerAdvice(createGlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("Juan Pérez");
        clientDTO.setGender("Masculino");
        clientDTO.setAge(30);
        clientDTO.setIdentification("1234567890");
        clientDTO.setAddress("Calle Principal 123");
        clientDTO.setPhone("0987654321");
        clientDTO.setPassword("password123");
        clientDTO.setStatus(true);
    }

    @Test
    void createClient_ShouldReturnCreatedClient() throws Exception {
        // Given
        when(clientService.createClient(any(ClientDTO.class))).thenReturn(clientDTO);

        // When & Then
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.identification").value("1234567890"))
                .andExpect(jsonPath("$.age").value(30));

        verify(clientService, times(1)).createClient(any(ClientDTO.class));
    }

    @Test
    void createClient_ShouldReturn409_WhenIdentificationExists() throws Exception {
        // Given
        when(clientService.createClient(any(ClientDTO.class)))
                .thenThrow(new DuplicateResourceException("Ya existe un cliente con la identificación 1234567890 en el sistema"));

        // When & Then
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("CLI-002"));

        verify(clientService, times(1)).createClient(any(ClientDTO.class));
    }

    @Test
    void getClientById_ShouldReturnClient_WhenClientExists() throws Exception {
        // Given
        when(clientService.getClientById(1L)).thenReturn(clientDTO);

        // When & Then
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.identification").value("1234567890"))
                .andExpect(jsonPath("$.id").value(1));

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void getClientById_ShouldReturn404_WhenClientDoesNotExist() throws Exception {
        // Given
        when(clientService.getClientById(999L))
                .thenThrow(new ResourceNotFoundException("El cliente con ID 999 no fue encontrado en el sistema"));

        // When & Then
        mockMvc.perform(get("/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("CLI-001"));

        verify(clientService, times(1)).getClientById(999L);
    }

    @Test
    void getClientByIdentification_ShouldReturnClient_WhenClientExists() throws Exception {
        // Given
        when(clientService.getClientByIdentification("1234567890")).thenReturn(clientDTO);

        // When & Then
        mockMvc.perform(get("/clientes/identification/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.identification").value("1234567890"));

        verify(clientService, times(1)).getClientByIdentification("1234567890");
    }

    @Test
    void getClientByIdentification_ShouldReturn404_WhenClientDoesNotExist() throws Exception {
        // Given
        when(clientService.getClientByIdentification("9999999999"))
                .thenThrow(new ResourceNotFoundException("El cliente con identificación 9999999999 no fue encontrado en el sistema"));

        // When & Then
        mockMvc.perform(get("/clientes/identification/9999999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("CLI-001"));

        verify(clientService, times(1)).getClientByIdentification("9999999999");
    }

    @Test
    void getAllClients_ShouldReturnAllClients() throws Exception {
        // Given
        List<ClientDTO> clients = Arrays.asList(clientDTO);
        when(clientService.getAllClients()).thenReturn(clients);

        // When & Then
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].identification").value("1234567890"));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void updateClient_ShouldReturnUpdatedClient() throws Exception {
        // Given
        when(clientService.updateClient(eq(1L), any(ClientDTO.class))).thenReturn(clientDTO);

        // When & Then
        mockMvc.perform(put("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Pérez"));

        verify(clientService, times(1)).updateClient(eq(1L), any(ClientDTO.class));
    }

    @Test
    void updateClient_ShouldReturn404_WhenClientDoesNotExist() throws Exception {
        // Given
        when(clientService.updateClient(eq(999L), any(ClientDTO.class)))
                .thenThrow(new ResourceNotFoundException("El cliente con ID 999 no fue encontrado en el sistema"));

        // When & Then
        mockMvc.perform(put("/clientes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("CLI-001"));

        verify(clientService, times(1)).updateClient(eq(999L), any(ClientDTO.class));
    }

    @Test
    void deleteClient_ShouldReturn204_WhenClientExists() throws Exception {
        // Given
        doNothing().when(clientService).deleteClient(1L);

        // When & Then
        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(1L);
    }

    @Test
    void deleteClient_ShouldReturn404_WhenClientDoesNotExist() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("El cliente con ID 999 no fue encontrado en el sistema"))
                .when(clientService).deleteClient(999L);

        // When & Then
        mockMvc.perform(delete("/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("CLI-001"));

        verify(clientService, times(1)).deleteClient(999L);
    }

    @Test
    void getAllClients_ShouldReturnEmptyList_WhenNoClientsExist() throws Exception {
        // Given
        when(clientService.getAllClients()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(clientService, times(1)).getAllClients();
    }
    
    private TestGlobalExceptionHandler createGlobalExceptionHandler() {
        return new TestGlobalExceptionHandler();
    }
}
