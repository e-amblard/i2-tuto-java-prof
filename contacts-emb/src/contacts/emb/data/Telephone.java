package contacts.emb.data;


public class Telephone {

	
	// Champs

	private int				id;
	
	private Personne		personne;

	private String			libelle;

	private String			numero;
	
	
	// Constructeurs
	
	public Telephone() {
	}
	
	public Telephone(int id, Personne personne, String libelle, String numero) {
		this.id = id;
		this.personne = personne;
		this.libelle = libelle;
		this.numero = numero;
	}


	// Getters & setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Personne getPersonne() {
		return personne;
	}

	public void setPersonne(Personne personne) {
		this.personne = personne;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	
	// hashcode() et equals()
	
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
		Telephone other = (Telephone) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
