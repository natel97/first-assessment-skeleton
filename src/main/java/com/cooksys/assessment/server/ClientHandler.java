package com.cooksys.assessment.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.assessment.MessageForwarder;
import com.cooksys.assessment.model.Message;
import com.cooksys.assessment.model.PeopleHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);
	public PeopleHelper peop;
	private Socket socket;
	private String myName = "nobody";
	private MessageForwarder fwd;

	public ClientHandler(Socket socket, PeopleHelper people) {
		super();
		this.socket = socket;
		this.peop = people;
	}

	public void run() {
		try {

			ObjectMapper mapper = new ObjectMapper();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			while (!socket.isClosed()) {					
				String raw = reader.readLine();
				Message message = mapper.readValue(raw, Message.class);
				switch (message.getCommand()) {
					case "connect":
						if(peop.findPerson(message.getUsername()) != -1) {
							
							writer.write(mapper.writeValueAsString(new Message("System", "The server already contains a user named " + message.getUsername() + "! Please connect with a different username!")));
							writer.flush();
							socket.close();
							break;
						}
						log.info("<{}>: user <{}> connected",message.getDate(), message.getUsername());
						myName =message.getUsername();
						peop.addAUser(myName);
						writer.flush();
						fwd = new MessageForwarder(peop,myName,mapper,writer);
						break;
					case "disconnect":
						log.info("<{}>: user <{}> disconnected", message.getDate(),message.getUsername());
						peop.sendBroadcast(new Message(message.getUsername(), message.getUsername() + " has disconnected :("));
						peop.removeUser(message.getUsername());
						this.socket.close();
						fwd.stopMe();
						break;
					case "broadcast":
						peop.sendBroadcast(message);
						log.info("<{}>: user <{}> broadcasts ", message.getDate(), message.getUsername());
						break;
					case "at":
						log.info("<{}>: user <{}> says to <{}>",message.getDate(), message.getUsername(),message.getContents().split(" ")[0]);
						Message a = new Message(message.getUsername(),message.getContents().replaceAll(message.getContents().split(" ")[0], ""));
						a.setCommand("at");
						peop.sendMessageToOne(a, message.getContents().split(" ")[0]);
						writer.flush();
						break;
					case "users":
						log.info("<{}>: Said users!", message.getDate());
						peop.sendMessage(new Message(message.getUsername(),peop.getUsers()), message.getUsername());
						writer.flush();
						break;
					case "echo":
						if(message.getContents().equals("end game")) {
							peop.resetGameForUser(message.getUsername());
							peop.sendMessage(new Message("GameManager", "You have been successfully removed from the game!"), message.getUsername());
						}
						log.info("<{}>: user <{}> echoed message <{}>",message.getDate(), message.getUsername(), message.getContents());
						if(message.getContents().startsWith("Hang Man")){
							Message errMSG = new Message("HangMan System", "Please format your hangman starting message in the following format: HangMan HM My Answer HM My Hint HM People to invite separated by a space");
							String[] contents = message.getContents().split(" HM ");
							if(!contents[0].equals("Hang Man") || contents.length != 4) {
								peop.sendMessage(errMSG, message.getUsername());
								writer.flush();
								break;
							}
							peop.setGameDetails(peop.addGame(message.getUsername()), contents[2], contents[1], contents[3].split(" "));
						}
						
						if(message.getContents().startsWith("guess ") && peop.getGame(message.getUsername())!= -1  && message.getContents().length() > 6) {
							if(peop.guessLetter(message.getContents().split(" ")[1], message.getUsername(), peop.getGame(message.getUsername()))) {
								peop.sendMessage(new Message("HangMan", "You have successfully guessed!"), message.getUsername());
								log.info(message.getUsername() + " has made a guess... he guessed " + message.getContents().split(" ")[1]);
							}
								else {
									peop.sendMessage(new Message("HangMan", "Your letter was already guessed!"), message.getUsername());
									log.info(message.getUsername() + " guessed for a letter that was already guessed! hahaha");
								}
							log.info("Sending request to refresh game!");
							peop.redraw(peop.getGame(message.getUsername()));
							writer.flush();
							break;
							}
						peop.sendMessage(message, message.getUsername());
						writer.flush();
						break;
				}
			}

		} catch (IOException e) {
			log.error("Something went wrong :/", e);
			fwd.stopMe();
		}
	}

}
