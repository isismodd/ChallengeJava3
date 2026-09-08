package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.entity.Animal;
import br.com.fiap.ClyvoPet.entity.Consulta;
import br.com.fiap.ClyvoPet.entity.Veterinario;
import br.com.fiap.ClyvoPet.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private VeterinarioService veterinarioService;

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com ID: " + id));
    }

    @Transactional
    public Consulta salvar(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta agendar(Long animalId, Long veterinarioId, LocalDateTime dataHora, String motivo) {
        Animal animal = animalService.buscarPorId(animalId);
        Veterinario veterinario = veterinarioService.buscarPorId(veterinarioId);

        Consulta consulta = new Consulta();
        consulta.setAnimal(animal);
        consulta.setVeterinario(veterinario);
        consulta.setDataHora(dataHora);
        consulta.setMotivo(motivo);
        consulta.setStatus("AGENDADA");

        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta atualizar(Long id, Consulta consultaAtualizada) {
        Consulta consulta = buscarPorId(id);
        consulta.setDataHora(consultaAtualizada.getDataHora());
        consulta.setMotivo(consultaAtualizada.getMotivo());
        consulta.setDiagnostico(consultaAtualizada.getDiagnostico());
        consulta.setPrescricao(consultaAtualizada.getPrescricao());
        consulta.setStatus(consultaAtualizada.getStatus());
        return consultaRepository.save(consulta);
    }

    @Transactional
    public void cancelar(Long id) {
        Consulta consulta = buscarPorId(id);
        consulta.setStatus("CANCELADA");
        consultaRepository.save(consulta);
    }

    @Transactional
    public void finalizar(Long id) {
        Consulta consulta = buscarPorId(id);
        consulta.setStatus("REALIZADA");
        consultaRepository.save(consulta);
    }

    public List<Consulta> buscarPorAnimal(Long animalId) {
        Animal animal = animalService.buscarPorId(animalId);
        return consultaRepository.findByAnimal(animal);
    }

    public List<Consulta> buscarPorVeterinario(Long veterinarioId) {
        Veterinario veterinario = veterinarioService.buscarPorId(veterinarioId);
        return consultaRepository.findByVeterinario(veterinario);
    }

    public List<Consulta> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.findByDataHoraBetween(inicio, fim);
    }

    public List<Consulta> buscarConsultasPendentesLembrete() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proximas24Horas = agora.plusHours(24);
        return consultaRepository.findByLembreteEnviadoFalseAndDataHoraBetween(agora, proximas24Horas);
    }
}
