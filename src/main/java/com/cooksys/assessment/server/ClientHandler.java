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
	private int myLocation = -1;
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
						}
						log.info("<{}>: user <{}> connected",message.getDate(), message.getUsername());
						myLocation = peop.addAUser(message.getUsername());
						writer.flush();
						fwd = new MessageForwarder(peop,myLocation,mapper,writer);
						break;
					case "disconnect":
						log.info("<{}>: user <{}> disconnected", message.getDate(),message.getUsername());
						peop.removeUser(message.getUsername());
						peop.sendBroadcast(new Message(message.getUsername(), " has disconnected :("));
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
						log.info("<{}>: user <{}> echoed message <{}>",message.getDate(), message.getUsername(), message.getContents());
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
