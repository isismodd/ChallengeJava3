package br.com.fiap.ClyvoPet.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
public class VeterinarioResponseDTO extends RepresentationModel<VeterinarioResponseDTO> {
    private Long id;
    private String nome;
    private String email;
    private String crmv;
    private String telefone;
    private String especialidade;
    private LocalDateTime dataCadastro;
    private Boolean ativo;
}

