package com.bl4ckbird.ebitm.smtp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.bl4ckbird.ebitm.pop.Server;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;

/**
 *
 * @author bl4ckbird
 */
public class SMTPServer{
    
    private final org.subethamail.smtp.server.SMTPServer smtpServer;
    
    public SMTPServer(){
        smtpServer = new org.subethamail.smtp.server.SMTPServer(new SimpleMessageListenerAdapter(new SMTPClient()));
        try {
            smtpServer.setBindAddress(InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException ex) {
            Logger.getLogger(SMTPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        smtpServer.setPort(25);
        
        
    }
    
    public void start(){
        smtpServer.start();
    }
    
    
}
