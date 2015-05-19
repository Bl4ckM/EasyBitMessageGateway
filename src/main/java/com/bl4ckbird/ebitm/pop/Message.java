package com.bl4ckbird.ebitm.pop;

/**
 * Message
 * 
 * An instance of this class represents a unique email handled by the pop3
 * system.
 * 
 * @author Matthew Herod
 * 
 */

public class Message {

	private String uniqueId;
	private String header;
	private String body;

	private int bodySize;

	private boolean deleted;

	private boolean deletePending = false;

	public Message(String uniqueId, String from, String to, String subject, String body, boolean deleted) {
                this.header = "";
                this.header += "Received: from luckyluke (www.007guard.com [127.0.0.1])\r\n" +
"by luckyluke.fritz.box\r\n" +
"with SMTP (SubEthaSMTP 3.1.7) id I9SL2W1C\r\n" +
"for <"+to+"@bla.com>;\r\n" +
"Sun, 17 May 2015 16:54:14 +0200 (CEST)\r\n";
                this.header += "Date: Fri, 15 May 2015 17:20:44 +0200\r\n";
                this.header += "Subject: " + subject + "\r\n" ;
                this.header += "Message-ID: " +uniqueId +"\r\n";
                this.header += "From: " +from + " <" + from +"@bla.com>" +"\r\n";
                this.header += "To: " +to + " <" + to +"@bla.com>" +"\r\n";
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
