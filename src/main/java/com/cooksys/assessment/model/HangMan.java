package com.cooksys.assessment.model;

import java.util.LinkedList;
import java.util.List;

public class HangMan {
	
	List<String> _people = new LinkedList<>();
	int[] scores;
	String _hint;
	String _solution;
	String _guesses;
	String _misses;
	String _progress;
	String[] manParts = {"_", "_", "_", "|","o", "/", "|", "\\", "|", "/", "\\"};
	String[] myParts;
	
	public void setHint(String hint){
		_hint = hint;
	}
	public void setSolution(String sol) {
		String build = "";
		for(int x = 0; x < sol.length(); x++) {
			if(sol.charAt(x) != ' ') {
				build += "_";
			}
		}
		_progress = build;
		_solution = sol;
	}
	public void addPerson(String person) {
		_people.add(person);
	}
	
	public boolean personIsInvited(String person) {
		return(_people.contains(person));
	}
	
	
	
	
	public boolean guess(String person, String a) {
		if(_guesses.contains(a))
			return false;
		if(_solution.contains(a)) {
			String[] solution = _solution.split("");
			String[] progress = _progress.split("");
			for(int x = 0; x < _solution.length(); x++) {
				if(solution[x] == a) {
					progress[x] = a;
				}
			}
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
		
		return String.format(""
  + "   _%s%s%s\r\n" + 
	"  |    %s         \n" + 
	"  |    %s      Guessed : %s\n" + 
	"  |   $s%s%s     \n" + 
	"  |    %s\r\n" + 
	"  |   %s%s\r\n" + 
	" _|_\r\n" + 
	"|   |______\r\n" + 
	"|          |\r\n" + 
	"|__________|\r\n" + 
	" \n\nHint: %s \nStatus: %s", _guesses, myParts[0], myParts[1], myParts[2], myParts[3], myParts[4], _guesses, myParts[5], myParts[6], myParts[7], myParts[8], myParts[9], myParts[10], _hint, _progress);
		
	}
}
