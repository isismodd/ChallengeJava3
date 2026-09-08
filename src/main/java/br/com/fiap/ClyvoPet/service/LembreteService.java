package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.entity.Consulta;
import br.com.fiap.ClyvoPet.entity.Lembrete;
import br.com.fiap.ClyvoPet.repository.LembreteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LembreteService {

    private static final Logger logger = LoggerFactory.getLogger(LembreteService.class);

    @Autowired
    private LembreteRepository lembreteRepository;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private EmailService emailService;

    /**
     * Lista todos os lembretes
     */
    public List<Lembrete> listarTodos() {
        return lembreteRepository.findAll();
    }

    /**
     * Busca um lembrete por ID
     */
    public Lembrete buscarPorId(Long id) {
        return lembreteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lembrete não encontrado com ID: " + id));
    }

    /**
     * Busca lembretes pendentes (não enviados)
     */
    public List<Lembrete> buscarPendentes() {
        return lembreteRepository.findByEnviadoFalse();
    }

    /**
     * Busca lembretes por consulta
     */
    public List<Lembrete> buscarPorConsulta(Long consultaId) {
        Consulta consulta = consultaService.buscarPorId(consultaId);
        return lembreteRepository.findByConsulta(consulta);
    }

    /**
     * Cria um lembrete para uma consulta
     */
    @Transactional
    public Lembrete criarLembrete(Consulta consulta) {
        Lembrete lembrete = new Lembrete();
        lembrete.setConsulta(consulta);
        lembrete.setTutorEmail(consulta.getAnimal().getTutorEmail());
        lembrete.setTutorTelefone(consulta.getAnimal().getTutorTelefone());
        lembrete.setMensagem(montarMensagem(consulta));
        lembrete.setTipo("EMAIL");
        lembrete.setEnviado(false);
        return lembreteRepository.save(lembrete);
    }

    /**
     * Envia um lembrete existente
     */
    @Transactional
    public void enviarLembrete(Long lembreteId) {
        Lembrete lembrete = buscarPorId(lembreteId);

        if (lembrete.getEnviado()) {
            logger.warn("⚠️ Lembrete {} já foi enviado anteriormente", lembreteId);
            throw new RuntimeException("Este lembrete já foi enviado");
        }

        try {
            // Verifica se o tutor tem e-mail cadastrado
            String tutorEmail = lembrete.getTutorEmail();
            if (tutorEmail == null || tutorEmail.isEmpty()) {
                logger.warn("⚠️ Tutor não possui e-mail cadastrado. Lembrete {} não enviado.", lembreteId);
                throw new RuntimeException("Tutor não possui e-mail cadastrado");
            }

            // Envia o e-mail usando o serviço completo
            logger.info("📧 Enviando lembrete {} para: {}", lembreteId, tutorEmail);
            emailService.enviarLembreteConsulta(lembrete.getConsulta());

            // Marca como enviado
            lembrete.setEnviado(true);
            lembrete.setDataEnvio(LocalDateTime.now());
            lembreteRepository.save(lembrete);

            // Marca a consulta como tendo lembrete enviado
            Consulta consulta = lembrete.getConsulta();
            consulta.setLembreteEnviado(true);
            consultaService.salvar(consulta);

            logger.info("✅ Lembrete {} enviado com sucesso para {}", lembreteId, tutorEmail);

        } catch (Exception e) {
            logger.error("❌ Erro ao enviar lembrete {}: {}", lembreteId, e.getMessage());
            throw new RuntimeException("Erro ao enviar lembrete: " + e.getMessage());
        }
    }

    /**
     * Cria e envia um lembrete para uma consulta
     */
    @Transactional
    public Lembrete criarEEnviarLembrete(Long consultaId) {
        logger.info("📝 Criando lembrete para consulta: {}", consultaId);
        Consulta consulta = consultaService.buscarPorId(consultaId);

        // Verifica se já existe lembrete enviado para esta consulta
        List<Lembrete> existentes = lembreteRepository.findByConsulta(consulta);
        for (Lembrete l : existentes) {
            if (l.getEnviado()) {
                logger.warn("⚠️ Consulta {} já possui lembrete enviado (ID: {})", consultaId, l.getId());
                throw new RuntimeException("Esta consulta já possui um lembrete enviado");
            }
        }

        Lembrete lembrete = criarLembrete(consulta);
        enviarLembrete(lembrete.getId());
        return lembrete;
    }

    /**
     * Envia todos os lembretes pendentes
     */
    @Transactional
    public void enviarTodosLembretesPendentes() {
        List<Lembrete> pendentes = buscarPendentes();

        if (pendentes.isEmpty()) {
            logger.info("📭 Nenhum lembrete pendente para envio");
            return;
        }

        logger.info("📨 Enviando {} lembretes pendentes...", pendentes.size());

        int enviados = 0;
        int erros = 0;

        for (Lembrete lembrete : pendentes) {
            try {
                enviarLembrete(lembrete.getId());
                enviados++;
            } catch (Exception e) {
                erros++;
                logger.error("❌ Falha ao enviar lembrete {}: {}", lembrete.getId(), e.getMessage());
            }
        }

        logger.info("✅ Envio automático concluído: {} enviados, {} erros", enviados, erros);
    }

    /**
     * Envia lembretes automáticos para consultas nas próximas 24 horas
     */
    @Transactional
    public void enviarLembretesAutomaticos() {
        logger.info("⏰ Iniciando envio automático de lembretes...");

        List<Consulta> consultasProximas = consultaService.buscarConsultasPendentesLembrete();

        if (consultasProximas.isEmpty()) {
            logger.info("📭 Nenhuma consulta próxima precisa de lembrete");
            return;
        }

        logger.info("📋 Encontradas {} consultas para lembrete", consultasProximas.size());

        int criados = 0;
        int erros = 0;

        for (Consulta consulta : consultasProximas) {
            try {
                // Verifica se já existe lembrete enviado para esta consulta
                List<Lembrete> existentes = lembreteRepository.findByConsulta(consulta);
                boolean jaEnviado = existentes.stream().anyMatch(Lembrete::getEnviado);

                if (jaEnviado) {
                    logger.debug("⏭️ Consulta {} já possui lembrete enviado", consulta.getId());
                    continue;
                }

                criarEEnviarLembrete(consulta.getId());
                criados++;

            } catch (Exception e) {
                erros++;
                logger.error("❌ Erro ao processar consulta {}: {}", consulta.getId(), e.getMessage());
            }
        }

        logger.info("✅ Envio automático concluído: {} lembretes criados, {} erros", criados, erros);
    }

    /**
     * Monta a mensagem do lembrete
     */
    private String montarMensagem(Consulta consulta) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        StringBuilder msg = new StringBuilder();
        msg.append("🐾 ClyvoPet - Clínica Veterinária\n\n");
        msg.append("Olá ").append(consulta.getAnimal().getTutorNome()).append("!\n\n");
        msg.append("Lembramos que seu pet ").append(consulta.getAnimal().getNome());
        msg.append(" tem consulta agendada para:\n\n");
        msg.append("Data: ").append(consulta.getDataHora().format(dateFormatter)).append("\n");
        msg.append("Hora: ").append(consulta.getDataHora().format(timeFormatter)).append("\n");
        msg.append("Veterinário: ").append(consulta.getVeterinario().getNome()).append("\n");

        if (consulta.getMotivo() != null && !consulta.getMotivo().isEmpty()) {
            msg.append("Motivo: ").append(consulta.getMotivo()).append("\n");
        }

        msg.append("\nPor favor, confirme sua presença com antecedência.\n");
        msg.append("Caso precise remarcar, entre em contato conosco.\n\n");
        msg.append("Agradecemos pela preferência! 🐶🐱");

        return msg.toString();
    }

    /**
     * Deleta um lembrete (apenas se não enviado)
     */
    @Transactional
    public void deletar(Long id) {
        Lembrete lembrete = buscarPorId(id);
        if (lembrete.getEnviado()) {
            throw new RuntimeException("Não é possível deletar um lembrete já enviado");
        }
        lembreteRepository.delete(lembrete);
        logger.info("🗑️ Lembrete {} deletado", id);
    }

    /**
     * Conta quantos lembretes pendentes existem
     */
    public long contarPendentes() {
        return lembreteRepository.countByEnviadoFalse();
    }
}