package com.cooksys.assessment.model;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class PeopleHelper {

	private List<People> people = new LinkedList<>();
	private Logger log = LoggerFactory.getLogger(PeopleHelper.class);
	private List<HangMan> games = new LinkedList<>();

	
	public synchronized boolean checkForMessages(int id) {
		return(people.get(id).pendingBroadcast() || people.get(id).messagePending());
	}
	
	public synchronized void addGame(String user) {
		HangMan h = new HangMan();
		h.addPerson(user);
		games.add(h);
	}
	
	public boolean guessLetter(String letter, String name, int game) {
		return games.get(game).guess(name, letter);
	}
	
	public synchronized Message getMessages(int id) {
		return 	people.get(id).messagePending() ? 
					people.get(id).readMessage() : 
					people.get(id).pendingBroadcast() ?
							people.get(id).getBroadcast() : null;
	}
	
	public synchronized boolean sendMessage(Message m, String to) {
		int id = findPerson(to);
		if(id == -1)
			return false;
		people.get(id).sendMessage(m);
		return true;
	}
	
	public void sendMessageToOne(Message m, String to) {
		if(!sendMessage(m,to)) {
			sendMessage(new Message("System","Your message to " + to + " failed! No such user exists!"),m.getUsername());
		}
	}
	public synchronized void sendBroadcast(Message m) {
		for(int x = 0; x < people.size(); x++) {
			people.get(x).sendBroadcast(m);
			log.info("Sending message to " + people.get(x).getName());
		}
	}
	
	public int addAUser(String name) {
		people.add(new People(name));
		int numToReturn = people.size() - 1;
		sendBroadcast(new Message("Server", String.format("<%s> has joined the party!",name)));
		return numToReturn;
	}
	
	public String getUsers() {
		String build = "List of users connected as of " + Message.getCurrentDate() + "\n";
		for(int x = 0; x < people.size(); x++)
			build += people.get(x).getName() + "\n";
		return build;
	}
	
	public int findPerson(String name) {
		for(int x = 0; x < people.size(); x++) {
			if (people.get(x).getName().equals(name))
				return x;
		}
		return -1;
	}
	public void removeUser(String name) {
		people.remove(findPerson(name));
	}
	
}
