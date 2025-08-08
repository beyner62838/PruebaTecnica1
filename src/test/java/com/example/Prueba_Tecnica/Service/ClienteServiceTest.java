package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Cliente;
import com.example.Prueba_Tecnica.Repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clientePrueba;

    @BeforeEach
    void setUp() {
        clientePrueba = Cliente.builder()
                .id(1L)
                .nombres("breyner")
                .apellidos("Pertuz")
                .numeroIdentificacion("123456789")
                .correoElectronico("brejo@test.com")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .fechaCreacion(LocalDateTime.now())
                .fechaModificacion(LocalDateTime.now())
                .build();
    }

    @Test
    void whenFindAll_thenReturnListOfClientes() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(clientePrueba));

        // Act
        List<Cliente> found = clienteService.findAll();

        // Assert
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clientePrueba));

        // Act
        Optional<Cliente> found = clienteService.findById(1L);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(clientePrueba.getId(), found.get().getId());
        verify(clienteRepository).findById(1L);
    }

    @Test
    void whenSaveClienteMayorDeEdad_thenReturnCliente() {
        // Arrange
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clientePrueba);

        // Act
        Cliente saved = clienteService.save(clientePrueba);

        // Assert
        assertNotNull(saved);
        assertEquals(clientePrueba.getNombres(), saved.getNombres());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void whenSaveClienteMenorDeEdad_thenThrowException() {
        // Arrange
        Cliente clienteMenor = Cliente.builder()
                .nombres("Menor")
                .apellidos("De Edad")
                .numeroIdentificacion("987654321")
                .correoElectronico("menor@test.com")
                .fechaNacimiento(LocalDate.now().minusYears(17))
                .build();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.save(clienteMenor);
        });

        assertEquals("El cliente debe ser mayor de edad.", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void whenExistsByNumeroIdentificacion_thenReturnTrue() {
        // Arrange
        when(clienteRepository.existsByNumeroIdentificacion("123456789")).thenReturn(true);

        // Act
        boolean exists = clienteService.existsByNumeroIdentificacion("123456789");

        // Assert
        assertTrue(exists);
        verify(clienteRepository).existsByNumeroIdentificacion("123456789");
    }

    @Test
    void whenExistsByCorreoElectronico_thenReturnTrue() {
        // Arrange
        when(clienteRepository.existsByCorreoElectronico("juan.perez@test.com")).thenReturn(true);

        // Act
        boolean exists = clienteService.existsByCorreoElectronico("juan.perez@test.com");

        // Assert
        assertTrue(exists);
        verify(clienteRepository).existsByCorreoElectronico("juan.perez@test.com");
    }

    @Test
    void whenUpdateCliente_thenReturnUpdatedCliente() {
        // Arrange
        Cliente clienteActualizado = Cliente.builder()
                .id(1L)
                .nombres("Juan Actualizado")
                .apellidos("Pérez")
                .numeroIdentificacion("123456789")
                .correoElectronico("juan.perez@test.com")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clientePrueba));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        // Act
        Cliente updated = clienteService.update(clienteActualizado, 1L);

        // Assert
        assertNotNull(updated);
        assertEquals("Juan Actualizado", updated.getNombres());
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void whenUpdateClienteNotFound_thenThrowException() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.update(clientePrueba, 1L);
        });

        assertEquals("Cliente no encontrado con id 1", exception.getMessage());
        verify(clienteRepository).findById(1L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void whenDeleteCliente_thenDeleteSuccessfully() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clientePrueba));
        doNothing().when(clienteRepository).deleteById(1L);

        // Act
        clienteService.delete(1L);

        // Assert
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void whenDeleteClienteNotFound_thenThrowException() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.delete(1L);
        });

        assertEquals("Cliente no encontrado con id 1", exception.getMessage());
        verify(clienteRepository).findById(1L);
        verify(clienteRepository, never()).deleteById(anyLong());
    }
}
