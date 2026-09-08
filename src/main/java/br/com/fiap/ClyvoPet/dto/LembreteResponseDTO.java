package br.com.fiap.ClyvoPet.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
public class LembreteResponseDTO extends RepresentationModel<LembreteResponseDTO> {
    private Long id;
    private ConsultaResponseDTO consulta;
    private String tutorEmail;
    private String tutorTelefone;
    private String mensagem;
    private LocalDateTime dataEnvio;
    private Boolean enviado;
    private String tipo;
}