package tn.esprit.seahawks.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

@Local
public interface AnimalServiceLocal {

	public int addAnimal(Animal a);
	public boolean deleteAnimal(int id);
	public boolean updateAnimal(Animal a);
	public List<Animal> getAllAnimals();
	public List<Animal> getUserAnimals(int id);
	public List<Animal> getUserUnlistedAnimals(int id);
	public List<Animal> getAnimalsBySpecie(String specie);
	public List<Animal> getAnimalsBySpecieAndBreed(String specie, String breed);
	public long averageAnimalsNbrPerUser();
	public long nbrOfAnimalsPerSpecie(String specie);
	public long nbrOfAnimalsPerSpecieBreed(String specie, String breed);
	public Map<String, Long> nbrOfAdoptionsPerSpecie();
	public Map<String, Long> getMostAdoptedAnimal();
	public List<Animal> getAnimalsByUser(Member member) ;
	public int lastIndex();
	List<Animal> getAnimalsByUser(User u);
	public List<Animal> getAnimalsPerMember(int memberid);
	public Animal getSingleAnimal(int id);
}