package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.entity.Animal;
import br.com.fiap.ClyvoPet.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    public List<Animal> listarTodos() {
        return animalRepository.findByAtivoTrue();
    }

    public Animal buscarPorId(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com ID: " + id));
    }

    @Transactional
    public Animal salvar(Animal animal) {
        return animalRepository.save(animal);
    }

    @Transactional
    public Animal atualizar(Long id, Animal animalAtualizado) {
        Animal animal = buscarPorId(id);
        animal.setNome(animalAtualizado.getNome());
        animal.setEspecie(animalAtualizado.getEspecie());
        animal.setRaca(animalAtualizado.getRaca());
        animal.setIdade(animalAtualizado.getIdade());
        animal.setPeso(animalAtualizado.getPeso());
        animal.setSexo(animalAtualizado.getSexo());
        animal.setTutorNome(animalAtualizado.getTutorNome());
        animal.setTutorTelefone(animalAtualizado.getTutorTelefone());
        animal.setTutorEmail(animalAtualizado.getTutorEmail());
        animal.setObservacoes(animalAtualizado.getObservacoes());
        return animalRepository.save(animal);
    }

    @Transactional
    public void deletar(Long id) {
        Animal animal = buscarPorId(id);
        animal.setAtivo(false);
        animalRepository.save(animal);
    }

    public List<Animal> buscarPorEspecie(String especie) {
        return animalRepository.findByEspecie(especie);
    }

    public List<Animal> buscarPorTutor(String tutorNome) {
        return animalRepository.findByTutorNomeContainingIgnoreCase(tutorNome);
    }
}
