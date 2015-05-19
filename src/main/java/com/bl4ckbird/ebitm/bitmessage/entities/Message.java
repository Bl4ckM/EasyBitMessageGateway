/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bl4ckbird.ebitm.bitmessage.entities;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
/**
 *
 * @author bl4ckbird
 */
public class Message {
    @Getter
    @Setter
    private String msgid = "";
    @Getter
    @Setter
    private String toAddress= "";
    @Getter
    @Setter
    private String fromAddress= "";
    @Getter
    private String subject= "";
    
    @Setter
    private String message= "";
    
    @Getter
    @Setter
    private String encodingType= "";
    @Getter
    @Setter
    private String receivedTime= "";
    @Getter
    @Setter
    private String read= "";
    
        public String getMessage(){
        String decmsg = "";
        try {
            decmsg = new String(Base64.decodeBase64(message), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decmsg;
    }
    
      public String getSubject(){
        String decsub = "";
        try {
            decsub = new String(Base64.decodeBase64(subject), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decsub;
    }

}
