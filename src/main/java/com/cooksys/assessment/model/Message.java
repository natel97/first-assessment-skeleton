package com.cooksys.assessment.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

	private String username;
	private String command;
	private String contents;
	private String date;
	
	public Message(String username, String content) {
		setDate(null);
		this.username = username;
		this.contents = content;
	}
	public Message() {
		super();
	}
	
	private void setDate(String date) {
		this.date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		if (date == null)
			this.date = date;
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
