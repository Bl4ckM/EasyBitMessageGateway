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
package com.bl4ckbird.ebitm.bitmessage;

import com.bl4ckbird.ebitm.bitmessage.entities.AddressBook;
import com.bl4ckbird.ebitm.bitmessage.entities.Inbox;
import com.bl4ckbird.ebitm.bitmessage.entities.Message;
import com.google.gson.Gson;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author Lukas Kaupp 'Bl4ckM' <lukas.kaupp@stud.h-da.de>
 */
public class BitMessageClient {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 9553;
    private static final String PASSWORD = "loco";
    private static final String USERNAME = "bl4ckbird";
    private XmlRpcClient client;
    private Gson gson;
    private AddressBook addressBook;
    private Inbox inbox;
    
    public BitMessageClient() throws MalformedURLException{
         XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
         config.setServerURL(new URL("http://" +  IP + ":" + PORT + "/"));
         config.setBasicUserName(USERNAME);
         config.setBasicPassword(PASSWORD);
         client = new XmlRpcClient();
         client.setConfig(config);
         gson = new Gson();
         addressBook = this.getAddressBook();
         inbox = this.getInbox();
    }
    
    public AddressBook getAddressBook(){
        AddressBook adrbook = new AddressBook();
        Object[] params = new Object[]{};
        try {
            String result = (String) client.execute("listAddresses2", params);
            adrbook = gson.fromJson(result, AddressBook.class);
        } catch (XmlRpcException ex) {
            Logger.getLogger(BitMessageClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adrbook;
    }
    
    public Inbox getInbox(){
        Inbox inbox = new Inbox();
        Object[] params = new Object[]{};
        try {
            String result = (String) client.execute("getAllInboxMessages", params);
            inbox = gson.fromJson(result, Inbox.class);
        } catch (XmlRpcException ex) {
            Logger.getLogger(BitMessageClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inbox;
    }
    
    public List<Message> getAllMessages(){
        return this.getInbox().getInboxMessages();
    }
    
    public Boolean deleteMessage(int msgid){
         Object[] params = new Object[]{msgid};
        try {
            String result = (String) client.execute("trashMessage", params);
            
        } catch (XmlRpcException ex) {
            Logger.getLogger(BitMessageClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    public Boolean sendMessage(String from, String to, String subject, String message){
         Object[] params = new Object[]{from,to,subject,message};
        try {
            String result = (String) client.execute("sendMessage", params);
          
        } catch (XmlRpcException ex) {
            Logger.getLogger(BitMessageClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    
    
}
