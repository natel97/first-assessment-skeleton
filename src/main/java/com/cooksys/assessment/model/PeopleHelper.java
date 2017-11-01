package com.cooksys.assessment.model;

import java.util.LinkedList;
import java.util.List;

public class PeopleHelper {

	private List<People> people = new LinkedList<>();
	
	
	public Message getMessages(People currentPerson) {
		int id = findPerson(currentPerson, true);
		return id == -1 ? null : 
			people.get(id).messagePending() ? 
					people.get(id).readMessage() : 
					people.get(id).pendingBroadcast() ?
							people.get(id).getBroadcast() : null;
	}
	
	public boolean sendMessage(Message m, People to) {
		int id = findPerson(to, false);
		if(id == -1)
			return false;
		people.get(id).sendMessage(m);
		return true;
	}
	
	public void sendBroadcast(Message m) {
		for(int x = 0; x < people.size(); x++) {
			people.get(x).sendBroadcast(m);
		}
	}
	
	private int findPerson(People p, boolean byID) {
		for(int x = 0; x < people.size(); x++) {
			if (byID ? (people.get(x).getID() == p.getID()) : (people.get(x).getName().equals(p.getName())))
				return x;
		}
		return -1;
	}
	
}
