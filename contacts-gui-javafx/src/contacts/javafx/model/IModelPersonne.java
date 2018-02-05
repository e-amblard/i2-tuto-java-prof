package contacts.javafx.model;

import contacts.commun.util.ExceptionValidation;
import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import javafx.collections.ObservableList;

public interface IModelPersonne {

	ObservableList<Personne> getPersonnes();

	Personne getPersonneVue();

	void refresh();

	void actualiserListe();

	void prepererAjouter();

	void preparerModifier( Personne personne );

	void validerMiseAJour() throws Exception;

	void supprimer( Personne personne ) throws ExceptionValidation;

	void ajouterTelphone();

	void supprimerTelephone( Telephone telephone );

}