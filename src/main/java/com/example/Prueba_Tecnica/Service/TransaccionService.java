package com.example.Prueba_Tecnica.Service;

import com.example.Prueba_Tecnica.Dto.ClienteDTO;
import com.example.Prueba_Tecnica.Entity.Cuenta;
import com.example.Prueba_Tecnica.Entity.Transaccion;
import com.example.Prueba_Tecnica.IService.ITransaccionService;
import com.example.Prueba_Tecnica.Repository.CuentaRepository;
import com.example.Prueba_Tecnica.Repository.TransaccionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TransaccionService implements ITransaccionService {

    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    @Transactional
    public void consignacion(ClienteDTO clienteDTO) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(clienteDTO.getCuentaId());
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            BigDecimal valor = BigDecimal.valueOf(clienteDTO.getMonto());
            cuenta.setSaldo(cuenta.getSaldo().add(valor));
            cuentaRepository.save(cuenta);

            Transaccion transaccion = Transaccion.builder()
                    .tipoTransaccion(Transaccion.TipoTransaccion.CONSIGNACION)
                    .valor(valor)
                    .cuentaDestino(cuenta)
                    .descripcion("Consignación realizada")
                    .build();

            transaccionRepository.save(transaccion);
        } else {
            throw new RuntimeException("Cuenta no encontrada");
        }
    }

    @Transactional
    public void retiro(ClienteDTO clienteDTO) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(clienteDTO.getCuentaId());
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            BigDecimal valor = BigDecimal.valueOf(clienteDTO.getMonto());

            if (cuenta.getSaldo().compareTo(valor) >= 0) {
                cuenta.setSaldo(cuenta.getSaldo().subtract(valor));
                cuentaRepository.save(cuenta);

                Transaccion transaccion = Transaccion.builder()
                        .tipoTransaccion(Transaccion.TipoTransaccion.RETIRO)
                        .valor(valor)
                        .cuentaOrigen(cuenta)
                        .descripcion("Retiro realizado")
                        .build();

                transaccionRepository.save(transaccion);
            } else {
                throw new RuntimeException("Saldo insuficiente");
            }
        } else {
            throw new RuntimeException("Cuenta no encontrada");
        }
    }

    @Transactional
    public void transferencia(ClienteDTO clienteDTO) {
        Optional<Cuenta> cuentaOrigenOpt = cuentaRepository.findById(clienteDTO.getCuentaOrigenId());
        Optional<Cuenta> cuentaDestinoOpt = cuentaRepository.findById(clienteDTO.getCuentaDestinoId());

        if (cuentaOrigenOpt.isPresent() && cuentaDestinoOpt.isPresent()) {
            Cuenta cuentaOrigen = cuentaOrigenOpt.get();
            Cuenta cuentaDestino = cuentaDestinoOpt.get();
            BigDecimal valor = BigDecimal.valueOf(clienteDTO.getMonto());

            if (cuentaOrigen.getSaldo().compareTo(valor) >= 0) {
                cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(valor));
                cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(valor));

                cuentaRepository.save(cuentaOrigen);
                cuentaRepository.save(cuentaDestino);

                Transaccion transaccion = Transaccion.builder()
                        .tipoTransaccion(Transaccion.TipoTransaccion.TRANSFERENCIA)
                        .valor(valor)
                        .cuentaOrigen(cuentaOrigen)
                        .cuentaDestino(cuentaDestino)
                        .descripcion("Transferencia realizada")
                        .build();

                transaccionRepository.save(transaccion);
            } else {
                throw new RuntimeException("Saldo insuficiente en la cuenta origen");
            }
        } else {
            throw new RuntimeException("Cuenta origen o destino no encontrada");
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Transaccion> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Transaccion> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Transaccion> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Transaccion getOne(Long aLong) {
        return null;
    }

    @Override
    public Transaccion getById(Long aLong) {
        return null;
    }

    @Override
    public Transaccion getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Transaccion> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Transaccion> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Transaccion> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Transaccion> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Transaccion> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Transaccion> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Transaccion, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Transaccion> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Transaccion> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Transaccion> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Transaccion> findAll() {
        return List.of();
    }

    @Override
    public List<Transaccion> findAllById(Iterable<Long> longs) {
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
    public void delete(Transaccion entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Transaccion> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Transaccion> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Transaccion> findAll(Pageable pageable) {
        return null;
    }
}
