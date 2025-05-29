package com.javareto.client.controller;

import com.javareto.client.dto.ClientDTO;
import com.javareto.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ClientDTO client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new ClientDTO();
        client.setId(1L);
        client.setName("Cesar Jimenez");
        client.setGender("Male");
        client.setAge(23);
        client.setIdentification("0995665747");
        client.setAddress("Solanda");
        client.setPhone("0982547896");
        client.setPassword("9635");
        client.setStatus(true);
    }

    @Test
    void testGetClientById_returnsExpectedClient() {
        when(clientService.getClientById(1L)).thenReturn(client);

        ResponseEntity<ClientDTO> response = clientController.getClientById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cesar Jimenez", response.getBody().getName());
        assertEquals("0995665747", response.getBody().getIdentification());
    }

    @Test
    void testDeleteClient() {
        doNothing().when(clientService).deleteClient(1L);

        ResponseEntity<Void> response = clientController.deleteClient(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(clientService, times(1)).deleteClient(1L);
    }
}
