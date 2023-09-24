package com.fakecoders.foundation.quickstart.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fakecoders.foundation.quickstart.event.OnGenerateResetLinkEvent;
import com.fakecoders.foundation.quickstart.exception.MailSendException;
import com.fakecoders.foundation.quickstart.model.PasswordResetToken;
import com.fakecoders.foundation.quickstart.model.User;
import com.fakecoders.foundation.quickstart.service.impl.MailService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import java.io.IOException;

@Component
public class OnGenerateResetLinkEventListener implements ApplicationListener<OnGenerateResetLinkEvent> {

    private static final Logger logger = LoggerFactory.getLogger(OnGenerateResetLinkEventListener.class);
   
    private final MailService mailService;

    @Autowired
    public OnGenerateResetLinkEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * As soon as a forgot password link is clicked and a valid email id is entered,
     * Reset password link will be sent to respective mail via this event
     */
    @Override
    @Async
    public void onApplicationEvent(OnGenerateResetLinkEvent onGenerateResetLinkMailEvent) {
        sendResetLink(onGenerateResetLinkMailEvent);
    }

    /**
     * Sends Reset Link to the mail address with a password reset link token
     */
    private void sendResetLink(OnGenerateResetLinkEvent event) {
        PasswordResetToken passwordResetToken = event.getPasswordResetToken();
        User user = passwordResetToken.getUser();
        String recipientAddress = user.getEmail();
       
        logger.info("enail link {} ",event.getRedirectUrl());
        String emailConfirmationUrl = event.getRedirectUrl().queryParam("email", recipientAddress).queryParam("token", passwordResetToken.getToken())
                .toUriString();
        try {
            mailService.sendResetLink(emailConfirmationUrl, recipientAddress);
        } catch (IOException | TemplateException | MessagingException e) {
            //logger.error(e);
            throw new MailSendException(recipientAddress, "Email Verification");
        }
    }

}
