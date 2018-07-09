package tn.esprit.seahawks.beans;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

public class JsonConverter {
	
	public static ObjectNode mainNode(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode main = mapper.createObjectNode();

		return main;
	}
	
	public static int retrieveOwnerID(Animal a){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode animal = mapper.createObjectNode();
		
		return Integer.parseInt(animal.get("owner").toString());
	}
	
	public static ObjectNode convertUserToNode(User u){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode userNode = mapper.createObjectNode();
		
		userNode.put("id", u.getId());
		userNode.put("login", u.getLogin());
		userNode.put("password", u.getPassword());
		userNode.put("photo", u.getPhoto());
		
//		ArrayNode animals = mapper.createArrayNode();
//		
//		for (Animal a : u.getAnimals()){
//			ObjectNode animal = mapper.createObjectNode();
//			
//			animal.put("id", a.getId());
//			animal.put("sex", a.getSex());
//			animal.put("age", a.getAge());
//			animal.put("weight", a.getWeight());
//			animal.put("height", a.getHeight());
//			animal.put("specie", a.getSpecie());
//			animal.put("breed", a.getBreed());
//			animal.put("isCastrated", a.isCastrated());
//			animal.put("isFostered", a.isFostered());
//			animal.put("isLost", a.isLost());
//			animal.put("status", a.getStatus());
//			//maybe add owner here
//			
//			animals.add(animal);
//		}
//		
//		userNode.put("animals", animals);
		
		//userNode.put("animals", u.getAnimals());
		//userNode.put("address", u.getAddress());
		
		return userNode;
	}
	
	public static ObjectNode convertMemberToNode(Member m){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode memberNode = mapper.createObjectNode();
		
		memberNode.put("id", m.getId());
		memberNode.put("login", m.getLogin());
		memberNode.put("password", m.getPassword());
		memberNode.put("photo", m.getPhoto());
		memberNode.put("firstName", m.getFirstName());
		memberNode.put("lastName", m.getLastName());
		//memberNode.put("animals", m.getAnimals());
		//memberNode.put("address", m.getAddress());
		
		return memberNode;
	}
	
	public static String convertUser(User u){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode main = mapper.createObjectNode();
		
		main.put("id", u.getId());
		main.put("login", u.getLogin());
		main.put("password", u.getPassword());
		main.put("photo", u.getPhoto());
		main.put("email", u.getEmail());
		main.put("password", u.getPassword());
		
		
		
		
		return main.toString();
	}
	
	public static String convertAnimal(Animal a){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode main = mapper.createObjectNode();
		
		main.put("id", a.getId());
		main.put("sex", a.getSex());
		main.put("age", a.getAge());
		main.put("weight", a.getWeight());
		main.put("height", a.getHeight());
		main.put("specie", a.getSpecie());
		main.put("breed", a.getBreed());
		main.put("isCastrated", a.isCastrated());
		main.put("isFostered", a.isFostered());
		main.put("isLost", a.isLost());
		main.put("status", a.getStatus());
		main.put("owner", convertUserToNode(a.getOwner()));
		
		return main.toString();
	}
	
	public static String convertBreedingOffer(BreedingOffer b){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode main = mapper.createObjectNode();
		
		main.put("id", b.getId());
		main.put("titre", b.getTitre());
		main.put("description", b.getDescription());
		
		return main.toString();
	}
	

	public static String convertAddress(Address a){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode main = mapper.createObjectNode();
		
		main.put("id", a.getId());
		main.put("city", a.getCity());
		main.put("country", a.getCountry());
		main.put("street", a.getStreet());
		main.put("zip", a.getZip());
		
		
		return main.toString();
}
	public static String convertBreedingOfferList(List<BreedingOffer> list){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode main = mapper.createObjectNode();
	
		Iterator<BreedingOffer> it = list.iterator();
		
		while(it.hasNext()){
		main.put("id", it.next().getId());
		main.put("titre", it.next().getTitre());
		main.put("description", it.next().getDescription());
	
		}
           return main.toString();

	}
}
