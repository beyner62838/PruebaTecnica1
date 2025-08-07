package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
}
