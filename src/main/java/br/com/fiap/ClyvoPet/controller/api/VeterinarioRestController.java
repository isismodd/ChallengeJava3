package br.com.fiap.ClyvoPet.controller.api;

import br.com.fiap.ClyvoPet.assembler.VeterinarioModelAssembler;
import br.com.fiap.ClyvoPet.dto.VeterinarioRequestDTO;
import br.com.fiap.ClyvoPet.dto.VeterinarioResponseDTO;
import br.com.fiap.ClyvoPet.entity.Veterinario;
import br.com.fiap.ClyvoPet.service.VeterinarioService;
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
@RequestMapping("/api/veterinarios")
public class VeterinarioRestController {

    @Autowired
    private VeterinarioService veterinarioService;

    @Autowired
    private VeterinarioModelAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<VeterinarioResponseDTO>> criar(@RequestBody @Valid VeterinarioRequestDTO request) {
        Veterinario veterinario = new Veterinario();
        veterinario.setNome(request.getNome());
        veterinario.setEmail(request.getEmail());
        veterinario.setSenha(request.getSenha());
        veterinario.setCrmv(request.getCrmv());
        veterinario.setTelefone(request.getTelefone());
        veterinario.setEspecialidade(request.getEspecialidade());

        Veterinario saved = veterinarioService.salvar(veterinario);
        EntityModel<VeterinarioResponseDTO> model = assembler.toModel(saved);
        return ResponseEntity.created(model.getRequiredLink("self").toUri()).body(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<VeterinarioResponseDTO>>> listarTodos() {
        List<EntityModel<VeterinarioResponseDTO>> veterinarios = veterinarioService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<VeterinarioResponseDTO>> collection = CollectionModel.of(veterinarios,
                linkTo(methodOn(VeterinarioRestController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<VeterinarioResponseDTO>> buscarPorId(@PathVariable Long id) {
        Veterinario veterinario = veterinarioService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(veterinario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<VeterinarioResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid VeterinarioRequestDTO request) {
        Veterinario veterinario = new Veterinario();
        veterinario.setNome(request.getNome());
        veterinario.setTelefone(request.getTelefone());
        veterinario.setEspecialidade(request.getEspecialidade());

        Veterinario updated = veterinarioService.atualizar(id, veterinario);
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        veterinarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}