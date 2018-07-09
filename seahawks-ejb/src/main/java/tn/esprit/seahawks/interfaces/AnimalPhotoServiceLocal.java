package tn.esprit.seahawks.interfaces;

import java.util.List;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.AnimalPhoto;

@Local
public interface AnimalPhotoServiceLocal {
	public int addPhoto(int animalId, String photo);
	public boolean deletePhoto(int photoId);
	public List<AnimalPhoto> getAllAnimalPics();
	public List<AnimalPhoto> getAnimalPicsById(int id);
	public int addPhotoBase64(int animalId, String photo);
	public boolean deleteAnimalPhotos(int animalId);
	public List<AnimalPhoto> getPhotos(Animal animal);
	public List<AnimalPhoto> getAll();
}
