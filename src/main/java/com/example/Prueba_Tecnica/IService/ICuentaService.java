package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICuentaService extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findAll();
    Optional<Cuenta> findById(Long id);
    Cuenta save(Cuenta cuenta);
    Cuenta update(Cuenta cuenta, Long id);
    void delete(Long id);
    void activateCuenta(Long id);
    void deactivateCuenta(Long id);
    void cancelateCuenta(Long id);
}