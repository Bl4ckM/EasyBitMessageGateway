/* 
 * The MIT License
 *
 * Copyright 2015 Lukas Kaupp 'Bl4ckM' <lukas.kaupp@stud.h-da.de>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.bl4ckbird.ebitm.pop;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lukas Kaupp 'Bl4ckM' <lukas.kaupp@stud.h-da.de>
 */
public class POP3Server extends Thread{
    private int PORT;
    private ServerSocket server;
    private ArrayList<POP3Client> clients;
    
    public POP3Server(){
        if(this instanceof POP3Server){
            this.PORT = 110;
        }
        clients = new ArrayList<>();
        try {
            server = new ServerSocket(this.PORT);
        } catch (IOException ex) {
            Logger.getLogger(POP3Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run(){
        while(true){
            try {
                
                
                Socket clientcon = server.accept();
                POP3Client client = null;
                
                if(this instanceof POP3Server){
                    client = new POP3Client(clientcon);
                }
              
                Thread t = new Thread(client);
                t.start();
             
                
                
            } catch (IOException ex) {
                break;
            }
        }
    }
    
}
