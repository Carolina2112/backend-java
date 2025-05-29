package com.javareto.account.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.javareto.account.model.Account;
import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, String> {
	
	List<Account> findByClientId(Long clientId);
	Optional<Account> findByAccountNumberAndStatus(String accountNumber, Boolean status);
    boolean existsByAccountNumber(String accountNumber);

}
