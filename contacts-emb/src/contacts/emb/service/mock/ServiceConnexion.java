package contacts.emb.service.mock;

import java.util.Map;

import contacts.commun.dto.DtoCompte;
import contacts.commun.service.IServiceConnexion;
import contacts.emb.service.util.IManagerSecurite;
import contacts.emb.service.util.UtilServices;


public class ServiceConnexion implements IServiceConnexion {
	
	
	// Champs 

	private IManagerSecurite			managerSecurite;

	private  Map<Integer, DtoCompte>	mapComptes;
	
	
	// Injecteurs
	
	public void setManagerSecurite( IManagerSecurite managerSecurite ) {
		this.managerSecurite = managerSecurite;
	}
	
	public void setDonnees( Donnees donnees ) {
		mapComptes = donnees.getMapComptes();
	}
	
	
	// Actions

	@Override
	public DtoCompte sessionUtilisateurOuvrir( String pseudo, String motDePasse ) {
		try {
			DtoCompte	compteConnecté = null;
	        for (DtoCompte compte : mapComptes.values()) {
	            if (compte.getPseudo().equals(pseudo)) {
	                if (compte.getMotDePasse().equals(motDePasse)) {
	                	compteConnecté = compte;
	                }
	                break;
	            }
	        }
			managerSecurite.setCompteConnecté( compteConnecté );
			return compteConnecté;
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomalie(e);
		}
	}


	@Override
	public void sessionUtilisateurFermer() {
		try {
			managerSecurite.setCompteConnecté( null );
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomalie(e);
		}
	}

}
