/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bl4ckbird.ebitm;

import com.bl4ckbird.ebitm.pop.POP3Server;
import com.bl4ckbird.ebitm.smtp.SMTPServer;

/**
 *
 * @author bl4ckbird
 */
public class main {
    
    public static void main(String[] args){
        POP3Server pop = new POP3Server();
        SMTPServer smtp = new SMTPServer();
        
        pop.start();
        smtp.start();
        
    }
    
}
