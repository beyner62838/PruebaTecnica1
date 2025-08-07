package com.example.Prueba_Tecnica.Repository;

import com.example.Prueba_Tecnica.Entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
