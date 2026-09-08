package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.entity.Veterinario;
import br.com.fiap.ClyvoPet.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VeterinarioService {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Veterinario> listarTodos() {
        return veterinarioRepository.findAll();
    }

    public Veterinario buscarPorId(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinário não encontrado com ID: " + id));
    }

    public Veterinario buscarPorEmail(String email) {
        return veterinarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Veterinário não encontrado com email: " + email));
    }

    @Transactional
    public Veterinario salvar(Veterinario veterinario) {
        veterinario.setSenha(passwordEncoder.encode(veterinario.getSenha()));
        return veterinarioRepository.save(veterinario);
    }

    @Transactional
    public Veterinario atualizar(Long id, Veterinario veterinarioAtualizado) {
        Veterinario veterinario = buscarPorId(id);
        veterinario.setNome(veterinarioAtualizado.getNome());
        veterinario.setTelefone(veterinarioAtualizado.getTelefone());
        veterinario.setEspecialidade(veterinarioAtualizado.getEspecialidade());
        return veterinarioRepository.save(veterinario);
    }

    @Transactional
    public void deletar(Long id) {
        Veterinario veterinario = buscarPorId(id);
        veterinario.setAtivo(false);
        veterinarioRepository.save(veterinario);
    }
}