package com.fakecoders.foundation.vamana.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;

@Component
public class SmtpMailSender {

    @Value("${spring.mail.username}")
    private String from;
    
    @Value("${spring.mail.password}")
    private String password;
    
    @Value("${spring.mail.host}")
    private String host;
    
    @Value("${spring.mail.port}")
    private String port;

    @Bean
    public JavaMailSender javaMailSender() {
    	 JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
         mailSender.setHost(host);
         mailSender.setPort(Integer.parseInt(port));
         mailSender.setUsername(from);
         mailSender.setPassword(password);

         Properties props = mailSender.getJavaMailProperties();
         props.put("mail.transport.protocol", "smtp");
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.debug", "true");

         return mailSender;
    }

    public void sendMail(String to, String subject, String body) throws MessagingException {
    	System.out.println("inside mail");
    	
    	System.out.println("inside mail 3");
    	
        MimeMessage message = javaMailSender().createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);//true indicates multipart message

        helper.setFrom(from); // <--- THIS IS IMPORTANT

        helper.setSubject(subject);
        helper.setTo(to);
        helper.setText(body, true);//true indicates body is html
        javaMailSender().send(message);
        System.out.println("inside mail sucess");
    }
}
