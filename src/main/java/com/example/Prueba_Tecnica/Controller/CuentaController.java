package com.example.Prueba_Tecnica.Controller;

import com.example.Prueba_Tecnica.Entity.Cuenta;
import com.example.Prueba_Tecnica.IService.ICuentaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/cuenta")
public class CuentaController {

    @Autowired
    private ICuentaService cuentaService;

    @GetMapping()
    public ResponseEntity<List<Cuenta>> findAll() {
        return ResponseEntity.ok(cuentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> findById(@PathVariable Long id) {
        return cuentaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Cuenta> save(@Valid @RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(cuentaService.save(cuenta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> update(@PathVariable Long id, @Valid @RequestBody Cuenta cuentadetalles) {
        return ResponseEntity.ok(cuentaService.update(cuentadetalles, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activar/{id}")
    public ResponseEntity<String> activateCuenta(@PathVariable Long id) {
        try {
            cuentaService.activateCuenta(id);
            return ResponseEntity.ok("Cuenta activada exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada");
        }
    }

    @PutMapping("/desactivar/{id}")
    public ResponseEntity<String> deactivateCuenta(@PathVariable Long id) {
        try {
            cuentaService.deactivateCuenta(id);
            return ResponseEntity.ok("Cuenta desactivada exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada");
        }
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelateCuenta(@PathVariable Long id) {
        try {
            cuentaService.cancelateCuenta(id);
            return ResponseEntity.ok("Cuenta cancelada exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
