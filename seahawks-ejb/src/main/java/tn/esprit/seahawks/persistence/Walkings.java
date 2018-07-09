package tn.esprit.seahawks.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Walkings {
	private int WalkingId ;
	private String Description ;
	private Date DateDebut ;
	private Date DateFin ;
	private String breed;
	private int MaxPlace;
	private int EquipementNB;
	private String Image;

	
	private int DepartId;
	private int ArriveId;
	private int UserId;
	private int id = WalkingId ;
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getWalkingId() {
		return WalkingId;
	}
	public void setWalkingId(int walkingId) {
		WalkingId = walkingId;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public Date getDateDebut() {
		return DateDebut;
	}
	public void setDateDebut(Date dateDebut) {
		DateDebut = dateDebut;
	}
	public Date getDateFin() {
		return DateFin;
	}
	public void setDateFin(Date dateFin) {
		DateFin = dateFin;
	}
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	public int getMaxPlace() {
		return MaxPlace;
	}
	public void setMaxPlace(int maxPlace) {
		MaxPlace = maxPlace;
	}
	public int getEquipementNB() {
		return EquipementNB;
	}
	public void setEquipementNB(int equipementNB) {
		EquipementNB = equipementNB;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public int getDepartId() {
		return DepartId;
	}
	public void setDepartId(int departId) {
		DepartId = departId;
	}
	public int getArriveId() {
		return ArriveId;
	}
	public void setArriveId(int arriveId) {
		ArriveId = arriveId;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	

}
