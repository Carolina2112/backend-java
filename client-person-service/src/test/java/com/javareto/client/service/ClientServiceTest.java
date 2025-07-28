package com.javareto.client.service;

import com.javareto.client.dto.ClientDTO;
import com.javareto.client.exception.DuplicateResourceException;
import com.javareto.client.exception.ResourceNotFoundException;
import com.javareto.client.model.Client;
import com.javareto.client.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private ClientDTO clientDTO;
    private Client client;

    @BeforeEach
    void setUp() {
        // Setup ClientDTO
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

        // Setup Client
        client = new Client();
        client.setId(1L);
        client.setName("Juan Pérez");
        client.setGender("Masculino");
        client.setAge(30);
        client.setIdentification("1234567890");
        client.setAddress("Calle Principal 123");
        client.setPhone("0987654321");
        client.setPassword("password123");
        client.setStatus(true);
    }

    @Test
    void createClient_ShouldCreateClientSuccessfully() {
        // Given
        when(clientRepository.existsByIdentification("1234567890")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        ClientDTO result = clientService.createClient(clientDTO);

        // Then
        assertNotNull(result);
        assertEquals("Juan Pérez", result.getName());
        assertEquals("1234567890", result.getIdentification());
        assertEquals(30, result.getAge());
        assertTrue(result.getStatus());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void createClient_ShouldThrowException_WhenIdentificationExists() {
        // Given
        when(clientRepository.existsByIdentification("1234567890")).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> clientService.createClient(clientDTO));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void getClientById_ShouldReturnClient_WhenClientExists() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // When
        ClientDTO result = clientService.getClientById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Juan Pérez", result.getName());
        assertEquals("1234567890", result.getIdentification());
        assertEquals(1L, result.getId());
    }

    @Test
    void getClientById_ShouldThrowException_WhenClientDoesNotExist() {
        // Given
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(999L));
    }

    @Test
    void getClientByIdentification_ShouldReturnClient_WhenClientExists() {
        // Given
        when(clientRepository.findByIdentification("1234567890")).thenReturn(Optional.of(client));

        // When
        ClientDTO result = clientService.getClientByIdentification("1234567890");

        // Then
        assertNotNull(result);
        assertEquals("Juan Pérez", result.getName());
        assertEquals("1234567890", result.getIdentification());
    }

    @Test
    void getClientByIdentification_ShouldThrowException_WhenClientDoesNotExist() {
        // Given
        when(clientRepository.findByIdentification("9999999999")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientByIdentification("9999999999"));
    }

    @Test
    void getAllClients_ShouldReturnAllClients() {
        // Given
        List<Client> clients = Arrays.asList(client);
        when(clientRepository.findAll()).thenReturn(clients);

        // When
        List<ClientDTO> result = clientService.getAllClients();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getName());
        assertEquals("1234567890", result.get(0).getIdentification());
    }

    @Test
    void updateClient_ShouldUpdateClientSuccessfully() {
        // Given
        ClientDTO updateDTO = new ClientDTO();
        updateDTO.setName("Juan Carlos Pérez");
        updateDTO.setAge(31);
        updateDTO.setAddress("Nueva Dirección 456");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        ClientDTO result = clientService.updateClient(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void updateClient_ShouldThrowException_WhenClientDoesNotExist() {
        // Given
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> clientService.updateClient(999L, clientDTO));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void deleteClient_ShouldSoftDeleteClient() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        clientService.deleteClient(1L);

        // Then
        verify(clientRepository, times(1)).save(any(Client.class));
        assertFalse(client.getStatus());
    }

    @Test
    void deleteClient_ShouldThrowException_WhenClientDoesNotExist() {
        // Given
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> clientService.deleteClient(999L));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void createClient_ShouldSetStatusToTrue_WhenCreatingNewClient() {
        // Given
        clientDTO.setStatus(null); // No status set
        when(clientRepository.existsByIdentification("1234567890")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        ClientDTO result = clientService.createClient(clientDTO);

        // Then
        assertNotNull(result);
        assertTrue(result.getStatus());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void updateClient_ShouldPreserveStatus_WhenStatusNotProvided() {
        // Given
        ClientDTO updateDTO = new ClientDTO();
        updateDTO.setName("Juan Carlos Pérez");
        // No status provided

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        ClientDTO result = clientService.updateClient(1L, updateDTO);

        // Then
        assertNotNull(result);
        assertTrue(result.getStatus()); // Should preserve original status
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void getAllClients_ShouldReturnEmptyList_WhenNoClientsExist() {
        // Given
        when(clientRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<ClientDTO> result = clientService.getAllClients();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
} 