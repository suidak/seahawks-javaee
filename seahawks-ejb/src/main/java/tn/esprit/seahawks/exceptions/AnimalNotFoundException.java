package tn.esprit.seahawks.exceptions;

public class AnimalNotFoundException extends Exception {
	
	public AnimalNotFoundException(){
		super("Animal Not Found!");
	}
}