package br.com.fiap.ClyvoPet.controller.api;

import br.com.fiap.ClyvoPet.assembler.LembreteModelAssembler;
import br.com.fiap.ClyvoPet.dto.LembreteResponseDTO;
import br.com.fiap.ClyvoPet.entity.Lembrete;
import br.com.fiap.ClyvoPet.service.LembreteService;
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
@RequestMapping("/api/lembretes")
public class LembreteRestController {

    @Autowired
    private LembreteService lembreteService;

    @Autowired
    private LembreteModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<LembreteResponseDTO>>> listarTodos() {
        List<EntityModel<LembreteResponseDTO>> lembretes = lembreteService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<LembreteResponseDTO>> collection = CollectionModel.of(lembretes,
                linkTo(methodOn(LembreteRestController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<LembreteResponseDTO>> buscarPorId(@PathVariable Long id) {
        Lembrete lembrete = lembreteService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(lembrete));
    }

    @GetMapping("/pendentes")
    public ResponseEntity<CollectionModel<EntityModel<LembreteResponseDTO>>> listarPendentes() {
        List<EntityModel<LembreteResponseDTO>> pendentes = lembreteService.buscarPendentes().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<LembreteResponseDTO>> collection = CollectionModel.of(pendentes,
                linkTo(methodOn(LembreteRestController.class).listarPendentes()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @PostMapping("/enviar/{consultaId}")
    public ResponseEntity<EntityModel<LembreteResponseDTO>> enviarLembrete(@PathVariable Long consultaId) {
        Lembrete lembrete = lembreteService.criarEEnviarLembrete(consultaId);
        EntityModel<LembreteResponseDTO> model = assembler.toModel(lembrete);
        return ResponseEntity.created(model.getRequiredLink("self").toUri()).body(model);
    }

    @PostMapping("/enviar-todos")
    public ResponseEntity<String> enviarTodosPendentes() {
        lembreteService.enviarTodosLembretesPendentes();
        return ResponseEntity.ok("Lembretes enviados com sucesso!");
    }
}