package com.example.Prueba_Tecnica.Entity;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de identificación es obligatorio")
    @Column(name = "tipo_identificacion", nullable = false)
    private String tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Column(name = "numero_identificacion", nullable = false, unique = true)
    private String numeroIdentificacion;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, message = "Los nombres deben tener al menos 2 caracteres")
    @Column(nullable = false)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, message = "Los apellidos deben tener al menos 2 caracteres")
    @Column(nullable = false)
    private String apellidos;

    @Email(message = "El correo electrónico debe ser válido")
    @Column(name = "correo_electronico", nullable = false, unique = true)
    private String correoElectronico;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuenta> cuentas;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

}