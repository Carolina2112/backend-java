package com.javareto.client.repository;

import com.javareto.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
	
	Optional<Client> findByIdentification(String identification);
    boolean existsByIdentification(String identification);
    Optional<Client> findByIdentificationAndPassword(String identification, String password);
	
}
