package br.com.fiap.ClyvoPet.repository;

import br.com.fiap.ClyvoPet.entity.Consulta;
import br.com.fiap.ClyvoPet.entity.Lembrete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LembreteRepository extends JpaRepository<Lembrete, Long> {
    List<Lembrete> findByConsulta(Consulta consulta);
    List<Lembrete> findByEnviadoFalse();
    long countByEnviadoFalse();
}