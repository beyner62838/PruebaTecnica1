package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Entity.Cuenta;
import java.util.List;
import java.util.Optional;

public interface ICuentaService {
    List<Cuenta> findAll();
    Optional<Cuenta> findById(Long id);
    Cuenta save(Cuenta cuenta);
    Cuenta update(Cuenta cuenta, Long id);
    void delete(Long id);
    void activateCuenta(Long id);
    void deactivateCuenta(Long id);
    void cancelateCuenta(Long id);
}