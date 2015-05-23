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
package com.bl4ckbird.ebitm.smtp;




import com.bl4ckbird.ebitm.bitmessage.BitMessageClient;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.subethamail.smtp.TooMuchDataException;
import org.subethamail.smtp.helper.SimpleMessageListener;

/**
 *
 * @author Lukas Kaupp 'Bl4ckM' <lukas.kaupp@stud.h-da.de>
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



        from = from.substring(0,from.indexOf("@"));
        to = to.substring(0,to.indexOf("@"));



        BufferedReader br = new BufferedReader(new InputStreamReader(data));
        String line ="";
        String body = "";
        String doublespace = "";
        String subject = "";

        Boolean bodyend = false;
        Boolean bodybegin = false;
        while((line = br.readLine()) != null){
             Matcher subjectptrn = Pattern.compile("^Subject: (.*)$").matcher(line);
             Matcher bodyptrn = Pattern.compile("^Content-Transfer-Encoding: (.*)$").matcher(line);
            if(subjectptrn.matches())
                subject = subjectptrn.group(1);




            if(bodybegin){

                   body += line + "\r\n";
                   if(line.equals("")) {
                       if (bodyend)
                           break;

                       bodyend = true;
                   }else
                       bodyend = false;



            }

            if(bodyptrn.matches())
                bodybegin = true;


        }
        client.sendMessage(from, to, subject, body);

        BufferedReader br2 = new BufferedReader(new InputStreamReader(data));
        String line2 = "";
        while((line2 = br.readLine() ) != null){
        System.out.println("data: " + line2);
        }
    }
    
   
    
}
