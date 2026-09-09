package br.com.fiap.ClyvoPet.assembler;

import br.com.fiap.ClyvoPet.controller.api.AnimalRestController;
import br.com.fiap.ClyvoPet.dto.AnimalResponseDTO;
import br.com.fiap.ClyvoPet.entity.Animal;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnimalModelAssembler implements RepresentationModelAssembler<Animal, EntityModel<AnimalResponseDTO>> {

    @Override
    public EntityModel<AnimalResponseDTO> toModel(Animal animal) {
        AnimalResponseDTO dto = new AnimalResponseDTO();
        dto.setId(animal.getId());
        dto.setNome(animal.getNome());
        dto.setEspecie(animal.getEspecie());
        dto.setRaca(animal.getRaca());
        dto.setIdade(animal.getIdade());
        dto.setPeso(animal.getPeso());
        dto.setSexo(animal.getSexo());
        dto.setTutorNome(animal.getTutorNome());
        dto.setTutorTelefone(animal.getTutorTelefone());
        dto.setTutorEmail(animal.getTutorEmail());
        dto.setDataCadastro(animal.getDataCadastro());
        dto.setObservacoes(animal.getObservacoes());
        dto.setAtivo(animal.getAtivo());

        return EntityModel.of(dto,
                linkTo(methodOn(AnimalRestController.class).buscarPorId(animal.getId())).withSelfRel(),
                linkTo(methodOn(AnimalRestController.class).listarTodos()).withRel("animais")
        );
    }
}