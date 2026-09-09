package br.com.fiap.ClyvoPet.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
public class AnimalResponseDTO extends RepresentationModel<AnimalResponseDTO> {
    private Long id;
    private String nome;
    private String especie;
    private String raca;
    private Integer idade;
    private Double peso;
    private String sexo;
    private String tutorNome;
    private String tutorTelefone;
    private String tutorEmail;
    private LocalDateTime dataCadastro;
    private String observacoes;
    private Boolean ativo;
}
