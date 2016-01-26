/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import static java.lang.Thread.sleep;
import java.util.List;

/**
 *
 * @author Ste
 */
public class MessageHandler {
    
    private SMSGateway iSMSGateway;
    private EmailGateway iEmailGateway;
    private Thermostat iThermostat;
    
    public MessageHandler(Thermostat aThermostat){
        iSMSGateway = SMSGateway.getInstance(this);
        iSMSGateway.initialize();
        iEmailGateway = EmailGateway.getInstance();
        iThermostat = aThermostat;
        //TODO as singleton
    }
    
    public void sendMessage(Message aMessage){
        if (aMessage == null || (aMessage.getUser() == null)){
            System.out.println("MessageController sendMessage doing nothing. Message invalid");
            return;
        }
        User tUser = aMessage.getUser();
        if (tUser == null) {
            System.out.println("USER == null!");
            return;
        }
        if (ThermostatProperties.PREFER_EMAIL_REPLIES_IF_AVAILABLE && tUser.hasValidEmail() && ThermostatProperties.A != null && ThermostatProperties.B != null){
            try {
                sendEmailMessage(aMessage);
            } catch (RuntimeException e){
                System.out.println("Sending Email failed, trying to send sms instead");
                if (tUser.hasValidMobileNr()){
                    sendSMSMessage(aMessage);
                }
            }
        } else if (tUser.hasValidMobileNr()){
            sendSMSMessage(aMessage);
        }
    }
    
    public void processReceivedSMSS(List<SMS> aSMSs){
        for (SMS tSMS : aSMSs) {
            CommandType tCommand = CommandParser.parse(tSMS);
            User tUser = Users.getUser(tSMS.getSender());
            if (tSMS.isDateValid() && Users.isAuthorized(tUser) && tCommand != null && tCommand.isActive()) {
                System.out.println("Date Valid & User Authorized & Command is active. Executing: -------> " + tSMS);
                iThermostat.processReceivedCommand(tCommand, tUser, tSMS);
                break; //execute only last command
            } else {
                System.out.println("SMS discarded: " + tSMS);
            }
        }
    } 

    private void sendEmailMessage(Message aMessage) {
        iEmailGateway.sendEmail(aMessage.getUser().getEmail(), aMessage.getBody());
        
    }

    private void sendSMSMessage(Message aMessage) {
        iSMSGateway.sendTextMessageToUser(aMessage.getUser().getMobileNr(), aMessage.getBody());
    }
    
    
    /////////////////////
    
    public void stop(){
        if (iSMSGateway != null) {
            System.out.println("Thermostat: Turning off SMSGateway");
            waitABit(3000);
            iSMSGateway.stop();
            iSMSGateway = null;
        }
    }
    
    private void waitABit(int a) {
        try {
            Thread.sleep(a);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    ///////////
    // TEST
    ///////////
    
    public void testSendSMS() {
        iSMSGateway.sendTextMessageToUser("+46700447531", "This is anooother test");
    }

    public void testLoopingAT() {
        iSMSGateway.testLoopingAT();
    }

    public String testReadAllMessagesRaw() {
        return iSMSGateway.readAllMessagesRaw();
    }

    public void testReadAllMessages() {
        for (SMS tSMS : iSMSGateway.getAllMessages()) {
            System.out.println(tSMS);
        }
    }

    public void testReadAllMessagesOneByOne() {
        for (SMS tSMS : iSMSGateway.getAllMessages()) {
            System.out.println(iSMSGateway.readMsgAtCertainPosition(tSMS.getPosition()));
        }
    }

    /*   Keep if i want to eliminate the listener
    public void startPollingIncomingCommands(boolean aDeleteReadMessages, int aSeconds) {
        timer = new Timer();
        //every 30 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<SMS> tSMSs = iSMSGateway.getAllMessages();
                Collections.sort(tSMSs);
                Collections.reverse(tSMSs);
                //print the list
                System.out.println("List of all messages on the modem ordered by date:");
                for (SMS tSMS : tSMSs) {
                    System.out.println(tSMS);
                }
                //check for valid commands
                for (SMS tSMS : tSMSs) {
                    if (tSMS.isDateValid() && tSMS.senderAuthorized() && (CommandParser.parse(tSMS)).isValid()) {
                        System.out.println("Date Valid & User Authorized & Command is valid. Executing: -------> " + tSMS);
                        iController.executeCommand(CommandParser.parse(tSMS));
                        break; //execute only last command
                    } else {
                        System.out.println("SMS discarded: " + tSMS);
                    }
                }
                if (aDeleteReadMessages) {
                    //TODO delete all messages
                    for (SMS tSMS : tSMSs) {
                        System.out.println("Delete message "+tSMS);
                        String tResp = iSMSGateway.deleteMsgAtCertainPosition(tSMS.getPosition());
                        System.out.println(tResp);
                    }
                }
            }
        }, 0, aSeconds * 1000);
    }*/
}
