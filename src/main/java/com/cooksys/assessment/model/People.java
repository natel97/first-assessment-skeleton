package com.cooksys.assessment.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class People {
	private int _ID;
	private String _name;
	private String _dateConnected;
	private boolean _pendingMessage;
	private Message _messagePending;
	private boolean _hasBroadcast;
	private Message _pendingBroadcast;
	private String _lastCommand;
	
	public People(String name, int ID) {
		this._name = name;
		this._dateConnected = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now());
		this._pendingMessage = false;	
		this._hasBroadcast = false;
		this._ID = ID;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getJoinDate() {
		return _dateConnected;
	}
	
	public void sendMessage(Message m) {
		_messagePending = m;
		_pendingMessage = true;
	}
	
	public boolean messagePending() {
		return _pendingMessage;
	}
	
	public Message readMessage() {
		_pendingMessage = false;
		return _messagePending;
	}
	
	public void setLastCommand(String cmd) {
		_lastCommand = cmd;
	}
	
	public String getLastCommand() {
		return _lastCommand;
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
	public int getID() {
		return _ID;
	}
}
