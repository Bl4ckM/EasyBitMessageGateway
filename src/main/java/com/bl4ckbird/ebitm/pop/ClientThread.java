/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bl4ckbird.ebitm.pop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author bl4ckbird
 */
public class ClientThread implements Runnable{
    
    
        
    
    private Socket connection;
    private BufferedReader input;
    private PrintWriter output;
    private POP3CommandInterpreter interpreter;
    
    public ClientThread(Socket s) throws IOException{
        connection = s;
        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
        if(this instanceof POPClient){
            interpreter = new POP3CommandInterpreter();
        }
        
    }
    
    @Override
    public void run(){
        String answer = interpreter.getWelcomeResponse();
        output.print(answer);
        output.flush();
        while(true){
            try {
                String line = input.readLine();
                
                if(line != null){
                answer = interpreter.handleInput(line);
                
                output.print(answer);
                output.flush();
                }
                
            } catch (IOException ex) {
                break;
            }
        }
        
    }
    
}
