package com.example.Prueba_Tecnica.Entity;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {




    public enum TipoCuenta {
        CORRIENTE, AHORROS
    }

    public enum EstadoCuenta {
        ACTIVA, INACTIVA, CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private TipoCuenta tipoCuenta;

    @NotBlank
    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 10)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estado;

    @NotNull
    @DecimalMin(value = "0.00", message = "El saldo no puede ser negativo")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @NotNull
    @Column(name = "exenta_gmf")
    private boolean exentaGMF;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();

        if (this.tipoCuenta == TipoCuenta.AHORROS && this.estado == null) {
            this.estado = EstadoCuenta.ACTIVA;
        }

        if (this.numeroCuenta == null) {
            this.numeroCuenta = generarNumeroCuenta();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
        
        // Validar modificación de cuenta cancelada
        if (this.estado == EstadoCuenta.CANCELADA) {
            throw new IllegalStateException("No se puede modificar una cuenta cancelada");
        }
    }

    private String generarNumeroCuenta() {
        // Prefijo según tipo de cuenta (53 para ahorros, 33 para corriente)
        String prefix = this.tipoCuenta == TipoCuenta.AHORROS ? "53" : "33";
        
        // Genera 8 dígitos aleatorios
        String randomDigits = String.format("%08d", (long) (Math.random() * 100000000L));
        
        // Genera dígito de verificación usando el algoritmo Luhn
        String numeroSinDigito = prefix + randomDigits;
        int digitoVerificacion = calcularDigitoVerificacion(numeroSinDigito);
        
        return numeroSinDigito + digitoVerificacion;
    }

    private int calcularDigitoVerificacion(String numero) {
        int sum = 0;
        boolean alternate = false;
        
        // Recorre el número de derecha a izquierda
        for (int i = numero.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numero.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        
        // Calcula el dígito de verificación
        return (10 - (sum % 10)) % 10;
    }

    public boolean puedeCancelarse() {
        if (this.estado == EstadoCuenta.CANCELADA) {
            return false;
        }
        
        // Verifica si el saldo es efectivamente cero (con un margen de error pequeño)
        BigDecimal epsilon = new BigDecimal("0.00001");
        boolean saldoCero = saldo != null && saldo.abs().compareTo(epsilon) < 0;
        
        // No se puede cancelar si tiene transacciones pendientes
        // Aquí se podría agregar una validación adicional si tienes acceso a las transacciones
        
        return saldoCero;
    }



}
