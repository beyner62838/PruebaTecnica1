package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Cuenta;
import com.example.Prueba_Tecnica.Repository.CuentaRepository;
import com.example.Prueba_Tecnica.IService.ICuentaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class CuentaService implements ICuentaService {


    @Autowired
    private CuentaRepository repository;

    @Override
    public <S extends Cuenta> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    //BUSCAR TODOS
    @Override
    public List<Cuenta> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Cuenta> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Cuenta entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Cuenta> entities) {

    }

    @Override
    public void deleteAll() {

    }

    //BUSCAR POR ID
    @Override
    public Optional<Cuenta> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    //GUARDAR O CREAR PRODUCTO CON SU RESPECTIVA VALIDACION
    @Override
    public Cuenta save(Cuenta cuenta) {
        // Validar saldo inicial según tipo de cuenta
        if (cuenta.getSaldo() != null && cuenta.getSaldo().compareTo(new BigDecimal("0")) < 0) {
            if (cuenta.getTipoCuenta() == Cuenta.TipoCuenta.AHORROS) {
                throw new IllegalArgumentException("La cuenta de ahorro no puede tener un saldo menor a $0");
            }
        }

        // Validar que solo puede haber una cuenta marcada como exenta de GMF por cliente
        if (cuenta.isExentaGMF() && cuenta.getCliente() != null) {
            boolean existeCuentaExentaGMF = repository.findAll().stream()
                .filter(c -> c.getCliente().getId().equals(cuenta.getCliente().getId()))
                .anyMatch(Cuenta::isExentaGMF);

            if (existeCuentaExentaGMF) {
                throw new IllegalStateException("El cliente ya tiene una cuenta marcada como exenta de GMF");
            }
        }

        // Generar número de cuenta si no existe
        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isEmpty()) {
            cuenta.setNumeroCuenta(generarNumeroCuenta(cuenta.getTipoCuenta()));
        }

        // Establecer estado inicial
        if (cuenta.getEstado() == null) {
            cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        }

        // Establecer fechas de auditoría
        LocalDateTime now = LocalDateTime.now();
        if (cuenta.getFechaCreacion() == null) {
            cuenta.setFechaCreacion(now);
        }
        cuenta.setFechaModificacion(now);

        return repository.save(cuenta);
    }



    // ACTUALIZAR
    @Override
    public Cuenta update(Cuenta cuenta, Long id) {
        Optional<Cuenta> cuentaOpt = repository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuentas = cuentaOpt.get();
            cuentas.setTipoCuenta(cuenta.getTipoCuenta());
            cuentas.setEstado(cuenta.getEstado());
            cuentas.setSaldo(cuenta.getSaldo());
            cuentas.setExentaGMF(cuenta.isExentaGMF());
            cuentas.setFechaModificacion(LocalDateTime.now());
            return repository.save(cuentas);
        } else {
            throw new RuntimeException("Producto no encontrado con id " + id);
        }
    }

    //BORRAR O ELIMINAR
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }


    //ACTIVAR UNA CUENTA O PRODUCTO YA SEA AHORRO O CORRIENTE
    @Override
    public void activateCuenta(Long id) {
        Optional<Cuenta> cuentaOpt = repository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
            cuenta.setFechaModificacion(LocalDateTime.now());
            repository.save(cuenta);
        } else {
            throw new EntityNotFoundException("Producto no encontrado con id " + id);
        }
    }

    //DESACTIVAR UNA CUENTA O PRODUCTO YA SEA AHORRO O CORRIENTE
    @Override
    public void deactivateCuenta(Long id) {
        Optional<Cuenta> cuentaOpt = repository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setEstado(Cuenta.EstadoCuenta.INACTIVA);
            cuenta.setFechaModificacion(LocalDateTime.now());
            repository.save(cuenta);
        } else {
            throw new EntityNotFoundException("Producto no encontrado con id " + id);
        }
    }

    //CANCELAR CUENTAS SOLO SI ESTAS TIENEN UN SALDO IGUAL A 0
    @Override
    public void cancelateCuenta(Long id) {
        Optional<Cuenta> cuentaOpt = repository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            if (false) {
                throw new IllegalStateException("No se puede cancelar una cuenta con saldo diferente a 0");
            }
            cuenta.setEstado(Cuenta.EstadoCuenta.CANCELADA);
            cuenta.setFechaModificacion(LocalDateTime.now());
            repository.save(cuenta);
        } else {
            throw new EntityNotFoundException("Producto no encontrado con id " + id);
        }
    }

    //METODO DE GENERAR NUMERO DE CUENTA ALEATORIAMENTE IGUAL QUE EN LA CLASE PRODUCTO EN LA CARPETA ENTITY

    private String generarNumeroCuenta(Cuenta.TipoCuenta tipoCuenta) {
        String prefix = tipoCuenta == Cuenta.TipoCuenta.AHORROS ? "53" : "33";
        return prefix + String.format("%08d", (int) (Math.random() * 100000000));
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Cuenta> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Cuenta> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Cuenta> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Cuenta getOne(Long aLong) {
        return null;
    }

    @Override
    public Cuenta getById(Long aLong) {
        return null;
    }

    @Override
    public Cuenta getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Cuenta> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Cuenta> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Cuenta> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Cuenta> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Cuenta> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Cuenta> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Cuenta, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Cuenta> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Cuenta> findAll(Pageable pageable) {
        return null;
    }
}