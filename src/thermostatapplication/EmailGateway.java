/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
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

        //1) setup Mail Server Properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        
        /*Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ThermostatProperties.A, ThermostatProperties.B);
                }
          });*/
        
        /*try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ThermostatProperties.B));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aEmail));
            message.setSubject("Status response");
            message.setText(aBody);
            Transport.send(message);
            System.out.println("EMAIL sent");
        } catch (MessagingException e) {
                throw new RuntimeException(e);
        }*/
        
        //2) get Mail Session
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            //msg.setFrom(new InternetAddress(ThermostatProperties.C, "Raspberry"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(aEmail));
            msg.setSubject("Status response");
            //msg.setText(msgBody);
            System.out.println("SENDING EMAIL: "+aBody);
            //System.out.println("SENDING EMAIL (modified): "+aBody.replaceAll("\n", "<br>"));
            aBody = aBody.replaceAll("\n", "<br>");
            msg.setContent(aBody, "text/html");
            //Transport.send(msg);
            
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", ThermostatProperties.A, ThermostatProperties.B);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    
}
