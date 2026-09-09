package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.model.Veterinario;
import br.com.fiap.ClyvoPet.service.VeterinarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/veterinarios")
public class VeterinarioWebController {

    private final VeterinarioService veterinarioService;

    public VeterinarioWebController(
            VeterinarioService veterinarioService) {

        this.veterinarioService = veterinarioService;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "veterinarios",
                veterinarioService.listarTodos()
        );

        return "veterinarios/lista";
    }

    @GetMapping("/novo")
    public String showFormCadastro(Model model) {

        model.addAttribute(
                "veterinario",
                new Veterinario()
        );

        return "veterinarios/formulario";
    }

    @PostMapping
    public String salvar(
            @ModelAttribute Veterinario veterinario) {

        veterinarioService.salvar(veterinario);

        return "redirect:/web/veterinarios";
    }

    @GetMapping("/{id}/editar")
    public String showFormEditar(
            @PathVariable Long id,
            Model model) {

        Veterinario veterinario =
                veterinarioService.buscarPorId(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Veterinário não encontrado com ID: " + id
                                )
                        );

        model.addAttribute(
                "veterinario",
                veterinario
        );

        return "veterinarios/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Veterinario veterinario) {

        veterinario.setId(id);

        veterinarioService.atualizar(veterinario);

        return "redirect:/web/veterinarios";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id) {

        veterinarioService.deletar(id);

        return "redirect:/web/veterinarios";
    }
}