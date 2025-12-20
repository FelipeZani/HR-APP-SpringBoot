package com.hrapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

   
    @Value("${spring.mail.username}") 
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNewUserCredentials(String to, String username, String rawPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        // Configuration de l'email
        message.setFrom(fromEmail);
        message.setTo(to); 
        message.setSubject("Vos identifiants de connexion au Système RH");
        
        
        String text = String.format(
            "Bonjour %s,\n\n" +
            "Votre compte d'accès au système de gestion des Ressources Humaines a été créé.\n" +
            "Voici vos identifiants :\n" +
            "   - Nom d'utilisateur (Login): %s\n" +
            "   - Mot de passe : %s\n\n" +
            "Cordialement,\n" +
            "L'équipe RH.",
            username, username, rawPassword
        );
        message.setText(text);

        // envoi
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // affiche dans les logs et lance une exception pour annuler la transaction
            System.err.println("Erreur lors de l'envoi de l'email à " + to + ": " + e.getMessage());
            throw new RuntimeException("Échec de l'envoi des identifiants par email. Compte créé, mais configuration SMTP ou email invalide.", e);
        }
    }
}