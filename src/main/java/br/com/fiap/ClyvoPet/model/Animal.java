package br.com.fiap.ClyvoPet.model;

import java.time.LocalDateTime;

public class Animal {
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

    // Construtores
    public Animal() {}

    public Animal(String nome, String especie, String tutorNome, String tutorTelefone) {
        this.nome = nome;
        this.especie = especie;
        this.tutorNome = tutorNome;
        this.tutorTelefone = tutorTelefone;
        this.ativo = true;
        this.dataCadastro = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getTutorNome() { return tutorNome; }
    public void setTutorNome(String tutorNome) { this.tutorNome = tutorNome; }

    public String getTutorTelefone() { return tutorTelefone; }
    public void setTutorTelefone(String tutorTelefone) { this.tutorTelefone = tutorTelefone; }

    public String getTutorEmail() { return tutorEmail; }
    public void setTutorEmail(String tutorEmail) { this.tutorEmail = tutorEmail; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", especie='" + especie + '\'' +
                ", tutor='" + tutorNome + '\'' +
                '}';
    }
}