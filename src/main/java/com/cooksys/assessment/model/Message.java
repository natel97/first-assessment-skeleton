package com.cooksys.assessment.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

	private String username;
	private String command;
	private String contents;
	private String date;
	
	public Message(String username, String content) {
		this.date = getCurrentDate();
		this.username = username;
		this.contents = content;
		this.command = "users";
	}
	public Message() {
		super();
	}
	
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
	}

	public String getDate() {
		return date;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
