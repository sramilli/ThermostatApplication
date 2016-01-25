/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Ste
 */
public class SMSGateway implements SerialDataListener{

    private static SMSGateway iInstance = null;
    private static MessageHandler iMessageHandler = null;
    
    Serial serial;
    SerialDataListener iSerialDataListener = null;
    
    static final private char ctrlZ = (char) 26;
    static final private char ctrlD = (char) 4;

    private SMSGateway(){
    }

    public static synchronized SMSGateway getInstance(MessageHandler aMessageHandler) {
        if (iInstance == null) {
            iInstance = new SMSGateway();
        }
        iMessageHandler = aMessageHandler;
        return iInstance;
    }

    public void initialize() {

        iSerialDataListener = this;
        System.out.println(" ... connect using settings: "+ThermostatProperties.GSM_BAUD_RATE+", N, 8, 1.");
        // create an instance of the serial communications class
        serial = SerialFactory.createInstance();
        serial.open(Serial.DEFAULT_COM_PORT, ThermostatProperties.GSM_BAUD_RATE);
        waitABit(3000);
        
        sendATCommand();
        System.out.println(readAnswer()); 
        // create and register the serial data listener
        /*serial.addListener(new SerialDataListener() {
             @Override
             public void dataReceived(SerialDataEvent event) {
                 // print out the data received to the console
                 waitABit(2000);
                 System.out.println("");
                 System.out.print("Resp---->"+event.getData()+"<----");
             
         });}*/
        System.out.println("Reading all old message present on the SIM at boot");
        List<SMS> tSMSs = this.getAllMessages();
        if (tSMSs.size() > 0){
            printAllMessages(tSMSs);
            deleteAllMessages(tSMSs);
        } else {
            System.out.println("No message present on the modem at startup");
        }
        serial.addListener(iSerialDataListener);
    }
    
    ////////////
    // listener from Thermostat
    ////////////
    
        @Override
     public void dataReceived(SerialDataEvent event) {
         // print out the data received to the console
         //http://www.developershome.com/sms/resultCodes3.asp
         System.out.println("Incoming event arrived from the GSM module!");
         String response = event.getData();
         this.removeListener(this);
         //String response = iSMSGateway.readAnswer();
         System.out.println("");
         if (GSMDataInterpreter.getCommand(response).equals(GSMCommand.MESSAGE_ARRIVED)){
            System.out.println("A new message has arrived!");
            System.out.print("AAAAASMSGateway-dataReceived: ---->"+response+"<----");
            waitABit(3000);
            List<SMS> tSMSs = getAllMessages();
            printAllMessages(tSMSs);
            
            iMessageHandler.processReceivedSMSS(tSMSs);
            
            //delete all messages
            this.deleteAllMessages(tSMSs);
         }
         this.addListener(this);
     }



    //
    //Collection utility
    //
    public void deleteAllMessages(Collection<SMS> aMessages){
        System.out.println("Deleting all old messages found at boot");
        for (SMS tSMS : aMessages) {
            System.out.println("Delete message "+tSMS);
            String tResp = this.deleteMsgAtCertainPosition(tSMS.getPosition());
            System.out.println("--->"+tResp);
        }
        System.out.println("All messages deleted!");
    }
    
    public void printAllMessages(Collection<SMS> aMessages){
        //print the list
        System.out.println("List of all messages on the modem ordered by date:");
        for (SMS tSMS : aMessages) {
            System.out.println(tSMS);
        }
    }
    
    //
    //Simple send AT command
    //
    public void sendATCommand (){
        System.out.println("---->Sending: AT");
        serial.write("AT\r");
        waitABit(3000); //TODO tweeka
    }
    
    public void sendReadAllMessagesCommand(){
        System.out.println("atReadAllMessages:---->Sending: AT+CMGL=\"ALL\"");
        serial.write("AT+CMGL=\"ALL\"\r");
        waitABit(3000);
    }
    
    
    //
    //Send message to user
    //
    public void sendStatusToUser(String aRecipient, String aMessage) {
        System.out.println("Sending Status message ["+aMessage+"] to recipient ["+aRecipient+"] ");
        sendTextMessageToUser(aRecipient, aMessage);
    }
    
    public void sendHelpMessageToUser(String aRecipient) {
        System.out.println("Sendind Help message to ["+aRecipient+"] ");
        sendTextMessageToUser(aRecipient, 
                "Examples:\n"
                + "1) on\n"
                + "2) off\n"
                + "3) manual\n"
                + "4) status\n"
                + "5) help\n"
                + "6) register +391234512345\n"
                + "7) ProgramDaily 6:15-7:45");
    }
    
    public void sendTextMessageToUser(String aNumberRecipient, String aMessage) {
        System.out.println("---->Sending: AT");
        serial.write("AT\r");
        readAnswerAndPrint();

        System.out.println("---->Sending: AT+CMGF=1");
        serial.write("AT+CMGF=1\r");
        readAnswerAndPrint();

        System.out.println("---->Sending: AT+CMGS=\""+aNumberRecipient+"\"");
        serial.write("AT+CMGS=\""+aNumberRecipient+"\"\r");
        readAnswerAndPrint();

        System.out.println("---->Sending: " + aMessage);
        serial.write(aMessage + ctrlZ);
        //this is needed because sending the sms takes time
        waitABit(4000);
        readAnswerAndPrint();
    }
    
