/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Ste
 */
class EmailGateway {
    
    private static EmailGateway iInstance = null;
        
    private EmailGateway(){
    }

    public static synchronized EmailGateway getInstance() {
        if (iInstance == null) {
            iInstance = new EmailGateway();
        }
        return iInstance;
    }

    void sendTextMessageToUser(String aEmail, String aBody) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ThermostatProperties.A, ThermostatProperties.B);
                }
          });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ThermostatProperties.B));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aEmail));
            message.setSubject("Status response");
            message.setText(aBody);
            Transport.send(message);
            System.out.println("EMAIL sent");
        } catch (MessagingException e) {
                throw new RuntimeException(e);
        }
    }
    
}
