package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement
public class User implements Serializable {
	private int id;
	private String login; 
	private String email;
	private String password;
	private String photo;
	private String phoneNumber;
	private String token;
	private Date lastLogin;
	private AccountStatus status;
	private Address address;
	private String role;
	private boolean isVet;
	private List<Animal> animals;

	private static User user_instance;

	public static User getUser_instance() {
		if (user_instance == null)
			user_instance = new User();
		return user_instance;
	}
	
	public static void setUser_instance(User user){
		user_instance = new User(user);
	}
	
	public void Logout(){
		user_instance = null;
	}

	public User() {

	}

	public User(int id) {
		super();
		this.id = id;
	}
	
	public User(User u){
		this.id = u.id;
		this.login = u.login;
		this.email = u.email;
		this.password = u.password;
		this.photo = u.photo;
		this.phoneNumber = u.phoneNumber;
		this.token = u.token;
		this.role = u.role;
		this.address = u.address;
		this.status = u.status;
		this.lastLogin = u.lastLogin;
	}

	public User(String login, String password) {
		super();
		this.login = login;
		this.password = password;

	}
	
	

	public User(int id, String login, String email, String password, String photo, String phoneNumber, String token,
			Date lastLogin, AccountStatus status, Address address, String role) {
		this.id = id;
		this.login = login;
		this.email = email;
		this.password = password;
		this.photo = photo;
		this.phoneNumber = phoneNumber;
		this.token = token;
		this.lastLogin = lastLogin;
		this.status = status;
		this.address = address;
		this.role = role;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoto() {
		return photo;
	}
	

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getToken() {
		return token;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	@Column(columnDefinition = "varchar(32) default 'waitingForConfirmation' ")
	@Enumerated(EnumType.STRING)
	public AccountStatus getStatus() {
		return status;
	}

	@ManyToOne
	public Address getAddress() {
		return address;
	}

	public String getRole() {
		return role;
	}

	
	@OneToMany(mappedBy="owner", fetch=FetchType.EAGER)
	@JsonBackReference
	public List<Animal> getAnimals() {
		return animals;
	}
	
	
	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}

	public boolean isVet() {
		return isVet;
	}

	
	// setters

	
	

	public void setId(int id) {
		this.id = id;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public void setToken(String token) {
		this.token = token;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setVet(boolean isVet) {
		this.isVet = isVet;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", email=" + email + ", password=" + password + ", photo="
				+ photo + ", token=" + token + ", lastLogin=" + lastLogin + ", status=" + status + ", address="
				+ address + ", role=" + role + "]";
	}

	
}
