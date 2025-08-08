package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.ClienteDTO;
import com.example.Prueba_Tecnica.Entity.Cuenta;
import com.example.Prueba_Tecnica.Entity.Transaccion;
import com.example.Prueba_Tecnica.Repository.CuentaRepository;
import com.example.Prueba_Tecnica.Repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private ClienteDTO clienteDTO;
    private Transaccion transaccion;

    @BeforeEach
    void setUp() {
        cuentaOrigen = Cuenta.builder()
                .id(1L)
                .saldo(new BigDecimal("1000.00"))
                .build();

        cuentaDestino = Cuenta.builder()
                .id(2L)
                .saldo(new BigDecimal("500.00"))
                .build();

        clienteDTO = ClienteDTO.builder()
                .cuentaId(1L)
                .cuentaDestinoId(2L)
                .monto(new BigDecimal("100.00"))
                .build();

        transaccion = Transaccion.builder()
                .id(1L)
                .valor(new BigDecimal("100.00"))
                .tipoTransaccion(Transaccion.TipoTransaccion.CONSIGNACION)
                .build();
    }

    @Test
    void whenConsignacion_thenSuccess() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaOrigen);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        // Act
        assertDoesNotThrow(() -> transaccionService.consignacion(clienteDTO));

        // Assert
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).save(any(Cuenta.class));
        verify(transaccionRepository).save(any(Transaccion.class));
    }

    @Test
    void whenRetiro_withSufficientFunds_thenSuccess() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaOrigen);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        // Act
        assertDoesNotThrow(() -> transaccionService.retiro(clienteDTO));

        // Assert
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).save(any(Cuenta.class));
        verify(transaccionRepository).save(any(Transaccion.class));
    }

    @Test
    void whenRetiro_withInsufficientFunds_thenThrowException() {
        // Arrange
        clienteDTO.setMonto(new BigDecimal("2000.00"));
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> 
            transaccionService.retiro(clienteDTO)
        );

        assertEquals("Saldo insuficiente", exception.getMessage());
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void whenTransferencia_withSufficientFunds_thenSuccess() {
        // Arrange
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));
        when(cuentaRepository.findById(2L)).thenReturn(Optional.of(cuentaDestino));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaOrigen);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        // Act
        assertDoesNotThrow(() -> transaccionService.transferencia(clienteDTO));

        // Assert
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).findById(2L);
        verify(cuentaRepository, times(2)).save(any(Cuenta.class));
        verify(transaccionRepository).save(any(Transaccion.class));
    }

    @Test
    void whenTransferencia_withInsufficientFunds_thenThrowException() {
        // Arrange
        clienteDTO.setMonto(new BigDecimal("2000.00"));
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));
        when(cuentaRepository.findById(2L)).thenReturn(Optional.of(cuentaDestino));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> 
            transaccionService.transferencia(clienteDTO)
        );

        assertEquals("Saldo insuficiente para la transferencia", exception.getMessage());
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).findById(2L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void whenObtenerTransacciones_thenReturnList() {
        // Arrange
        when(transaccionRepository.findAll()).thenReturn(Arrays.asList(transaccion));

        // Act
        List<Transaccion> transacciones = transaccionService.obtenerTransacciones();

        // Assert
        assertNotNull(transacciones);
        assertEquals(1, transacciones.size());
        verify(transaccionRepository).findAll();
    }

    @Test
    void whenObtenerTransaccionPorId_thenReturnTransaccion() {
        // Arrange
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));

        // Act
        Transaccion found = transaccionService.obtenerTransaccionPorId(1L);

        // Assert
        assertNotNull(found);
        assertEquals(transaccion.getId(), found.getId());
        verify(transaccionRepository).findById(1L);
    }

    @Test
    void whenObtenerTransaccionPorId_notFound_thenThrowException() {
        // Arrange
        when(transaccionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> 
            transaccionService.obtenerTransaccionPorId(1L)
        );

        assertEquals("Transacción no encontrada", exception.getMessage());
        verify(transaccionRepository).findById(1L);
    }
}
