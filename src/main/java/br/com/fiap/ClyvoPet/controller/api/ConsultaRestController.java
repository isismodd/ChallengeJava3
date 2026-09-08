package br.com.fiap.ClyvoPet.controller.api;

import br.com.fiap.ClyvoPet.assembler.ConsultaModelAssembler;
import br.com.fiap.ClyvoPet.dto.ConsultaRequestDTO;
import br.com.fiap.ClyvoPet.dto.ConsultaResponseDTO;
import br.com.fiap.ClyvoPet.entity.Consulta;
import br.com.fiap.ClyvoPet.service.ConsultaService;
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
@RequestMapping("/api/consultas")
public class ConsultaRestController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ConsultaModelAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<ConsultaResponseDTO>> criar(@RequestBody @Valid ConsultaRequestDTO request) {
        Consulta consulta = consultaService.agendar(
                request.getAnimalId(),
                request.getVeterinarioId(),
                request.getDataHora(),
                request.getMotivo()
        );
        EntityModel<ConsultaResponseDTO> model = assembler.toModel(consulta);
        return ResponseEntity.created(model.getRequiredLink("self").toUri()).body(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ConsultaResponseDTO>>> listarTodas() {
        List<EntityModel<ConsultaResponseDTO>> consultas = consultaService.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ConsultaResponseDTO>> collection = CollectionModel.of(consultas,
                linkTo(methodOn(ConsultaRestController.class).listarTodas()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ConsultaResponseDTO>> buscarPorId(@PathVariable Long id) {
        Consulta consulta = consultaService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(consulta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ConsultaResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid ConsultaRequestDTO request) {
        Consulta consulta = new Consulta();
        consulta.setDataHora(request.getDataHora());
        consulta.setMotivo(request.getMotivo());
        consulta.setDiagnostico(request.getDiagnostico());
        consulta.setPrescricao(request.getPrescricao());
        consulta.setStatus(request.getStatus());

        Consulta updated = consultaService.atualizar(id, consulta);
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<EntityModel<ConsultaResponseDTO>> cancelar(@PathVariable Long id) {
        consultaService.cancelar(id);
        Consulta consulta = consultaService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(consulta));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<EntityModel<ConsultaResponseDTO>> finalizar(@PathVariable Long id) {
        consultaService.finalizar(id);
        Consulta consulta = consultaService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(consulta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        consultaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}