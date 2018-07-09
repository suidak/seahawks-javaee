package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@XmlRootElement
public class Animal implements Serializable {
	private int id;
	private String sex;
	private int age;
	private double weight;
	private double height;
	private String specie;
	private String breed;
	private boolean isCastrated;
	private boolean isFostered;
	private boolean isLost;
	private String status;
	private User owner;
	//private String photo;
	private List<AnimalPhoto> photos;

	public Animal() {
		super();
	}


	public Animal(int id, String sex, int age, double weight, double height, String specie, String breed,
			boolean isCastrated, boolean isFostered, boolean isLost, String status, User owner, 
			List<AnimalPhoto> photo) {
		super();
		this.id = id;
		this.sex = sex;
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.specie = specie;
		this.breed = breed;
		this.isCastrated = isCastrated;
		this.isFostered = isFostered;
		this.isLost = isLost;
		this.status = status;
		this.owner = owner;
		this.photos = photo;
	}

	// getters

	
	
	//getters
	
	public Animal(int id) {
		super();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public String getSex() {
		return sex;
	}

	public int getAge() {
		return age;
	}

	public double getWeight() {
		return weight;
	}

	public double getHeight() {
		return height;
	}

	public String getSpecie() {
		return specie;
	}

	public String getBreed() {
		return breed;
	}

	public boolean isCastrated() {
		return isCastrated;
	}

	public boolean isFostered() {
		return isFostered;
	}

	public boolean isLost() {
		return isLost;
	}

	public String getStatus() {
		return status;
	}

	@ManyToOne(fetch=FetchType.EAGER )
	@XmlTransient
	public User getOwner() {
		return owner;
	}

	@OneToMany(mappedBy="animal", fetch=FetchType.EAGER)
	@JsonBackReference
	public List<AnimalPhoto> getPhoto() {
		return photos;
	}

	// setters
	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	public void setSex(String sex) {
		this.sex = sex;
	}

	@XmlElement
	public void setAge(int age) {
		this.age = age;
	}

	@XmlElement
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@XmlElement
	public void setHeight(double height) {
		this.height = height;
	}

	@XmlElement
	public void setSpecie(String specie) {
		this.specie = specie;
	}

	@XmlElement
	public void setBreed(String breed) {
		this.breed = breed;
	}

	@XmlElement
	public void setCastrated(boolean isCastrated) {
		this.isCastrated = isCastrated;
	}

	@XmlElement
	public void setFostered(boolean isFostered) {
		this.isFostered = isFostered;
	}

	@XmlElement
	public void setLost(boolean isLost) {
		this.isLost = isLost;
	}

	@XmlElement
	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((photos == null) ? 0 : photos.hashCode());
		result = prime * result + ((specie == null) ? 0 : specie.hashCode());
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Animal other = (Animal) obj;
		if (age != other.age)
			return false;
		if (photos == null) {
			if (other.photos != null)
				return false;
		} else if (!photos.equals(other.photos))
			return false;
		if (specie == null) {
			if (other.specie != null)
				return false;
		} else if (!specie.equals(other.specie))
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}
	
	@XmlElement
	public void setPhoto(List<AnimalPhoto> photo) {
		this.photos = photo;
	}
	
	
}
