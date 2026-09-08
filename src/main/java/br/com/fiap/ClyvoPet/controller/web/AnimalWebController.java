package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.entity.Animal;
import br.com.fiap.ClyvoPet.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/animais")
public class AnimalWebController {

    @Autowired
    private AnimalService animalService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("animais", animalService.listarTodos());
        return "animais/lista";
    }

    @GetMapping("/novo")
    public String showFormCadastro(Model model) {
        model.addAttribute("animal", new Animal());
        return "animais/formulario";
    }

    @PostMapping
    public String salvar(@ModelAttribute Animal animal) {
        animalService.salvar(animal);
        return "redirect:/web/animais";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("animal", animalService.buscarPorId(id));
        return "animais/detalhes";
    }

    @GetMapping("/{id}/editar")
    public String showFormEditar(@PathVariable Long id, Model model) {
        model.addAttribute("animal", animalService.buscarPorId(id));
        return "animais/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Animal animal) {
        animalService.atualizar(id, animal);
        return "redirect:/web/animais";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id) {
        animalService.deletar(id);
        return "redirect:/web/animais";
    }
}
