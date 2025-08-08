package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface IClienteService {
    Optional<Cliente> findById(String numeroIdentificacion);

    boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    boolean existsByCorreoElectronico(String correoElectronico);

    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    Cliente save(Cliente cliente);
    Cliente update(Cliente cliente, Long id);
    void delete(Long id);
    void delete(Class<Cliente> clienteClass);
}