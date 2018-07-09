package tn.esprit.seahawks.persistence;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement

@DiscriminatorValue("member")// edited by Sejir


public class Member extends User implements Serializable{
	private String firstName;
	private String lastName;
	
	public Member() {
		
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
