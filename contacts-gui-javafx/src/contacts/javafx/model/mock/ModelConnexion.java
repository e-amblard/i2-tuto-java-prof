package contacts.javafx.model.mock;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import contacts.commun.util.ExceptionValidation;
import contacts.javafx.data.Compte;
import contacts.javafx.model.IModelConnexion;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


public class ModelConnexion implements IModelConnexion {
	
	
	// Logger
	private static final Logger logger = Logger.getLogger( ModelConnexion.class.getName() );
	
	
	// Données observables 
	private final Compte         compteVue = new Compte();

	// Compte connecté
	private final ObjectProperty<Compte>	compteConnecté = new SimpleObjectProperty<>();

	
	// Autres champs
    private  Map<Integer, Compte>	mapComptes;
	
	
	// Injecteurs
	
	public void setDonnees(Donnees donnees) {
		mapComptes = donnees.getMapComptes();

		compteVue.pseudoProperty().set( "geek" );
		compteVue.motDePasseProperty().set( "geek" );
	}
	

	// Getters
	
	@Override
	public Compte getCompteVue() {
		return compteVue;
	}
	
	@Override
	public ObjectProperty<Compte> compteConnecteProperty() {
		return compteConnecté;
	}
	
	@Override
	public Compte getCompteConnecte() {
		return compteConnecté.get();
	}
	
	
	// Actions


	@Override
	public void ouvrirSessionUtilisateur() throws ExceptionValidation  {

		Compte compteTrouvé = null;
		
		for ( Compte compte : mapComptes.values() ) {
			if ( compte.getPseudo().equals(compteVue.getPseudo() )
					&& compte.getMotDePasse().equals( compteVue.getMotDePasse()) ) {
				compteTrouvé = compte;
			}
		}
		
		// Message de log
		String logMessage;
		if( compteTrouvé == null ) {
			logMessage = "Pseudo ou mot de passe invalide : " + compteVue.pseudoProperty().get() + " / " + compteVue.motDePasseProperty().get();
		} else {
			logMessage = "\n    Utilisateur connecté : " + compteTrouvé.getId() +  " " + compteTrouvé.getPseudo();
			logMessage += "\n    Roles :";
			for( String role : compteTrouvé.getRoles() ) {
				logMessage += " " + role;
			}
		}
		logger.log( Level.CONFIG, logMessage );
		
		if( compteTrouvé == null ) {
			throw new ExceptionValidation( "Pseudo ou mot de passe invalide." );
		} else {
			final Compte compte = compteTrouvé;
			Platform.runLater( () -> compteConnecté.set( compte ) );
		}
	}
	

	@Override
	public void fermerSessionUtilisateur()  {
		compteConnecté.set( null );
	}

}
