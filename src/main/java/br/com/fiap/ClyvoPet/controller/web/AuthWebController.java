package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.entity.Veterinario;
import br.com.fiap.ClyvoPet.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthWebController {

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping("/registrar")
    public String showRegistro(Model model) {
        model.addAttribute("veterinario", new Veterinario());
        return "registrar";
    }

    @PostMapping("/registrar")
    public String registrar(Veterinario veterinario, RedirectAttributes redirectAttributes) {
        try {
            // Verifica se o email já existe
            try {
                veterinarioService.buscarPorEmail(veterinario.getEmail());
                redirectAttributes.addAttribute("error", true);
                return "redirect:/registrar?error";
            } catch (RuntimeException e) {
                // Email não encontrado, pode prosseguir
            }

            veterinarioService.salvar(veterinario);
            redirectAttributes.addAttribute("sucesso", true);
            return "redirect:/login?sucesso";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/registrar?error";
        }
    }
}