package br.com.fiap.ClyvoPet.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LembreteRequestDTO {

    @NotNull(message = "ID da consulta é obrigatório")
    private Long consultaId;

    private String tutorEmail;
    private String tutorTelefone;

    @NotBlank(message = "Mensagem é obrigatória")
    private String mensagem;

    @NotBlank(message = "Tipo é obrigatório (EMAIL ou SMS)")
    @Pattern(regexp = "^(EMAIL|SMS)$", message = "Tipo deve ser EMAIL ou SMS")
    private String tipo;
}
