package br.com.fiap.ClyvoPet.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
public class ConsultaResponseDTO extends RepresentationModel<ConsultaResponseDTO> {
    private Long id;
    private AnimalResponseDTO animal;
    private VeterinarioResponseDTO veterinario;
    private LocalDateTime dataHora;
    private String motivo;
    private String diagnostico;
    private String prescricao;
    private String status;
    private Boolean lembreteEnviado;
}