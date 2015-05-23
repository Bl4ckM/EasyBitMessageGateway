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

import java.io.*;
import java.util.Properties;

/**
 * Message
 * 
 * An instance of this class represents a unique email handled by the pop3
 * system.
 * 
 * @author Matthew Herod
 * @author Lukas Kaupp 'Bl4ckM' <lukas.kaupp@stud.h-da.de>
 */

public class POP3Message {

	private String uniqueId;
	private String header;
	private String body;

	private int bodySize;

	private boolean deleted;

	private boolean deletePending = false;

	public POP3Message(String uniqueId, String from, String to, String subject, String body, boolean deleted) {

        Properties prop = new Properties();
        try {
            InputStream fr = this.getClass().getResourceAsStream("/ebitg.properties");
            prop.load(fr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ending = (String)prop.get("mail.ending");
        this.header = "";
                this.header += "Received: from luckyluke (www.007guard.com [127.0.0.1])\r\n" +
"by luckyluke.fritz.box\r\n" +
"with SMTP (SubEthaSMTP 3.1.7) id I9SL2W1C\r\n" +
"for <"+to+ ending+">;\r\n" +
"Sun, 17 May 2015 16:54:14 +0200 (CEST)\r\n";
                this.header += "Date: Fri, 15 May 2015 17:20:44 +0200\r\n";
                this.header += "Subject: " + subject + "\r\n" ;
                this.header += "Message-ID: " +uniqueId +"\r\n";
                this.header += "From: " +from + " <" + from + ending +">" +"\r\n";
                this.header += "To: " +to + " <" + to + ending +">" +"\r\n";
                this.header += "MIME-Version: 1.0\r\n";
                this.header += "Content-Type: text/plain; charset=UTF-8\r\n\r\n";
            
            
		this.uniqueId = uniqueId;
		
		this.body = body;
		this.bodySize = body.length();
		this.deleted = deleted;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public String getHeader() {
		return header;
	}

	public String getBody() {
		return body;
	}
	
	public String[] getBodyLines(int numLines) {
		String[] lines = body.split("\n");
		if (numLines > lines.length) {
			numLines = lines.length;
		}
		String[] topLines = new String[numLines];
		for (int i = 0; i < numLines; i++) {
			topLines[i] = lines[i];
		}
		return topLines;
	}

	public int getOctetWeight() {
		return bodySize;
	}

	public String getOctetString() {
		return header + body;
	}

	public boolean isDeleted() {
		return deleted || deletePending;
	}

	public void setDeletePending(boolean delete) {
		deletePending = delete;
	}

	public boolean isDeletePending() {
		return deletePending && !deleted;
	}

}
