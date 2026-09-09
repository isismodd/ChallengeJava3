package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.model.Veterinario;
import br.com.fiap.ClyvoPet.repository.VeterinarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarioService {

    private final VeterinarioRepository repository;

    public VeterinarioService(VeterinarioRepository repository) {
        this.repository = repository;
    }

    public void salvar(Veterinario veterinario) {

        System.out.println(
                "📝 Salvando veterinário: "
                        + veterinario.getEmail()
        );

        String senhaCriptografada = BCrypt.hashpw(
                veterinario.getSenha(),
                BCrypt.gensalt()
        );

        veterinario.setSenha(senhaCriptografada);

        if (veterinario.getRole() == null
                || veterinario.getRole().isEmpty()) {

            veterinario.setRole("VETERINARIO");
        }

        repository.salvar(veterinario);

        System.out.println(
                "✅ Veterinário salvo com sucesso!"
        );
    }

    public Optional<Veterinario> buscarPorEmail(String email) {
        return repository.buscarPorEmail(email);
    }

    public Optional<Veterinario> buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public List<Veterinario> listarTodos() {
        return repository.listarTodos();
    }

    public void atualizar(Veterinario veterinario) {
        repository.atualizar(veterinario);
    }

    public void deletar(Long id) {
        repository.deletar(id);
    }

    public boolean autenticar(String email, String senha) {

        Optional<Veterinario> optional =
                repository.buscarPorEmail(email);

        if (optional.isEmpty()) {
            return false;
        }

        Veterinario veterinario = optional.get();

        return BCrypt.checkpw(
                senha,
                veterinario.getSenha()
        );
    }
}