package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Cliente;
import com.example.Prueba_Tecnica.IService.IClienteService;
import com.example.Prueba_Tecnica.Repository.ClienteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService implements IClienteService {


    @Autowired
    private ClienteRepository repository;

    @Override
    public Optional<Cliente> findById(String numeroIdentificacion) {
        return Optional.empty();
    }

    @Override
    public boolean existsByNumeroIdentificacion(String numeroIdentificacion) {
        return false;
    }

    @Override
    public boolean existsByCorreoElectronico(String correoElectronico) {
        return false;
    }

    @Override
    public <S extends Cliente> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    // ENCONTRAR TODOS LOS CLIENTES
    @Override
    public List<Cliente> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Cliente> findAllById(Iterable<Long> longs) {
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
    public void delete(Cliente entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Cliente> entities) {

    }

    @Override
    public void deleteAll() {

    }

    // BUSCAR POR ID
    @Override
    public Optional<Cliente> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    //GUARDAR O CREAR CLIENTE PERO ESTE NO DEBE SER MENOR DE EDAD POR ESO SE LE PONE UNA VALIDACION
    @Override
    public Cliente save(@Valid Cliente cliente) {
        if (!isOfAge(cliente.getFechaNacimiento())) {
            throw new IllegalArgumentException("El cliente debe ser mayor de edad.");
        }
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setFechaModificacion(LocalDateTime.now());
        return repository.save(cliente);
    }

    //METODO DE VALIDACION PARA QUE EL CLIENTE SEA MAYOR DE EDAD
    private boolean isOfAge(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
    }

    //ACTUALIZAR
    @Override
    public Cliente update(@Valid Cliente clienteDetalles, Long id) {
        Optional<Cliente> clienteOpt = repository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setTipoIdentificacion(clienteDetalles.getTipoIdentificacion());
            cliente.setNumeroIdentificacion(clienteDetalles.getNumeroIdentificacion());
            cliente.setNombres(clienteDetalles.getNombres());
            cliente.setApellidos(clienteDetalles.getApellidos());
            cliente.setCorreoElectronico(clienteDetalles.getCorreoElectronico());
            cliente.setFechaNacimiento(clienteDetalles.getFechaNacimiento());
            cliente.setFechaModificacion(LocalDateTime.now());
            return repository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado con id " + id);
        }

    }

    //BORRAR O ELIMINAR
    @Override
    public void delete(Long id) {
        Optional<Cliente> clienteOpt = repository.findById(id);
        if (clienteOpt.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente no encontrado con id " + id);
        }

    }

    @Override
    public void delete(Class<Cliente> clienteClass) {

    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Cliente> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Cliente> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Cliente> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Cliente getOne(Long aLong) {
        return null;
    }

    @Override
    public Cliente getById(Long aLong) {
        return null;
    }

    @Override
    public Cliente getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Cliente> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Cliente> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Cliente> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Cliente> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Cliente> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Cliente> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Cliente, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Cliente> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return null;
    }
}