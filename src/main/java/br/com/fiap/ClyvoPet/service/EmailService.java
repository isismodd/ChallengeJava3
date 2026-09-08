package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.entity.Consulta;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envia um e-mail simples em texto puro
     */
    public void enviarEmailTexto(String para, String assunto, String mensagem) {
        try {
            logger.info("📧 Preparando envio de e-mail para: {}", para);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(mensagem);

            mailSender.send(mimeMessage);
            logger.info("✅ E-mail enviado com sucesso para: {}", para);

        } catch (MessagingException e) {
            logger.error("❌ Erro ao montar e-mail para {}: {}", para, e.getMessage());
            throw new RuntimeException("Erro ao montar e-mail: " + e.getMessage());
        } catch (MailSendException e) {
            logger.error("❌ Erro ao enviar e-mail para {}: {}", para, e.getMessage());
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    /**
     * Envia um e-mail com template HTML (mais bonito)
     */
    public void enviarEmailHtml(String para, String assunto, String template, Context context) {
        try {
            logger.info("📧 Preparando envio de e-mail HTML para: {}", para);

            String htmlContent = templateEngine.process(template, context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("✅ E-mail HTML enviado com sucesso para: {}", para);

        } catch (MessagingException e) {
            logger.error("❌ Erro ao montar e-mail HTML para {}: {}", para, e.getMessage());
            throw new RuntimeException("Erro ao montar e-mail: " + e.getMessage());
        } catch (MailSendException e) {
            logger.error("❌ Erro ao enviar e-mail HTML para {}: {}", para, e.getMessage());
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    /**
     * Envia lembrete de consulta usando template HTML
     */
    public void enviarLembreteConsulta(Consulta consulta) {
        String para = consulta.getAnimal().getTutorEmail();
        String nomeTutor = consulta.getAnimal().getTutorNome();
        String nomeAnimal = consulta.getAnimal().getNome();
        String especie = consulta.getAnimal().getEspecie();

        if (para == null || para.isEmpty()) {
            logger.warn("⚠️ Tutor do animal {} não possui e-mail cadastrado. Lembrete não enviado.", nomeAnimal);
            return;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Context context = new Context();
        context.setVariable("tutorNome", nomeTutor);
        context.setVariable("animalNome", nomeAnimal);
        context.setVariable("animalEspecie", especie);
        context.setVariable("dataConsulta", consulta.getDataHora().format(dateFormatter));
        context.setVariable("horaConsulta", consulta.getDataHora().format(timeFormatter));
        context.setVariable("veterinarioNome", consulta.getVeterinario().getNome());
        context.setVariable("motivo", consulta.getMotivo() != null ? consulta.getMotivo() : "Consulta de rotina");
        context.setVariable("ano", java.time.Year.now().getValue());

        enviarEmailHtml(
                para,
                "🐾 ClyvoPet - Lembrete de Consulta para " + nomeAnimal,
                "email/lembrete-consulta",
                context
        );
    }

    /**
     * Envia um lembrete em texto puro (fallback)
     */
    public void enviarLembreteConsultaTexto(Consulta consulta) {
        String para = consulta.getAnimal().getTutorEmail();
        String nomeTutor = consulta.getAnimal().getTutorNome();
        String nomeAnimal = consulta.getAnimal().getNome();

        if (para == null || para.isEmpty()) {
            logger.warn("⚠️ Tutor do animal {} não possui e-mail cadastrado.", nomeAnimal);
            return;
        }

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("🐾 ClyvoPet - Clínica Veterinária\n\n");
        mensagem.append("Olá ").append(nomeTutor).append("!\n\n");
        mensagem.append("Lembramos que seu pet ").append(nomeAnimal);
        mensagem.append(" tem consulta agendada para:\n\n");
        mensagem.append("📅 Data: ").append(consulta.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        mensagem.append("🕐 Hora: ").append(consulta.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm"))).append("\n");
        mensagem.append("🏥 Veterinário: ").append(consulta.getVeterinario().getNome()).append("\n");

        if (consulta.getMotivo() != null && !consulta.getMotivo().isEmpty()) {
            mensagem.append("📋 Motivo: ").append(consulta.getMotivo()).append("\n");
        }

        mensagem.append("\nPor favor, confirme sua presença com antecedência.\n");
        mensagem.append("Caso precise remarcar, entre em contato conosco.\n\n");
        mensagem.append("Agradecemos pela preferência! 🐶🐱");

        enviarEmailTexto(para, "🐾 ClyvoPet - Lembrete de Consulta", mensagem.toString());
    }
}