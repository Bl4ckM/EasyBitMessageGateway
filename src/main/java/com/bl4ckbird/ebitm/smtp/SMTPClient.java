package com.bl4ckbird.ebitm.smtp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.bl4ckbird.ebitm.bitmessage.BitMessageClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.subethamail.smtp.TooMuchDataException;
import org.subethamail.smtp.helper.SimpleMessageListener;

/**
 *
 * @author bl4ckbird
 */
public class SMTPClient implements SimpleMessageListener {
    private BitMessageClient client;
    public SMTPClient(){
        try {
            client = new BitMessageClient();
        } catch (MalformedURLException ex) {
            Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean accept(String string, String string1) {
        return true;
    }

    @Override
    public void deliver(String from, String to, InputStream data) throws TooMuchDataException, IOException {
       
        System.out.println("from: " + from);
        System.out.println("to:" + to);
        BufferedReader br = new BufferedReader(new InputStreamReader(data));
        String line = "";
        while((line = br.readLine() ) != null){
        System.out.println("data: " + line);
        }
    }
    
   
    
}
