package br.com.fiap.ClyvoPet.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultaRequestDTO {

    @NotNull(message = "ID do animal é obrigatório")
    private Long animalId;

    @NotNull(message = "ID do veterinário é obrigatório")
    private Long veterinarioId;

    @NotNull(message = "Data e hora são obrigatórias")
    private LocalDateTime dataHora;

    @Size(max = 200, message = "Motivo deve ter no máximo 200 caracteres")
    private String motivo;

    private String diagnostico;
    private String prescricao;

    private String status;
}