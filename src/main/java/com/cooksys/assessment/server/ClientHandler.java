package com.cooksys.assessment.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.assessment.model.People;
import com.cooksys.assessment.model.PeopleHelper;
import com.cooksys.assessment.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);
	public PeopleHelper peop;
	private Socket socket;

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
						log.info("<{}>: user <{}> connected",message.getDate(), message.getUsername());
						String conResp = mapper.writeValueAsString(new Message(message.getUsername(),String.format("<%s>: %s has joined the party!", message.getDate(),message.getUsername() )));
						writer.write(conResp);
						writer.flush();
						break;
					case "disconnect":
						log.info("<{}>: user <{}> disconnected", message.getDate(),message.getUsername());
						this.socket.close();
						break;
					case "broadcast":
						log.info("<{}>: user <{}> broadcasts ", message.getDate(), message.getUsername());
						break;
					case "@":
						log.info("<{}>: user <{}> says to one person",message.getDate(), message.getUsername());
						break;
					case "users":
						log.info("<{}>: Said users!", message.getDate());
						break;
					case "echo":
						log.info("<{}>: user <{}> echoed message <{}>",message.getDate(), message.getUsername(), message.getContents());
						String response = mapper.writeValueAsString(message);
						writer.write(response);
						writer.flush();
						break;
				}
			}

		} catch (IOException e) {
			log.error("Something went wrong :/", e);
		}
	}

}
