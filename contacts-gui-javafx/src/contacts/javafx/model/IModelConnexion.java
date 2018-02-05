package contacts.javafx.model;

import contacts.commun.util.ExceptionValidation;
import contacts.javafx.data.Compte;
import javafx.beans.property.ObjectProperty;


public interface IModelConnexion {

	Compte getCompteVue();

	ObjectProperty<Compte> compteConnecteProperty();

	Compte getCompteConnecte();

	void ouvrirSessionUtilisateur() throws ExceptionValidation;

	void fermerSessionUtilisateur();

}