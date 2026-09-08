package br.com.fiap.ClyvoPet.repository;

import br.com.fiap.ClyvoPet.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByAtivoTrue();
    List<Animal> findByEspecie(String especie);
    List<Animal> findByTutorNomeContainingIgnoreCase(String tutorNome);
}