package br.com.fiap.ClyvoPet.assembler;

import br.com.fiap.ClyvoPet.controller.api.LembreteRestController;
import br.com.fiap.ClyvoPet.dto.LembreteResponseDTO;
import br.com.fiap.ClyvoPet.dto.ConsultaResponseDTO;
import br.com.fiap.ClyvoPet.entity.Lembrete;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LembreteModelAssembler implements RepresentationModelAssembler<Lembrete, EntityModel<LembreteResponseDTO>> {

    @Override
    public EntityModel<LembreteResponseDTO> toModel(Lembrete lembrete) {
        LembreteResponseDTO dto = new LembreteResponseDTO();
        dto.setId(lembrete.getId());

        ConsultaResponseDTO consultaDTO = new ConsultaResponseDTO();
        consultaDTO.setId(lembrete.getConsulta().getId());
        dto.setConsulta(consultaDTO);

        dto.setTutorEmail(lembrete.getTutorEmail());
        dto.setTutorTelefone(lembrete.getTutorTelefone());
        dto.setMensagem(lembrete.getMensagem());
        dto.setDataEnvio(lembrete.getDataEnvio());
        dto.setEnviado(lembrete.getEnviado());
        dto.setTipo(lembrete.getTipo());

        return EntityModel.of(dto,
                linkTo(methodOn(LembreteRestController.class).buscarPorId(lembrete.getId())).withSelfRel(),
                linkTo(methodOn(LembreteRestController.class).listarTodos()).withRel("lembretes")
        );
    }
}