package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
