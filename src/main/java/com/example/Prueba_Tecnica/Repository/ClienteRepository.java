package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByNumId(int numId);
}