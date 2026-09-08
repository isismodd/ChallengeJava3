package br.com.fiap.ClyvoPet.repository;

import br.com.fiap.ClyvoPet.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    Optional<Veterinario> findByEmail(String email);
    Optional<Veterinario> findByCrmv(String crmv);
}