package com.packovery.auth;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailService {


    @Inject
    Mailer mailer;

    @Inject
    @Location("otp.html")
    Template otpTemplate;

    public void sendOtpEmail(String toEmail, String otp) {

        String bodyHtml = otpTemplate
                .data("otp", otp)
                .render();

        Mail email = Mail.withHtml(
                toEmail,
                "Il tuo Codice di Verifica",
                bodyHtml
        ).setReplyTo("testquarkus24@gmail.com");

        mailer.send(email);
    }
}
