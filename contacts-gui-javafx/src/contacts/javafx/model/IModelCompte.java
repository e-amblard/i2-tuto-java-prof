package contacts.javafx.model;

import contacts.commun.util.ExceptionValidation;
import contacts.javafx.data.Compte;
import javafx.collections.ObservableList;


public interface IModelCompte {

	ObservableList<Compte> getComptes();

	Compte getCompteVue();
	
	void actualiserListe();

	void preparerAjouter();

	void preparerModifier(Compte compte);

	void validerMiseAJour() throws ExceptionValidation;

	void supprimer(Compte compte) throws ExceptionValidation;

}