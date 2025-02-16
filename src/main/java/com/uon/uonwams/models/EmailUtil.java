/**
 Program: UON WAMS Application
 Filename: EmailUtil.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.models;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
    String fromEmail;
    String fromEmailPassword;
    Session session;

    /**
     JavaMail Example - Send Mail in Java using SMTP. [source code]. Available
     from: https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp [Accessed 23th December 2024].
     */
    // initialize session
    public EmailUtil() {
        // load email and app password of email from .env file
        Dotenv dotenv = Dotenv.load();
        this.fromEmail = dotenv.get("FROM_EMAIL");
        this.fromEmailPassword = dotenv.get("FROM_EMAIL_APP_PASSWORD");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromEmailPassword);
            }
        };
        session = Session.getInstance(props, auth);
    }

    /**
     JavaMail Example - Send Mail in Java using SMTP. [source code]. Available
     from: https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp [Accessed 23th December 2024].
     */
    public void sendEmail(String toEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));
        msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

        // send message to email
        Transport.send(msg);

        System.out.println("EMail Sent Successfully!!");
    }
}