package com.cooksys.assessment.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class People {
	private String _name;
	private String _dateConnected;
	private boolean _pendingMessage;
	private Message _messagePending;
	private boolean _hasBroadcast;
	private Message _pendingBroadcast;
	private int currentGame = -1;
	private Logger log = LoggerFactory.getLogger(People.class);

	
	public People(String name) {
		this._name = name;
		this._dateConnected = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now());
		this._pendingMessage = false;	
		this._hasBroadcast = false;
	}
	
	public void shift(int x) {
		currentGame = x > currentGame ? currentGame : currentGame - 1;
	}
	public int getGame() {
		log.info("Game of " + _name + "is being requested.. current value " + String.valueOf(currentGame));
		return currentGame;
	}
	
	public void addToGame(int game){
		if(currentGame == -1) {
			currentGame = game;
			log.info("Game of " + _name + " has been set to " + String.valueOf(game));
		}
	}
	
	public void removeFromGames() {
		log.info("Game of " + _name + " has been reset");
		currentGame = -1;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getJoinDate() {
		return _dateConnected;
	}
	
	public void sendMessage(Message m) {
		log.info("Message being sent to " + _name);
		_messagePending = m;
		_pendingMessage = true;
	}
	
	public boolean messagePending() {
		return _pendingMessage;
	}
	
	public Message readMessage() {
		log.info("Message has been read!");
		_pendingMessage = false;
		return _messagePending;
	}


	
	public boolean pendingBroadcast() {
		return _hasBroadcast;
	}
	
	public void sendBroadcast(Message m) {
		_pendingBroadcast = m;
		_hasBroadcast = true;
	}
	public Message getBroadcast() {
		_hasBroadcast = false;
		return _pendingBroadcast;
	}
}
