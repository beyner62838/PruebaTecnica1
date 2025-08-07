package com.example.Prueba_Tecnica.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import com.example.Prueba_Tecnica.Entity.Cliente;
import com.example.Prueba_Tecnica.IService.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping()
    public ResponseEntity<List<Cliente>> findAll() {
        List<Cliente> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Cliente> save (@Valid @RequestBody Cliente cliente) {
        Cliente newCliente = clienteService.save(cliente);
        return ResponseEntity.ok(newCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        Cliente updatedCliente = clienteService.update(cliente, id);
        return ResponseEntity.ok(updatedCliente);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
