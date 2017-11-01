package com.cooksys.assessment.model;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class PeopleHelper {

	private List<People> people = new LinkedList<>();
	private Logger log = LoggerFactory.getLogger(PeopleHelper.class);

	
	public synchronized boolean checkForMessages(int id) {
		return(people.get(id).pendingBroadcast() || people.get(id).messagePending());
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
	
	public synchronized void sendBroadcast(Message m) {
		for(int x = 0; x < people.size(); x++) {
			people.get(x).sendBroadcast(m);
			log.info("Sending message to " + people.get(x).getName());
		}
	}
	
	public int addAUser(String name) {
		people.add(new People(name));
		int numToReturn = people.size() - 1;
		sendBroadcast(new Message(name, String.format("<%s>: %s has joined the party!", Message.getCurrentDate(),name)));
		return numToReturn;
	}
	
	private int findPerson(String name) {
		for(int x = 0; x < people.size(); x++) {
			if (people.get(x).getName().equals(name))
				return x;
		}
		return -1;
	}
	
}
