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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Lukas Kaupp 'Bl4ckM' <lukas.kaupp@stud.h-da.de>
 */
public class POP3Client implements Runnable{
    
    
        
    
    private Socket connection;
    private BufferedReader input;
    private PrintWriter output;
    private POP3CommandInterpreter interpreter;
    
    public POP3Client(Socket s) throws IOException{
        connection = s;
        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
        if(this instanceof POP3Client){
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
