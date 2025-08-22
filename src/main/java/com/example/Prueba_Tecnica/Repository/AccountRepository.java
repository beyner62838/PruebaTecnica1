package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
