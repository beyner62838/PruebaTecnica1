package com.example.Prueba_Tecnica.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private Long id;
    @NotBlank private String nombres;
    @NotBlank private String apellidos;
    @NotBlank private String numeroIdentificacion;
    @Email private String correoElectronico;
    @PositiveOrZero 
    private BigDecimal saldoTotal;
    
    @PositiveOrZero 
    private Integer numeroTransacciones;

    private List<TransaccionResumidaDTO> ultimasTransacciones;

    // Campos para operaciones
    @Positive 
    private Long cuentaId;
    
    @Positive 
    @DecimalMin(value = "0.01") 
    private BigDecimal monto;
    
    @Positive 
    private Long cuentaDestinoId;
    
    @Positive 
    private Long cuentaOrigenId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransaccionResumidaDTO {
        private LocalDate fecha;
        private String tipo;
        private BigDecimal monto;
        private String descripcion;
    }
}
