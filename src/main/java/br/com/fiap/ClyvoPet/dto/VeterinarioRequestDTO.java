package br.com.fiap.ClyvoPet.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VeterinarioRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 100, message = "E-mail deve ter no máximo 100 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotBlank(message = "CRMV é obrigatório")
    @Size(max = 20, message = "CRMV deve ter no máximo 20 caracteres")
    private String crmv;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @Size(max = 50, message = "Especialidade deve ter no máximo 50 caracteres")
    private String especialidade;
}