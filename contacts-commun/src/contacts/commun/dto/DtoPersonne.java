package contacts.commun.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class DtoPersonne implements Serializable {
	
	
	// Champs
	
	private int				id;
	
	private String			nom;
	
	private String			prenom;
	
	private List<DtoTelephone>	telephones = new ArrayList<>();
	
	
	// Constructeurs
	
	public DtoPersonne() {
	}

	public DtoPersonne(int id, String nom, String prenom ) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
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

	public List<DtoTelephone> getTelephones() {
		return telephones;
	}

	public void setTelephones(List<DtoTelephone> telephones) {
		this.telephones = telephones;
	}

}
