package com.raquitich.talleres.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TallerResponseDTO {

    private Long idTaller;
    private String nombreTaller;
    private String descripcionTaller;
    private Integer cuposTaller;
}
