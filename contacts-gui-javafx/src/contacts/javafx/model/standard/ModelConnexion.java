package contacts.javafx.model.standard;

import java.util.logging.Level;
import java.util.logging.Logger;

import contacts.commun.dto.DtoCompte;
import contacts.commun.service.IServiceConnexion;
import contacts.commun.util.ExceptionValidation;
import contacts.javafx.data.Compte;
import contacts.javafx.data.mapper.IMapper;
import contacts.javafx.model.IModelConnexion;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


public class ModelConnexion implements IModelConnexion {
	
	// Logger
	private static final Logger logger = Logger.getLogger( ModelConnexion.class.getName() );
	
	
	// Données observables 
	
	// Vue connexion
	private final Compte		compteVue = new Compte();

	// Compte connecté
	private final ObjectProperty<Compte>	compteConnecté = new SimpleObjectProperty<>();

	
	// Autres champs
	private IMapper				mapper;
	private IServiceConnexion	serviceConnexion;
	

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
	
	
	// Injecteurs
	
	public void setMapper(IMapper mapper) {
		this.mapper = mapper;
	}

	public void setServiceConnexion(IServiceConnexion serviceConnexion) {
		this.serviceConnexion = serviceConnexion;

		compteVue.pseudoProperty().set( "geek" );
		compteVue.motDePasseProperty().set( "geek" );
	}
	
	
	// Actions


	@Override
	public void ouvrirSessionUtilisateur() throws ExceptionValidation {

		DtoCompte dto = serviceConnexion.sessionUtilisateurOuvrir(
				compteVue.pseudoProperty().get(), compteVue.motDePasseProperty().get() );
		
		// Message de log
		String logMessage;
		if( dto == null ) {
			logMessage = "Pseudo ou mot de passe invalide : " + compteVue.pseudoProperty().get() + " / " + compteVue.motDePasseProperty().get();
		} else {
			logMessage = "\n    Utilisateur connecté : " + dto.getId() +  " " + dto.getPseudo();
			logMessage += "\n    Roles :";
			for( String role : dto.getRoles() ) {
				logMessage += " " + role;
			}
		}
		logger.log( Level.CONFIG, logMessage );
		
		if( dto == null ) {
			throw new ExceptionValidation( "Pseudo ou mot de passe invalide." );
		} else {
			 Platform.runLater( () -> compteConnecté.set( mapper.map( dto ) ) );
		}
	}
	

	@Override
	public void fermerSessionUtilisateur() {
		serviceConnexion.sessionUtilisateurFermer();
		compteConnecté.set( null );
	}

}
