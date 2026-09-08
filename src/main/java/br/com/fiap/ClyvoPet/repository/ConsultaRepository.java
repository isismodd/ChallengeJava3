package br.com.fiap.ClyvoPet.repository;

import br.com.fiap.ClyvoPet.entity.Animal;
import br.com.fiap.ClyvoPet.entity.Consulta;
import br.com.fiap.ClyvoPet.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByAnimal(Animal animal);

    List<Consulta> findByVeterinario(Veterinario veterinario);

    List<Consulta> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Consulta> findByStatus(String status);

    List<Consulta> findByLembreteEnviadoFalseAndDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Consulta> findByLembreteEnviadoFalseAndDataHoraBefore(LocalDateTime dataHora);
}