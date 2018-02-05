package contacts.emb.service.util;

import java.io.Serializable;

import contacts.commun.dto.DtoCompte;
import contacts.commun.util.ExceptionAutorisation;
import contacts.commun.util.Roles;


@SuppressWarnings("serial")
public class ManagerSecurite implements IManagerSecurite, Serializable {
	
	
	// Champs
	
	protected DtoCompte compteConnecté;

	
	// Getters & Setters
	
	@Override
	public void setCompteConnecté( DtoCompte compteConnecté ) {
		this.compteConnecté = compteConnecté;
	}
	

	// Actions 
	
	@Override
	public int getIdCompteConnecte() {
		return compteConnecté.getId();
	}

	// Vérifie que le compte connecté a le rôle utilisateur (ou à défaut administrateur)
	@Override
	public void verifierAutorisationUtilisateurOuAdmin() throws ExceptionAutorisation {
		if ( 
				compteConnecté == null
				|| (
						! compteConnecté.isInRole( Roles.UTILISATEUR )
						&& ! compteConnecté.isInRole( Roles.ADMINISTRATEUR ) 
				)
			) {
			throw new ExceptionAutorisation();
		}
	}

	// Vérifie que le compte connecté a le rôle administrateur
	@Override
	public void verifierAutorisationAdmin() throws ExceptionAutorisation {
		if ( 
				compteConnecté == null
				|| ! compteConnecté.isInRole( Roles.ADMINISTRATEUR )
			) {
			throw new ExceptionAutorisation();
		}
	}

	// Vérifie que le compte connecte, soit a le rôle administrateur
	// soit a comme identifiant celui passé en paramètre
	@Override
	public void verifierAutorisationAdminOuCompteConnecte( int idCompte ) throws ExceptionAutorisation {
		if ( 
				compteConnecté == null
				|| ( 
						! compteConnecté.isInRole( Roles.ADMINISTRATEUR )
						&& compteConnecté.getId() != idCompte
				)
			) {
			throw new ExceptionAutorisation();
		}
	}

}
