package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Entity.Cliente;
import com.example.Prueba_Tecnica.Entity.Cuenta;
import com.example.Prueba_Tecnica.Repository.ClienteRepository;
import com.example.Prueba_Tecnica.Repository.CuentaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CuentaService cuentaService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = Cuenta.builder()
                .id(1L)
                .numeroCuenta("1234567890")
                .tipoCuenta(Cuenta.TipoCuenta.AHORROS)
                .saldo(new BigDecimal("1000.00"))
                .estado(Cuenta.EstadoCuenta.ACTIVA)
                .build();
    }

    @Test
    void whenFindAll_thenReturnList() {
        // Arrange
        when(cuentaRepository.findAll()).thenReturn(Arrays.asList(cuenta));

        // Act
        List<Cuenta> cuentas = cuentaService.findAll();

        // Assert
        assertNotNull(cuentas);
        assertEquals(1, cuentas.size());
        verify(cuentaRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnCuenta() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // Act
        Optional<Cuenta> found = cuentaService.findById(1L);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(cuenta.getId(), found.get().getId());
        verify(cuentaRepository).findById(1L);
    }

    @Test
    void whenSave_thenSuccess() {
        // Arrange
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        // Act
        Cuenta saved = cuentaService.save(cuenta);

        // Assert
        assertNotNull(saved);
        assertEquals(cuenta.getNumeroCuenta(), saved.getNumeroCuenta());
        assertEquals(Cuenta.EstadoCuenta.ACTIVA, saved.getEstado());
        verify(cuentaRepository).save(any(Cuenta.class));
    }

    @Test
    void whenUpdate_thenSuccess() {
        // Arrange
        Cuenta cuentaActualizada = Cuenta.builder()
                .id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .saldo(new BigDecimal("2000.00"))
                .estado(cuenta.getEstado())
                .build();
        
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaActualizada);

        // Act
        Cuenta updated = cuentaService.update(cuentaActualizada, 1L);

        // Assert
        assertNotNull(updated);
        assertEquals(new BigDecimal("2000.00"), updated.getSaldo());
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).save(any(Cuenta.class));
    }

    @Test
    void whenUpdate_notFound_thenThrowException() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            cuentaService.update(cuenta, 1L)
        );

        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void whenDelete_thenSuccess() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        doNothing().when(cuentaRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> cuentaService.delete(1L));

        // Verify
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).deleteById(1L);
    }

    @Test
    void whenDelete_notFound_thenThrowException() {
        // Arrange
        when(cuentaRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            cuentaService.delete(1L)
        );

        verify(cuentaRepository).existsById(1L);
        verify(cuentaRepository, never()).deleteById(anyLong());
    }
}
