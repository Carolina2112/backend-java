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
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    public ClientDTO createClient(ClientDTO clientDTO) {
        if (clientRepository.existsByIdentification(clientDTO.getIdentification())) {
            throw new DuplicateResourceException("Client with identification " + 
                clientDTO.getIdentification() + " already exists");
        }
        
        Client client = mapToEntity(clientDTO);
        client.setStatus(true);
        Client savedClient = clientRepository.save(client);
        
        return mapToDTO(savedClient);
    }
    
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        client.setName(clientDTO.getName());
        client.setGender(clientDTO.getGender());
        client.setAge(clientDTO.getAge());
        client.setAddress(clientDTO.getAddress());
        client.setPhone(clientDTO.getPhone());
        client.setPassword(clientDTO.getPassword());
        if (clientDTO.getStatus() != null) {
            client.setStatus(clientDTO.getStatus());
        }
        
        Client updatedClient = clientRepository.save(client);
        return mapToDTO(updatedClient);
    }
    
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        client.setStatus(false);
        clientRepository.save(client);
    }
    
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        
        return mapToDTO(client);
    }
    
    public ClientDTO getClientByIdentification(String identification) {
        Client client = clientRepository.findByIdentification(identification)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with identification: " + identification));
        
        return mapToDTO(client);
    }
    
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    
    private Client mapToEntity(ClientDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setGender(dto.getGender());
        client.setAge(dto.getAge());
        client.setIdentification(dto.getIdentification());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setPassword(dto.getPassword());
        client.setStatus(dto.getStatus());
        return client;
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