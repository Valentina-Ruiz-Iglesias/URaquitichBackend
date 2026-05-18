package com.raquitich.talleres.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "talleres_extracurriculares")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TallerExtracurricular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taller")
    private Long idTaller;

    @NotBlank(message = "El nombre del taller es obligatorio")
    @Column(name = "nombre_taller", nullable = false)
    private String nombreTaller;

    @Column(name = "descripcion_taller")
    private String descripcionTaller;

    @NotNull(message = "Los cupos son obligatorios")
    @Min(value = 0, message = "Los cupos no pueden ser negativos")
    @Column(name = "cupos_taller", nullable = false)
    private Integer cuposTaller;
}
