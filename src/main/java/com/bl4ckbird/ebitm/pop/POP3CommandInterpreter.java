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

/**
 * CommandInterpreter.java
 * 
 * My implementation of CommandInterpeter provides a command 
 * handling method, which is capable of returning multiline 
 * responses in with correct formatting for the pop3 protocol.
 * I make use of objects for storing each instance of a user's 
 * 'Maildrop' or a child 'Message'.
 * 
 * Each instance of this class represents a single pop3 client
 * session, and the instance is also in charge of managing the
 * references to each of the objects the session is related to.
 * 
 * The default constructor for this method is private, as to
 * promote my static method for constructing new instances of 
 * this class connectSession(Database database).
 * 
 * Each CommandInterpreter object also stores a reference to the
 * relevant database, as provided by the method calling the
 * constructor for this class.
 * 
 * When operations are performed for updating database information 
 * the relevant object stores a pending mark, which is processed 
 * against the database when the CommandInterpreter enters the 
 * UPDATE state.
 * 
 * @author Matthew Herod, https://github.com/mherod/pop3server
 * @author Lukas Kaupp 'Bl4ckM' <lukas.kaupp@stud.h-da.de>, modified base, add BitMessageClient
 */

import com.bl4ckbird.ebitm.bitmessage.BitMessageClient;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class POP3CommandInterpreter{

	public static enum Command {
		USER, PASS, STAT, UIDL, LIST, RETR, TOP, DELE, RSET, CAPA, NOOP, QUIT
	}

	public static enum State {
		AUTHORIZATION, TRANSACTION, UPDATE
	}

	private BitMessageClient client = null;
	private String username = null;
        private String maildrop;
	private boolean holdingLock = false;

	private boolean disconnectSuggested = false;

	private State currentState;

	public POP3CommandInterpreter() {
            try {
                maildrop = "";
                client = new BitMessageClient();
            } catch (MalformedURLException ex) {
                Logger.getLogger(POP3CommandInterpreter.class.getName()).log(Level.SEVERE, null, ex);
            }
		currentState = State.AUTHORIZATION;
	}

	

	private boolean inState(State state) {
		return currentState.equals(state);
	}

	private void enterState(State newState) {
		currentState = newState;
	}
 
	public String handleInput(String input) {
            System.out.println(input);
		String command = input, argument = "";

		input = input.trim();

		int length = input.length();
		int firstSpace = input.indexOf(" ");
		if (firstSpace > 0) {
			if (length > firstSpace) {
				argument = input.substring(firstSpace + 1, length);
			}
			command = input.substring(0, firstSpace);
		}
		if (argument.length() > 40) {
			argument = argument.substring(0, 40);
		}
		command = command.toUpperCase();

		try {
			Command cmd = Command.valueOf(command);
			Response response = processCommand(cmd, argument);

			if (!cmd.equals(Command.STAT) && !cmd.equals(Command.LIST)
					&& !cmd.equals(Command.UIDL)) {
				response.setTag(input);
			}
                        System.out.println(response.buildOutput());
			return response.buildOutput();

		} catch (IllegalArgumentException iae) {
			return new Response(Response.Type.ERR, "unsupported command")
					.buildOutput();
		}
	}

	private Response processCommand(Command command, String argument) {
		final boolean hasArguments = argument.length() > 0;
		final String[] args = hasArguments ? argument.split(" ")
				: new String[0];

		switch (command) {

		case USER:
			if (!inState(State.AUTHORIZATION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during "
								+ State.AUTHORIZATION);
			}
			if (!hasArguments) {
				return new Response(Response.Type.ERR,
						"a mailbox wasn't specified");
			}
			/*if (!database.isUserRegistered(argument)) {
				return new Response(Response.Type.ERR,
						"a mailbox wasn't found for " + argument);
			}*/
			return new Response(Response.Type.OK, "welcome "
					+ (username = argument));

		case PASS:
			if (!inState(State.AUTHORIZATION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during "
								+ State.AUTHORIZATION);
			}
			if (username == null) {
				return new Response(Response.Type.ERR,
						"a mailbox should be specifed before providing password");
			}
			if (!hasArguments) {
				return new Response(Response.Type.ERR,
						"a password wasn't provided");
			}/*
			if (!database.isUserValid(username, argument)) {
				return new Response(Response.Type.ERR,
						"the password provided is incorrect");
			}
			if (database.isMaildropLocked(username)) {
				return new Response(Response.Type.ERR,
						"the specified user's maildrop is currently locked");
			}
			if ((maildrop = database.retrieveMaildrop(username)) == null) {
				return new Response(Response.Type.ERR,
						"the database failed to retrieve the maildrop");
			}
			if (!database.setMaildropLocked(username, true)) {
				return new Response(Response.Type.ERR,
						"the database failed to set a lock on the maildrop");
			}*/
			holdingLock = true;
			enterState(State.TRANSACTION);
			int passMsgCount = client.getAllMessages().size();
			int passOctCount = 0;
                        for(com.bl4ckbird.ebitm.bitmessage.entities.Message m : client.getAllMessages()){
                            passOctCount += m.getMessage().length();
                        };
			return new Response(Response.Type.OK, username + "'s maildrop has "
					+ passMsgCount + " messages (" + passOctCount + " octets)");

		case NOOP:
			return new Response(Response.Type.OK);

		case STAT:
			if (!inState(State.TRANSACTION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during " + State.TRANSACTION);
			}
			int count = client.getAllMessages().size();
			int octets = 0;
                         for(com.bl4ckbird.ebitm.bitmessage.entities.Message m : client.getAllMessages()){
                            octets += m.getMessage().length();
                        };
			return new Response(Response.Type.OK, count + " " + octets);

		case LIST:
			if (!inState(State.TRANSACTION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during " + State.TRANSACTION);
			}

			List<com.bl4ckbird.ebitm.bitmessage.entities.Message> savedMessages5 = client.getAllMessages();
                         ArrayList<Message> savedMessages = new ArrayList<>();
                        for(int i = 0; i<savedMessages5.size(); i++){
                           com.bl4ckbird.ebitm.bitmessage.entities.Message m = savedMessages5.get(i);
                           Message newm = new Message(i + "",m.getFromAddress(),m.getToAddress(),m.getSubject(),m.getMessage(),false);
                           savedMessages.add(newm);
                           
                           
                        }

			if (!hasArguments) {
				int count0 = client.getAllMessages().size();
				int octets0 = 0;
                                for(com.bl4ckbird.ebitm.bitmessage.entities.Message m : client.getAllMessages()){
                                    octets0 += m.getMessage().length();
                                };

				Response listResponse = new Response(Response.Type.OK, count0
						+ " messages (" + octets0 + " octets)");
				int msgIndex = 0;
				for (Message m : savedMessages) {
					msgIndex++;
					if (!m.isDeleted()) {
						listResponse.appendExtraLine(msgIndex + " "
								+ m.getOctetWeight());
					}
				}
				return listResponse.appendTerminationLine();
			}

			int requestedMsg = 0;
			try {
				requestedMsg = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				return new Response(Response.Type.ERR, "number format error");
			}
			Message selectedMessage = null;
			if (requestedMsg > savedMessages.size() || requestedMsg < 0) {
				return new Response(Response.Type.ERR,
						"no such message, your maildrop contains "
								+ savedMessages.size() + " messages");
			}
			selectedMessage = savedMessages.get(requestedMsg - 1);
			if (selectedMessage == null || selectedMessage.isDeleted()) {
				return new Response(Response.Type.ERR,
						"no such message, your maildrop contains "
								+ savedMessages.size() + " messages");
			}
			return new Response(Response.Type.OK, requestedMsg + " "
					+ selectedMessage.getOctetWeight());

		case UIDL:
			if (!inState(State.TRANSACTION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during " + State.TRANSACTION);
			}

			List<com.bl4ckbird.ebitm.bitmessage.entities.Message> savedMessages2 = client.getAllMessages();
                         ArrayList<Message> savedMessages1 = new ArrayList<>();
                        for(int i = 0; i<savedMessages2.size(); i++){
                           com.bl4ckbird.ebitm.bitmessage.entities.Message m = savedMessages2.get(i);
                           Message newm = new Message(i + "",m.getFromAddress(),m.getToAddress(),m.getSubject(),m.getMessage(),false);
                           savedMessages1.add(newm);
                           
                           
                        }

			if (!hasArguments) {
				Response response = new Response(Response.Type.OK);
				int msgIndex = 0;
				for (Message m : savedMessages1) {
					msgIndex++;
					if (!m.isDeleted()) {
						response.appendExtraLine(msgIndex + " "
								+ m.getUniqueId());
					}
				}
				return response.appendTerminationLine();
			}

			int requestedMsg1 = 0;
			try {
				requestedMsg1 = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				return new Response(Response.Type.ERR, "number format error");
			}
			Message selectedMessage1 = null;
			if (requestedMsg1 > savedMessages1.size() || requestedMsg1 < 0) {
				return new Response(Response.Type.ERR,
						"no such message, your maildrop contains "
								+ savedMessages1.size() + " messages");
			}
			selectedMessage1 = savedMessages1.get(requestedMsg1 - 1);
			if (selectedMessage1 == null || selectedMessage1.isDeleted()) {
				return new Response(Response.Type.ERR,
						"no such message, your maildrop contains "
								+ savedMessages1.size() + " messages");
			}
			return new Response(Response.Type.OK, requestedMsg1 + " "
					+ selectedMessage1.getUniqueId());

		case RETR:
			if (!inState(State.TRANSACTION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during " + State.TRANSACTION);
			}
			if (!hasArguments) {
				return new Response(Response.Type.ERR,
						"a message was not specified");
			}
			Message selectedMessage0 = getMessageSelection(args[0]);
			if (selectedMessage0 == null || selectedMessage0.isDeleted()) {
				return new Response(Response.Type.ERR, "no such message");
			}
			String messageContent = selectedMessage0.getOctetString();
			int retrMessageWeight = selectedMessage0.getOctetWeight();
			Response retrResponse = new Response(Response.Type.OK);

			retrResponse.setResponse("message follows (" + retrMessageWeight
					+ " octets)");
			retrResponse.appendExtraLine(messageContent);
			return retrResponse.appendTerminationLine();

		case DELE:
			if (!inState(State.TRANSACTION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during " + State.TRANSACTION);
			}
			Message selectedMessage2 = getMessageSelection(args[0]);
			if (selectedMessage2 == null) {
				return new Response(Response.Type.ERR, "no such message");
			}
			if (selectedMessage2.isDeleted()) {
				return new Response(Response.Type.ERR,
						"message already deleted");
			}
			client.deleteMessage(Integer.parseInt(selectedMessage2.getUniqueId()));
			return new Response(Response.Type.OK, "marked message for delete");

		case RSET:
			if (!inState(State.TRANSACTION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during " + State.TRANSACTION);
			}
			
			int msgCount = 0;
			int octCount = 0;
			return new Response(Response.Type.OK, "maildrop has " + msgCount
					+ " (" + octCount + " octets)");

		case TOP:
			if (!inState(State.TRANSACTION)) {
				return new Response(Response.Type.ERR,
						"command can only be used during " + State.TRANSACTION);
			}
			if (!hasArguments || args.length != 2) {
				return new Response(Response.Type.ERR, "not enough arguments");
			}
			Message selectedMessageTop = getMessageSelection(args[0]);
			if (selectedMessageTop != null && !selectedMessageTop.isDeleted()) {
				int numLines;
				try {
					numLines = Integer.parseInt(args[1]);
				} catch (NumberFormatException nfe) {
					return new Response(Response.Type.ERR,
							"number format error on number of lines");
				}
				Response topResponse = new Response(Response.Type.OK);
				topResponse.appendExtraLine(selectedMessageTop.getHeader());
				String[] lines = selectedMessageTop.getBodyLines(numLines);
				for (String line : lines) {
					topResponse.appendExtraLine(line);
				}
				return topResponse.appendTerminationLine();
			}
			return new Response(Response.Type.ERR, "no such message");

		case CAPA:
			Response capaResponse = new Response(Response.Type.OK,
					"capability list follows");
			capaResponse.appendExtraLine("USER");
			capaResponse.appendExtraLine("UIDL");
			capaResponse.appendExtraLine("TOP");
			return capaResponse.appendTerminationLine();

		case QUIT:
			if (inState(State.TRANSACTION)) {
				enterState(State.UPDATE);
				processPendingOperations();
			}
			disconnectSuggested = true;

			if (maildrop != null) {
				
				logout();

				String stat = 0 == 0 ? "maildrop empty" : 0
						+ " messages left";
				return new Response(Response.Type.OK,
						"POP3 server logging out (" + stat + ")");
			}
			return new Response(Response.Type.OK, "POP3 server logging out");

		}
		return null;
	}
        
        private boolean logout(){
            maildrop = null;
            return true;
        }

	private boolean processPendingOperations() {
		return true;
	}

	private Message getMessageSelection(String arg) {
		List<com.bl4ckbird.ebitm.bitmessage.entities.Message> savedMessages2 = client.getAllMessages();
                         ArrayList<Message> savedMessages0 = new ArrayList<>();
                        for(int i = 0; i<savedMessages2.size(); i++){
                           com.bl4ckbird.ebitm.bitmessage.entities.Message m = savedMessages2.get(i);
                           Message newm = new Message(i + "",m.getFromAddress(),m.getToAddress(),m.getSubject(),m.getMessage(),false);
                           savedMessages0.add(newm);
                           
                           
                        }
		int requestedMsg0 = 0;
		try {
			requestedMsg0 = Integer.parseInt(arg);
		} catch (NumberFormatException nfe) {
			return null;
		}
		if (requestedMsg0 > savedMessages0.size() || requestedMsg0 < 1) {
			return null;
		}
		return savedMessages0.get(requestedMsg0 - 1); // index starts at zero
	}
   
	public String getWelcomeResponse() {
		return new Response(Response.Type.OK, "POP3 server ready").buildOutput();
	}

	public boolean isHoldingLock() {
		return holdingLock;
	}

	public boolean releaseLock() {
		
		return !holdingLock;
	}

	public boolean isDisconnectSuggested() {
		return disconnectSuggested;
	}
}
