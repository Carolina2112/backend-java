package com.javareto.account.repository;

import com.javareto.account.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByAccountAccountNumberOrderByDateDesc(String accountNumber);
    
    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.clientId = :clientId " +
           "AND t.date BETWEEN :startDate AND :endDate ORDER BY t.date DESC")
    List<Transaction> findByClientIdAndDateBetween(@Param("clientId") Long clientId, 
                                                   @Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);
}