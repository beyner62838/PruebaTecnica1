package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Cliente;
import com.example.Prueba_Tecnica.IService.IClienteService;
import com.example.Prueba_Tecnica.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClienteService {

    @Autowired
    private ClienteRepository repository;

    @Override
    public Optional<Cliente> findById(String numeroIdentificacion) {
        // Este método debe implementarse si se necesita buscar por número de identificación
        // Por ahora retorna vacío ya que el repositorio no tiene este método
        return Optional.empty();
    }

    @Override
    public boolean existsByNumeroIdentificacion(String numeroIdentificacion) {
        return repository.existsByNumeroIdentificacion(numeroIdentificacion);
    }

    @Override
    public boolean existsByCorreoElectronico(String correoElectronico) {
        return repository.existsByCorreoElectronico(correoElectronico);
    }

    @Override
    public List<Cliente> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Cliente save(@Valid Cliente cliente) {
        if (!isOfAge(cliente.getFechaNacimiento())) {
            throw new IllegalArgumentException("El cliente debe ser mayor de edad.");
        }
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setFechaModificacion(LocalDateTime.now());
        return repository.save(cliente);
    }

    private boolean isOfAge(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
    }

    @Override
    public Cliente update(Cliente clienteDetalles, Long id) {
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
        repository.deleteAll();
    }
}
