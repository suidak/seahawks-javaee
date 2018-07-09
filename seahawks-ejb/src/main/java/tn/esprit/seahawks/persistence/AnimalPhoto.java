package tn.esprit.seahawks.persistence;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AnimalPhoto implements Serializable{
	
	private int id;
	private String photo;
	private Animal animal;
	public AnimalPhoto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public String getPhoto() {
		return photo;
	}
	
	@ManyToOne
	public Animal getAnimal() {
		return animal;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
}
