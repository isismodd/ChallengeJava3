package br.com.fiap.ClyvoPet.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AnimalRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "Espécie é obrigatória")
    @Size(max = 50, message = "Espécie deve ter no máximo 50 caracteres")
    private String especie;

    @Size(max = 50, message = "Raça deve ter no máximo 50 caracteres")
    private String raca;

    @Min(value = 0, message = "Idade deve ser maior que 0")
    private Integer idade;

    @DecimalMin(value = "0.0", inclusive = false, message = "Peso deve ser maior que 0")
    private Double peso;

    @Pattern(regexp = "^[MF]$", message = "Sexo deve ser 'M' ou 'F'")
    private String sexo;

    @NotBlank(message = "Nome do tutor é obrigatório")
    @Size(max = 100, message = "Nome do tutor deve ter no máximo 100 caracteres")
    private String tutorNome;

    @NotBlank(message = "Telefone do tutor é obrigatório")
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String tutorTelefone;

    @Email(message = "E-mail inválido")
    @Size(max = 100, message = "E-mail deve ter no máximo 100 caracteres")
    private String tutorEmail;

    private String observacoes;
}