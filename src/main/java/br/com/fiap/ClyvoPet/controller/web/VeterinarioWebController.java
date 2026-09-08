package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.entity.Veterinario;
import br.com.fiap.ClyvoPet.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/veterinarios")
public class VeterinarioWebController {

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("veterinarios", veterinarioService.listarTodos());
        return "veterinarios/lista";
    }

    @GetMapping("/novo")
    public String showFormCadastro(Model model) {
        model.addAttribute("veterinario", new Veterinario());
        return "veterinarios/formulario";
    }

    @PostMapping
    public String salvar(@ModelAttribute Veterinario veterinario) {
        veterinarioService.salvar(veterinario);
        return "redirect:/web/veterinarios";
    }

    @GetMapping("/{id}/editar")
    public String showFormEditar(@PathVariable Long id, Model model) {
        model.addAttribute("veterinario", veterinarioService.buscarPorId(id));
        return "veterinarios/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Veterinario veterinario) {
        veterinarioService.atualizar(id, veterinario);
        return "redirect:/web/veterinarios";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id) {
        veterinarioService.deletar(id);
        return "redirect:/web/veterinarios";
    }
}
