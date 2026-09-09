package br.com.fiap.ClyvoPet.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "animais")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String especie;

    private String raca;
    private Integer idade;
    private Double peso;

    @Column(length = 1)
    private String sexo;

    @Column(name = "tutor_nome", nullable = false)
    private String tutorNome;

    @Column(name = "tutor_telefone", nullable = false)
    private String tutorTelefone;

    @Column(name = "tutor_email")
    private String tutorEmail;

    @CreationTimestamp
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    private String observacoes;

    private Boolean ativo = true;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consulta> consultas;
}