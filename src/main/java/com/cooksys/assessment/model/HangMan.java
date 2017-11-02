package com.cooksys.assessment.model;

import java.util.LinkedList;
import java.util.List;

public class HangMan {
	
	private List<String> _people = new LinkedList<>();
	private int[] scores = new int[] {};
	private String _hint = "";
	private String _solution = "";
	private String _guesses = "";
	private String _misses = "";
	private String _progress = "";
	private String[] manParts = {"_", "_", "_", "|","o", "/", "|", "\\", "|", "/", "\\"};
	private String[] myParts = new String[] {"", "", "", "", "", "", "", "", "", "", "", ""};
	
	public void setHint(String hint){
		_hint = hint;
	}
	public void setSolution(String sol) {
		String build = "";
		for(int x = 0; x < sol.length(); x++) {
			if(sol.charAt(x) != ' ') {
				build += "_";
			}
			else {
				build += " ";
			}
		}
		_progress = build;
		_solution = sol;
	}
	public void addPerson(String person) {
		_people.add(person);
	}
	
	public List<String> getPeople() {
		return _people;
	}
	
	public boolean personIsInvited(String person) {
		return(_people.contains(person));
	}
	
	
	
	
	public boolean guess(String person, String a) {
		if(_guesses.contains(a))
			return false;
		if(_solution.contains(a)) {
			String prog = "";
			String[] solution = _solution.split("");
			String[] progress = _progress.split("");
			for(int x = 0; x < _solution.length(); x++) {
				if(solution[x].equals(a)) {
					prog += solution[x];
				}
				else if(solution[x].equals(" ")) {
					prog += " ";
				}
				else if (!progress[x].equals("_")) {
					prog += progress[x];
				}
				else
					prog += "_";
			}
			_progress = prog;
			_guesses += a;
		}
		else {
			_misses += a;
			_guesses += a;
		}
		return true;
	}
	public String redraw() {
		for(int x = 0; x < manParts.length; x++) {
			if(x < _misses.length())
				myParts[x] = manParts[x];
			else {
				myParts[x] = " ";
			}
		}
		
		return String.format("\n"
  + "   _%s%s%s\r\n" + 
	"  |    %s         \n" + 
	"  |    %s      Guessed : %s\n" + 
	"  |   %s%s%s      Missed : %s\n" + 
	"  |    %s\r\n" + 
	"  |    %s%s\r\n" + 
	" _|_\r\n" + 
	"|   |______\r\n" + 
	"|          |\r\n" + 
	"|__________|\r\n" + 
	" \n\nHint: %s \nStatus: %s", myParts[0], myParts[1], myParts[2], myParts[3], myParts[4], _guesses,  myParts[5], myParts[6], myParts[7], _misses, myParts[8], myParts[9], myParts[10], _hint, _progress);
		
	}
}
