package contacts.emb.service.standard;

import contacts.commun.dto.DtoCompte;
import contacts.commun.service.IServiceConnexion;
import contacts.emb.dao.IDaoCompte;
import contacts.emb.data.mapper.IMapper;
import contacts.emb.service.util.IManagerSecurite;
import contacts.emb.service.util.UtilServices;


public class ServiceConnexion implements IServiceConnexion {
	
	
	// Champs 

	private IManagerSecurite	managerSecurite;
	private IMapper				mapper;
	private IDaoCompte			daoCompte;
	
	
	// Injecteurs
	
	public void setMapper( IMapper mapper ) {
		this.mapper = mapper;
	}

	public void setDaoCompte( IDaoCompte daoCompte ) {
		this.daoCompte = daoCompte;
	}
	
	public void setManagerSecurite( IManagerSecurite managerSecurite ) {
		this.managerSecurite = managerSecurite;
	}
	
	
	// Actions

	@Override
	public DtoCompte sessionUtilisateurOuvrir( String pseudo, String motDePasse ) {
		try {
			DtoCompte compte = mapper.map( daoCompte.validerAuthentification(pseudo, motDePasse) );
			managerSecurite.setCompteConnecté( compte );
			return compte;
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
