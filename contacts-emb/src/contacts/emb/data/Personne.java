package contacts.emb.data;

import java.util.ArrayList;
import java.util.List;


public class Personne {
	
	
	// Champs
	
	private int				id;
	
	private String			nom;
	
	private String			prenom;

	private List<Telephone>	telephones = new ArrayList<>();
	
	
	// Constructeurs
	
	public Personne() {
	}

	public Personne(int id, String nom, String prenom ) {
		setId(id);
		setNom(nom);
		setPrenom(prenom);
	}
	
	
	// Getters & setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public List<Telephone> getTelephones() {
		return telephones;
	}

	public void setTelephones(List<Telephone> telephones) {
		this.telephones = telephones;
	}

	
	// Actions
	
	public void ajouterTelephone( Telephone telephone ) {
		telephones.add( telephone );
	}
	
	public void retirerTelephone( Telephone telephone ) {
		telephones.remove(telephone);
	}
	
	
	// hashcode() + equals()
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Personne other = (Personne) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

}
