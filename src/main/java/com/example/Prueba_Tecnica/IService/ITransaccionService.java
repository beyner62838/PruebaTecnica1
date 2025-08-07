package com.example.Prueba_Tecnica.IService;



import com.example.Prueba_Tecnica.Dto.ClienteDTO;
import com.example.Prueba_Tecnica.Entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransaccionService extends JpaRepository<Transaccion, Long> {
    void consignacion(ClienteDTO clienteDTO);

    void retiro(ClienteDTO clienteDTO);

    void transferencia(ClienteDTO clienteDTO);
    }