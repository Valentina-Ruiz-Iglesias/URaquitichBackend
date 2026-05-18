package com.raquitich.talleres.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TallerRequestDTO {

    @NotBlank(message = "El nombre del taller es obligatorio")
    private String nombreTaller;

    private String descripcionTaller;

    @NotNull(message = "Los cupos son obligatorios")
    @Min(value = 0, message = "Los cupos no pueden ser negativos")
    private Integer cuposTaller;
}
