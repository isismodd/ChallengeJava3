package br.com.fiap.ClyvoPet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "lembretes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lembrete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(name = "tutor_email")
    private String tutorEmail;

    @Column(name = "tutor_telefone")
    private String tutorTelefone;

    @Column(length = 500)
    private String mensagem;

    @CreationTimestamp
    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    private Boolean enviado = false;

    @Column(length = 10)
    private String tipo;
}