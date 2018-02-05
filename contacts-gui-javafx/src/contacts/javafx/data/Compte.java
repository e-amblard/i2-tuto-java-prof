package contacts.javafx.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Compte  {

	
	// Données observables
	
	private final IntegerProperty	id			 = new SimpleIntegerProperty();
	private final StringProperty	pseudo		= new SimpleStringProperty();
	private final StringProperty	motDePasse	= new SimpleStringProperty();
	private final StringProperty	email 		= new SimpleStringProperty();
	
	private final ObservableList<String> roles = FXCollections.observableArrayList();
	
	
	// Getters et Setters

	public final IntegerProperty idProperty() {
		return this.id;
	}

	public final int getId() {
		return this.idProperty().get();
	}

	public final void setId(final int id) {
		this.idProperty().set(id);
	}

	public final StringProperty pseudoProperty() {
		return this.pseudo;
	}

	public final String getPseudo() {
		return this.pseudoProperty().get();
	}

	public final void setPseudo(final String pseudo) {
		this.pseudoProperty().set(pseudo);
	}

	public final StringProperty motDePasseProperty() {
		return this.motDePasse;
	}

	public final String getMotDePasse() {
		return this.motDePasseProperty().get();
	}

	public final void setMotDePasse(final String motDePasse) {
		this.motDePasseProperty().set(motDePasse);
	}

	public final StringProperty emailProperty() {
		return this.email;
	}

	public final String getEmail() {
		return this.emailProperty().get();
	}

	public final void setEmail(final String email) {
		this.emailProperty().set(email);
	}

	public final ObservableList<String> getRoles() {
		return this.roles;
	}

	
	public boolean isInRole( String role ) {
		
		if ( role != null ) {
			for ( String r : roles ) {
				if ( role.equals( r ) ) {
					return true;
				}
			}
		}
		return false;
	}

	
	// toString()
	
	@Override
	public String toString() {
		return getPseudo();
	}
	
	
	// Constructeurs
	
	public Compte() {
	}

	public Compte( int id, String pseudo, String motDePasse, String email ) {
		setId(id);
		setPseudo(pseudo);
		setMotDePasse(motDePasse);
		setEmail(email);
	}
	
}
