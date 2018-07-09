package tn.esprit.seahawks.persistence;

import java.io.Serializable;

import javax.persistence.*;

@Entity

public class Address implements Serializable{
	private int id;
	private String country;
	private String street;
	private String city;
	private String state;
	private String zip;
	private Double x;
	private Double y;
	
	public Address() {
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	
	
	
	public String getCountry() {
		return country;
	}


	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	
	public String getState() {
		return state;
	}


	public String getZip() {
		return zip;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}


	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public void setY(Double y) {
		this.y = y;
	}
	
	public void setState(String state) {
		this.state = state;
	}
		

}
