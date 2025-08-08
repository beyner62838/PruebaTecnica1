package com.example.Prueba_Tecnica.IService;

import com.example.Prueba_Tecnica.Dto.ClienteDTO;
import com.example.Prueba_Tecnica.Entity.Transaccion;
import java.util.List;

public interface ITransaccionService {
    void consignacion(ClienteDTO clienteDTO);
    void retiro(ClienteDTO clienteDTO);
    void transferencia(ClienteDTO clienteDTO);
    List<Transaccion> obtenerTransacciones();
    Transaccion obtenerTransaccionPorId(Long id);
}