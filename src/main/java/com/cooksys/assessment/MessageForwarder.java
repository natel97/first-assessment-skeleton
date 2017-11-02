package com.cooksys.assessment;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.assessment.model.Message;
import com.cooksys.assessment.model.PeopleHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageForwarder implements Runnable{
	
	private boolean keepGoing;
	private PeopleHelper people;
	String myChild;
	PrintWriter writer;
	Thread myThread;

	private Logger log = LoggerFactory.getLogger(MessageForwarder.class);
	ObjectMapper mapper;
	
	public MessageForwarder(PeopleHelper h, String name, ObjectMapper o, PrintWriter p) {
		keepGoing = true;
		people = h;
		myChild = name;
		mapper = o;
		writer = p;
		this.myThread = new Thread(this);
		myThread.start();
		
	}
	
	public void stopMe() {
		keepGoing = false;
		log.info("Oh no! I have been told to stop");
	}

	@Override
	public void run() {
				
		while(keepGoing) {
			if (people.checkForMessages(people.findPerson(myChild))){
				if(people.findPerson(myChild) == -1)
					break;
				Message message = people.getMessages(people.findPerson(myChild));
				String m;
				
				try {
					m = mapper.writeValueAsString(message);
					writer.write(m);
					writer.flush();
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		log.info("I have been stopped successfully!");
		
		
	}

}
