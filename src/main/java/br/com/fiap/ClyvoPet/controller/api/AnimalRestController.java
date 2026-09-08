package br.com.fiap.ClyvoPet.controller.api;

import br.com.fiap.ClyvoPet.assembler.AnimalModelAssembler;
import br.com.fiap.ClyvoPet.dto.AnimalRequestDTO;
import br.com.fiap.ClyvoPet.dto.AnimalResponseDTO;
import br.com.fiap.ClyvoPet.entity.Animal;
import br.com.fiap.ClyvoPet.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/animais")
public class AnimalRestController {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private AnimalModelAssembler assembler;

    private Animal toEntity(AnimalRequestDTO dto) {
        Animal animal = new Animal();
        animal.setNome(dto.getNome());
        animal.setEspecie(dto.getEspecie());
        animal.setRaca(dto.getRaca());
        animal.setIdade(dto.getIdade());
        animal.setPeso(dto.getPeso());
        animal.setSexo(dto.getSexo());
        animal.setTutorNome(dto.getTutorNome());
        animal.setTutorTelefone(dto.getTutorTelefone());
        animal.setTutorEmail(dto.getTutorEmail());
        animal.setObservacoes(dto.getObservacoes());
        return animal;
    }

    @PostMapping
    public ResponseEntity<EntityModel<AnimalResponseDTO>> criar(@RequestBody @Valid AnimalRequestDTO request) {
        Animal animal = toEntity(request);
        Animal saved = animalService.salvar(animal);
        EntityModel<AnimalResponseDTO> model = assembler.toModel(saved);
        return ResponseEntity.created(model.getRequiredLink("self").toUri()).body(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<AnimalResponseDTO>>> listarTodos() {
        List<EntityModel<AnimalResponseDTO>> animais = animalService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<AnimalResponseDTO>> collection = CollectionModel.of(animais,
                linkTo(methodOn(AnimalRestController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AnimalResponseDTO>> buscarPorId(@PathVariable Long id) {
        Animal animal = animalService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(animal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<AnimalResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid AnimalRequestDTO request) {
        Animal animal = toEntity(request);
        Animal updated = animalService.atualizar(id, animal);
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        animalService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}