    //
    //Simple read
    //
    public String readAnswer() {
        waitABit(3000);
        StringBuffer tReply = new StringBuffer();
        while (serial.availableBytes() > 0) {
            tReply.append(serial.read());
        }
        //System.out.println("RAW messages:\n"+tReply.toString());
        return tReply.toString();
    }
    

    //
    //Commands + answers
    //
    public String readAllMessagesRaw() {
        System.out.println("---->Sending: AT+CMGL=\"ALL\"");
        serial.write("AT+CMGL=\"ALL\"\r");
        waitABit(3000); //TODO tweeka
        String msgs = readAnswer();
        System.out.println("Raw data from GSM module:\n" + msgs);
        return msgs;
        /*
         AT+CMGL="ALL"
         +CMGL: 1,"REC READ","Telia","","15/04/27,21:31:40+08"
         Ditt saldo borjar bli lagt. Sla *120# lur/skicka for att kontrollera saldo. Ladda direkt via kontokort, m.telia.se/snabbladda eller las mer om de andra l
         +CMGL: 2,"REC READ","Telia","","15/04/27,21:31:41+08"
         addningssatten pa www.telia.se/ladda. Halsningar Telia
         +CMGL: 3,"REC READ","+46700447531","","15/05/02,18:01:08+08"
         Sms di prova
         +CMGL: 4,"REC READ","+46700447531","","15/05/02,18:01:34+08"
         Andra prova
         +CMGL: 5,"REC READ","+46700447531","","15/05/02,18:01:51+08"
         Terza prova
         +CMGL: 6,"REC READ","Telia","","15/05/14,19:32:43+08"
         Du har nu fatt 25 kr i bonus. Bonusen far du i samband med dina fyra forsta laddningar efter din registrering. Total bonus blir 100 kr. Mvh Telia
         +CMGL: 7,"REC READ","Telia","","15/05/14,19:32:44+08"
         Ditt kort ar nu laddat. For att se ditt saldo tryck *120# lur/skicka. For mer information om priser se www.telia.se/refill
         +CMGL: 8,"REC READ","+46700447531","","15/05/21,22:37:34+08"
         . 
         +CMGL: 9,"REC READ","+46700447531","","15/05/21,22:38:21+08"
         00510075006500730074006F000A00C80020000A0055006E00200073006F006C006F0020006D0065007300730061006700670069006F
         +CMGL: 10,"REC READ","+46700447531","","15/05/26,22:59:28+08"
         On
         OK
         */
    }

    public List<SMS> getAllMessages() {
        //read all messages and parse them
        String rawMessageString = readAllMessagesRaw();
        return parseAllMessages(rawMessageString);
    }
    
    public String readMsgAtCertainPosition(int aPos) {
        System.out.println("---->Sending: AT+CMGR=" + aPos);
        serial.write("AT+CMGR=" + aPos + "\r");
        waitABit(3000); //TODO tweeka
        return readAnswer();
    }
    
    public String deleteMsgAtCertainPosition(int aPos) {
        System.out.println("---->Sending: AT+CMGD=" + aPos);
        serial.write("AT+CMGD=" + aPos + "\r");
        waitABit(3000); //TODO tweeka
        return readAnswer();
    }
    
    private void readAnswerAndPrint() {
        waitABit(1000);
        StringBuffer reply = new StringBuffer();
        while (serial.availableBytes() > 0) {
            reply.append(serial.read());
        }
        if (reply.length() > 0) {
            System.out.println("//////:\n" + reply + "//////");
        } else {
            System.out.println("<---->NO ANSWER FROM GSM MODULE!");
        }
        //whaitABit(1000);
    }
    
    //
    //Parse utility
    //
    public List<SMS> parseAllMessages(String aMessages) {
        //read all messages
        System.out.println("Start parsing string: start string-->"+aMessages+"<--end string");
        StringTokenizer st = new StringTokenizer(aMessages, "\r\n");
        //parse messages
        List<SMS> tSMSs = new ArrayList<SMS>();
        int i = 1;
        List<String> tRows = new ArrayList<String>();

        while (st.hasMoreTokens()) {
            tRows.add(st.nextToken());
        }
        boolean headClean = false, smsNotAddedYet = false;
        SMS tSMS = new SMS();

        outerLoop:
        for (int j = 0; j < tRows.size() - 1; j++) {
            String s = tRows.get(j);
            while (!headClean && !s.startsWith("+CMGL")) {
                continue outerLoop;
            }
            headClean = true;
            if (s.startsWith("+CMGL")) {
                tSMS = new SMS();
                tSMS.parseHeaderAndSetData(s);
                continue;
            } else {
                tSMS.setText(tSMS.getText() + s);
                tSMSs.add(tSMS);
                continue;
            }
        }
        System.out.println("Parsed "+tSMSs.size()+" smss");
        Collections.sort(tSMSs);
        Collections.reverse(tSMSs);
        return tSMSs;
    }
    
    //
    //Test
    //
    public void testLoopingAT() {
        for (int i = 0; i < 10; i++) {
            System.out.println("----Sending: AT (" + i + "), " + new Date().toString());
            serial.write("AT\r");
            readAnswerAndPrint();
            waitABit(5000);
        }
    }
    
    
    
    
    private void waitABit(int a) {
        try {
            // wait 1 second before continuing
            Thread.sleep(a);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeListener(SerialDataListener aListener){
        serial.removeListener(aListener);
    }
    
    public void addListener(SerialDataListener aListener){
        serial.addListener(aListener);
    }

    public void stop() {
        if (serial != null) {
            serial.removeListener(iSerialDataListener);
            serial.close();
            serial = null;
        }
    }



}
