package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.ClienteDTO;
import com.example.Prueba_Tecnica.Entity.Cuenta;
import com.example.Prueba_Tecnica.Entity.Transaccion;
import com.example.Prueba_Tecnica.IService.ITransaccionService;
import com.example.Prueba_Tecnica.Repository.CuentaRepository;
import com.example.Prueba_Tecnica.Repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransaccionService implements ITransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public void consignacion(ClienteDTO clienteDTO) {
        Cuenta cuenta = cuentaRepository.findById(clienteDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        BigDecimal nuevoSaldo = cuenta.getSaldo().add(clienteDTO.getMonto());
        cuenta.setSaldo(nuevoSaldo);
        
        Transaccion transaccion = Transaccion.builder()
                .cuentaOrigen(cuenta)
                .tipoTransaccion(Transaccion.TipoTransaccion.CONSIGNACION)
                .valor(clienteDTO.getMonto())
                .fecha(LocalDateTime.now())
                .descripcion("Consignación")
                .build();

        transaccionRepository.save(transaccion);
        cuentaRepository.save(cuenta);
    }

    @Override
    public void retiro(ClienteDTO clienteDTO) {
        Cuenta cuenta = cuentaRepository.findById(clienteDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (cuenta.getSaldo().compareTo(clienteDTO.getMonto()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(clienteDTO.getMonto());
        cuenta.setSaldo(nuevoSaldo);

        Transaccion transaccion = Transaccion.builder()
                .cuentaOrigen(cuenta)
                .tipoTransaccion(Transaccion.TipoTransaccion.RETIRO)
                .valor(clienteDTO.getMonto())
                .fecha(LocalDateTime.now())
                .descripcion("Retiro")
                .build();

        transaccionRepository.save(transaccion);
        cuentaRepository.save(cuenta);
    }

    @Override
    public void transferencia(ClienteDTO clienteDTO) {
        Cuenta cuentaOrigen = cuentaRepository.findById(clienteDTO.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));

        Cuenta cuentaDestino = cuentaRepository.findById(clienteDTO.getCuentaDestinoId())
                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));

        if (cuentaOrigen.getSaldo().compareTo(clienteDTO.getMonto()) < 0) {
            throw new RuntimeException("Saldo insuficiente para la transferencia");
        }

        BigDecimal nuevoSaldoOrigen = cuentaOrigen.getSaldo().subtract(clienteDTO.getMonto());
        cuentaOrigen.setSaldo(nuevoSaldoOrigen);

        BigDecimal nuevoSaldoDestino = cuentaDestino.getSaldo().add(clienteDTO.getMonto());
        cuentaDestino.setSaldo(nuevoSaldoDestino);

        Transaccion transaccion = Transaccion.builder()
                .cuentaOrigen(cuentaOrigen)
                .cuentaDestino(cuentaDestino)
                .tipoTransaccion(Transaccion.TipoTransaccion.TRANSFERENCIA)
                .valor(clienteDTO.getMonto())
                .fecha(LocalDateTime.now())
                .descripcion("Transferencia entre cuentas")
                .build();

        transaccionRepository.save(transaccion);
        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);
    }

    @Override
    public List<Transaccion> obtenerTransacciones() {
        return transaccionRepository.findAll();
    }

    @Override
    public Transaccion obtenerTransaccionPorId(Long id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
    }
}
