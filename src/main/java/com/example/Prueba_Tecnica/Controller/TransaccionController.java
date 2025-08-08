package com.example.Prueba_Tecnica.Controller;
import com.example.Prueba_Tecnica.Dto.ClienteDTO;
import com.example.Prueba_Tecnica.IService.ITransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    @Qualifier("transaccionService")
    private ITransaccionService transacciones;


    @PostMapping("/consignacion")
    public ResponseEntity<String> consignacion(@RequestBody  ClienteDTO clienteDTO) {
        transacciones.consignacion(clienteDTO);
        return ResponseEntity.ok("Consignación realizada exitosamente");
    }

    @PostMapping("/retiro")
    public ResponseEntity<String> retiro(@RequestBody ClienteDTO clienteDTO) {
        transacciones.retiro(clienteDTO);
        return ResponseEntity.ok("Retiro realizado exitosamente");
    }

    @PostMapping("/transferencia")
    public ResponseEntity<String> transferencia(@RequestBody ClienteDTO clienteDTO) {
        transacciones.transferencia(clienteDTO);
        return ResponseEntity.ok("Transferencia realizada exitosamente");
    }
}