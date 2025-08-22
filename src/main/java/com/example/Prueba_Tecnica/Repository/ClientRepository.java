package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByIdentificationNumber(String identificationNumber);
    boolean existsByEmail(String email);
    Optional<Client> findByIdentificationNumber(String identificationNumber);
}
