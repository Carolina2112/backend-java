// Servicio principal de clientes. Ahora extiende CrudFacade para reutilizar lógica CRUD.
// Refactorizado para aplicar el patrón Facade y mantener el código limpio y escalable.
// Ver README para detalles de la mejora.
package com.javareto.client.service;

import com.javareto.client.dto.ClientDTO;
import com.javareto.client.model.Client;
import com.javareto.client.repository.ClientRepository;
import com.javareto.client.exception.ResourceNotFoundException;
import com.javareto.client.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService extends CrudFacade<Client, Long> {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    protected ClientRepository getRepository() {
        return clientRepository;
    }

    public ClientDTO createClient(ClientDTO clientDTO) {
        if (clientRepository.existsByIdentification(clientDTO.getIdentification())) {
            throw new DuplicateResourceException("Ya existe un cliente con la identificación " + 
                clientDTO.getIdentification() + " en el sistema");
        }
        
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setGender(clientDTO.getGender());
        client.setAge(clientDTO.getAge());
        client.setIdentification(clientDTO.getIdentification());
        client.setAddress(clientDTO.getAddress());
        client.setPhone(clientDTO.getPhone());
        client.setPassword(clientDTO.getPassword());
        client.setStatus(clientDTO.getStatus() != null ? clientDTO.getStatus() : true);
        
        Client savedClient = super.create(client);
        return mapToDTO(savedClient);
    }

    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("El cliente con ID " + id + " no fue encontrado en el sistema"));
        
        if (clientDTO.getName() != null) client.setName(clientDTO.getName());
        if (clientDTO.getGender() != null) client.setGender(clientDTO.getGender());
        if (clientDTO.getAge() != null) client.setAge(clientDTO.getAge());
        if (clientDTO.getAddress() != null) client.setAddress(clientDTO.getAddress());
        if (clientDTO.getPhone() != null) client.setPhone(clientDTO.getPhone());
        if (clientDTO.getPassword() != null) client.setPassword(clientDTO.getPassword());
        if (clientDTO.getStatus() != null) client.setStatus(clientDTO.getStatus());
        
        Client updatedClient = super.update(client);
        return mapToDTO(updatedClient);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("El cliente con ID " + id + " no fue encontrado en el sistema"));
        
        // Soft delete
        client.setStatus(false);
        super.update(client);
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("El cliente con ID " + id + " no fue encontrado en el sistema"));
        return mapToDTO(client);
    }

    public ClientDTO getClientByIdentification(String identification) {
        Client client = clientRepository.findByIdentification(identification)
            .orElseThrow(() -> new ResourceNotFoundException("El cliente con identificación " + identification + " no fue encontrado en el sistema"));
        return mapToDTO(client);
    }
    
    public List<ClientDTO> getAllClients() {
        List<Client> clients = super.readAll();
        return clients.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    private ClientDTO mapToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setGender(client.getGender());
        dto.setAge(client.getAge());
        dto.setIdentification(client.getIdentification());
        dto.setAddress(client.getAddress());
        dto.setPhone(client.getPhone());
        dto.setPassword(client.getPassword());
        dto.setStatus(client.getStatus());
        return dto;
    }
}