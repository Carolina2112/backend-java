package com.javareto.account.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import com.javareto.account.exception.ResourceNotFoundException;

import java.util.Map;

@Component
public class ClientServiceClient {
    
    private final RestTemplate restTemplate;
    private final String clientServiceUrl;
    
    @Autowired
    public ClientServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.clientServiceUrl = "http://client-person-service";
    }
    
    public Map<String, Object> getClientById(Long clientId) {
        try {
            String url = clientServiceUrl + "/clientes/" + clientId;
            return restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Client not found with id: " + clientId);
        } catch (Exception ex) {
            throw new RuntimeException("Error communicating with client service: " + ex.getMessage());
        }
    }
    
    public boolean clientExists(Long clientId) {
        try {
            getClientById(clientId);
            return true;
        } catch (ResourceNotFoundException ex) {
            return false;
        }
    }
}