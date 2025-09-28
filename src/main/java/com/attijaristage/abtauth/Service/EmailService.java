package com.attijaristage.abtauth.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendCredentials(String toEmail, String username, String password) {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Vos identifiants de connexion");
        message.setText("Bonjour " + username + ",\n\n" +
                "Votre compte sur la plateforme bancaire interne d’Attijari Bank a été créé avec succès.\n\n" +
                "Voici vos identifiants de connexion :\n" +
                "• Nom d’utilisateur : " + username + "\n" +
                "• Mot de passe provisoire : " + password + "\n\n" +
                "⚠️ Pour des raisons de sécurité, il est fortement recommandé de modifier votre mot de passe " +
                "lors de votre première connexion afin de protéger l’accès à votre compte.\n\n" +
                "Ceci est un message automatique, merci de ne pas y répondre.\n\n" +
                "Cordialement,\n" +
                "L’équipe IT – Attijari Bank");

        mailSender.send(message);




    }
}

