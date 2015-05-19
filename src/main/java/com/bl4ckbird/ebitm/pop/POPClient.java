/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bl4ckbird.ebitm.pop;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author bl4ckbird
 */
public class POPClient extends ClientThread{

    public POPClient(Socket s) throws IOException {
        super(s);
    }
    
 
    
}
