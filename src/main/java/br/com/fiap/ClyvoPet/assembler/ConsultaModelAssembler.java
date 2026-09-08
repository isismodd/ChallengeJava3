package br.com.fiap.ClyvoPet.assembler;

import br.com.fiap.ClyvoPet.controller.api.ConsultaRestController;
import br.com.fiap.ClyvoPet.dto.ConsultaResponseDTO;
import br.com.fiap.ClyvoPet.dto.AnimalResponseDTO;
import br.com.fiap.ClyvoPet.dto.VeterinarioResponseDTO;
import br.com.fiap.ClyvoPet.entity.Consulta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConsultaModelAssembler implements RepresentationModelAssembler<Consulta, EntityModel<ConsultaResponseDTO>> {

    @Override
    public EntityModel<ConsultaResponseDTO> toModel(Consulta consulta) {
        ConsultaResponseDTO dto = new ConsultaResponseDTO();
        dto.setId(consulta.getId());

        AnimalResponseDTO animalDTO = new AnimalResponseDTO();
        animalDTO.setId(consulta.getAnimal().getId());
        animalDTO.setNome(consulta.getAnimal().getNome());
        animalDTO.setEspecie(consulta.getAnimal().getEspecie());
        dto.setAnimal(animalDTO);

        VeterinarioResponseDTO vetDTO = new VeterinarioResponseDTO();
        vetDTO.setId(consulta.getVeterinario().getId());
        vetDTO.setNome(consulta.getVeterinario().getNome());
        vetDTO.setEspecialidade(consulta.getVeterinario().getEspecialidade());
        dto.setVeterinario(vetDTO);

        dto.setDataHora(consulta.getDataHora());
        dto.setMotivo(consulta.getMotivo());
        dto.setDiagnostico(consulta.getDiagnostico());
        dto.setPrescricao(consulta.getPrescricao());
        dto.setStatus(consulta.getStatus());
        dto.setLembreteEnviado(consulta.getLembreteEnviado());

        return EntityModel.of(dto,
                linkTo(methodOn(ConsultaRestController.class).buscarPorId(consulta.getId())).withSelfRel(),
                linkTo(methodOn(ConsultaRestController.class).listarTodas()).withRel("consultas")
        );
    }
}