/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bl4ckbird.ebitm.pop;

import com.bl4ckbird.ebitm.pop.POP3Server;
import com.bl4ckbird.ebitm.pop.POPClient;
import com.bl4ckbird.ebitm.smtp.SMTPClient;
import com.bl4ckbird.ebitm.smtp.SMTPServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bl4ckbird
 */
public class Server extends Thread{
    private int PORT;
    private ServerSocket server;
    private ArrayList<ClientThread> clients;
    
    public Server(){
        if(this instanceof POP3Server){
            this.PORT = 110;
        }
        clients = new ArrayList<>();
        try {
            server = new ServerSocket(this.PORT);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run(){
        while(true){
            try {
                
                
                Socket clientcon = server.accept();
                ClientThread client = null;
                
                if(this instanceof POP3Server){
                    client = new POPClient(clientcon);
                }
              
                Thread t = new Thread(client);
                t.start();
             
                
                
            } catch (IOException ex) {
                break;
            }
        }
    }
    
}
