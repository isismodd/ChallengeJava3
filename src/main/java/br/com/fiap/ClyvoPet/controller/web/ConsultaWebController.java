package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.entity.Consulta;
import br.com.fiap.ClyvoPet.service.AnimalService;
import br.com.fiap.ClyvoPet.service.ConsultaService;
import br.com.fiap.ClyvoPet.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/consultas")
public class ConsultaWebController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("consultas", consultaService.listarTodas());
        return "consultas/lista";
    }

    @GetMapping("/nova")
    public String showFormCadastro(Model model) {
        model.addAttribute("consulta", new Consulta());
        model.addAttribute("animais", animalService.listarTodos());
        model.addAttribute("veterinarios", veterinarioService.listarTodos());
        return "consultas/formulario";
    }

    @PostMapping
    public String salvar(@ModelAttribute Consulta consulta) {
        consultaService.salvar(consulta);
        return "redirect:/web/consultas";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("consulta", consultaService.buscarPorId(id));
        return "consultas/detalhes";
    }

    @GetMapping("/{id}/editar")
    public String showFormEditar(@PathVariable Long id, Model model) {
        model.addAttribute("consulta", consultaService.buscarPorId(id));
        model.addAttribute("animais", animalService.listarTodos());
        model.addAttribute("veterinarios", veterinarioService.listarTodos());
        return "consultas/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Consulta consulta) {
        consultaService.atualizar(id, consulta);
        return "redirect:/web/consultas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id) {
        consultaService.cancelar(id);
        return "redirect:/web/consultas";
    }

    @PostMapping("/{id}/finalizar")
    public String finalizar(@PathVariable Long id) {
        consultaService.finalizar(id);
        return "redirect:/web/consultas";
    }
}