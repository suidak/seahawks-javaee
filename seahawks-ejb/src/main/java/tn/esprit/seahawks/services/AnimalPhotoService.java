package tn.esprit.seahawks.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.seahawks.interfaces.AnimalPhotoServiceLocal;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.AnimalPhoto;
import tn.esprit.seahawks.persistence.BreedingOffer;

@Stateless
public class AnimalPhotoService implements AnimalPhotoServiceLocal{
	
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public int addPhoto(int animalId, String photo) {
		try {
			Animal animal = em.find(Animal.class, animalId);
			AnimalPhoto pic = new AnimalPhoto();
			pic.setPhoto(photo);
			pic.setAnimal(animal);
			
			em.persist(pic);
			em.flush();
			em.refresh(pic);
			return pic.getId();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public boolean deletePhoto(int photoId) {
		AnimalPhoto p = em.find(AnimalPhoto.class, photoId);

		try {
			String dbPath = p.getPhoto();
			String fileLocation = "D:\\ESPRIT\\4TWIN2\\Seahawks\\javaee\\seahawks-web\\src\\main"
					+ "\\webapp\\" + dbPath;
			Path path = Paths.get(fileLocation);
			
			try {
			    Files.delete(path);
			} catch (NoSuchFileException x) {
			    System.err.format("%s: no such" + " file or directory%n", path);
			    return false;
			} catch (DirectoryNotEmptyException x) {
			    System.err.format("%s not empty%n", path);
			    return false;
			} catch (IOException x) {
			    // File permission problems are caught here.
			    System.err.println(x);
			    return false;
			}
			
			em.remove(p);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<AnimalPhoto> getPhotos(Animal animal) {
		Query query = em.createQuery("select ap from AnimalPhoto ap join ap.animal a  where a.id=:id");
		query.setParameter("id", animal.getId() );
		
		return query.getResultList();
	}

	@Override
	public List<AnimalPhoto> getAll() {
		Query query = em.createQuery("select ap from AnimalPhoto ap ");	
		return query.getResultList();
		
	}
	
	
	
	@Override
	public boolean deleteAnimalPhotos(int animalId){
		try {
			List<AnimalPhoto> pics = em.createQuery("select p from AnimalPhoto p "
					+ "where p.animal.id=:id", AnimalPhoto.class)
					.setParameter("id", animalId)
					.getResultList();
			
			for (AnimalPhoto p : pics){
				em.find(AnimalPhoto.class, p.getId());
				String dbPath = p.getPhoto();
				String fileLocation = "C:\\wamp64\\www\\" +  dbPath;
				Path path = Paths.get(fileLocation);
				
				try {
				    Files.delete(path);
				} catch (NoSuchFileException x) {
				    System.err.format("%s: no such" + " file or directory%n", path);
				    return false;
				} catch (DirectoryNotEmptyException x) {
				    System.err.format("%s not empty%n", path);
				    return false;
				} catch (IOException x) {
				    // File permission problems are caught here.
				    System.err.println(x);
				    return false;
				}
				
				em.remove(p);
			}
			
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<AnimalPhoto> getAllAnimalPics(){
		return em.createQuery("select a from AnimalPhoto a", AnimalPhoto.class).getResultList();
	}
	
	@Override
	public List<AnimalPhoto> getAnimalPicsById(int id){
		Animal a = em.find(Animal.class, id);
		return em.createQuery("select a from AnimalPhoto a where a.animal=:animal", AnimalPhoto.class)
				.setParameter("animal", a)
				.getResultList();
	}
	
	@Override
	public int addPhotoBase64(int animalId, String photo) {
		try {
			Animal animal = em.find(Animal.class, animalId);
			AnimalPhoto pic = new AnimalPhoto();
			
			pic.setPhoto(saveImage64(photo));
			pic.setAnimal(animal);
			
			em.persist(pic);
			em.flush();
			em.refresh(pic);
			return pic.getId();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}
	
	public String saveImage64(String base64) {

		String temp = "assets\\animals\\" + UUID.randomUUID().toString() + ".jpeg";
		
		String fileLocation = "C:\\xampp\\htdocs\\" + temp;
		//C:\wamp64\www\images
		try (FileOutputStream imageOutFile = new FileOutputStream(fileLocation)) {
			// Converting a Base64 String into Image byte array
			byte[] imageByteArray = Base64.getDecoder().decode(base64);
			imageOutFile.write(imageByteArray);
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return temp;
	}
	
}
