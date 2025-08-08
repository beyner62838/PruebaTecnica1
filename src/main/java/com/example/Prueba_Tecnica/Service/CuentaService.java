package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Cuenta;
import com.example.Prueba_Tecnica.Repository.CuentaRepository;
import com.example.Prueba_Tecnica.IService.ICuentaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CuentaService implements ICuentaService {
    
    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    public Cuenta save(Cuenta cuenta) {
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta update(Cuenta cuenta, Long id) {
        return cuentaRepository.findById(id)
            .map(existingCuenta -> {
                existingCuenta.setTipoCuenta(cuenta.getTipoCuenta());
                existingCuenta.setSaldo(cuenta.getSaldo());
                existingCuenta.setEstado(cuenta.getEstado());
                return cuentaRepository.save(existingCuenta);
            })
            .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (!cuentaRepository.existsById(id)) {
            throw new EntityNotFoundException("Cuenta no encontrada con id: " + id);
        }
        cuentaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activateCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + id));
        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional
    public void deactivateCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + id));
        cuenta.setEstado(Cuenta.EstadoCuenta.INACTIVA);
        cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional
    public void cancelateCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + id));
        if (cuenta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("No se puede cancelar una cuenta con saldo diferente a 0");
        }
        cuenta.setEstado(Cuenta.EstadoCuenta.CANCELADA);
        cuentaRepository.save(cuenta);
    }
}
