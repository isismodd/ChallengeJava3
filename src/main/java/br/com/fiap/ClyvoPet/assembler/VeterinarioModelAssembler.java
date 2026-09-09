package br.com.fiap.ClyvoPet.assembler;

import br.com.fiap.ClyvoPet.controller.api.VeterinarioRestController;
import br.com.fiap.ClyvoPet.dto.VeterinarioResponseDTO;
import br.com.fiap.ClyvoPet.entity.Veterinario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VeterinarioModelAssembler implements RepresentationModelAssembler<Veterinario, EntityModel<VeterinarioResponseDTO>> {

    @Override
    public EntityModel<VeterinarioResponseDTO> toModel(Veterinario veterinario) {
        VeterinarioResponseDTO dto = new VeterinarioResponseDTO();
        dto.setId(veterinario.getId());
        dto.setNome(veterinario.getNome());
        dto.setEmail(veterinario.getEmail());
        dto.setCrmv(veterinario.getCrmv());
        dto.setTelefone(veterinario.getTelefone());
        dto.setEspecialidade(veterinario.getEspecialidade());
        dto.setDataCadastro(veterinario.getDataCadastro());
        dto.setAtivo(veterinario.getAtivo());

        return EntityModel.of(dto,
                linkTo(methodOn(VeterinarioRestController.class).buscarPorId(veterinario.getId())).withSelfRel(),
                linkTo(methodOn(VeterinarioRestController.class).listarTodos()).withRel("veterinarios")
        );
    }
}