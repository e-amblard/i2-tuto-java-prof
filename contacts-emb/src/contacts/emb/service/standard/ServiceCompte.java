package contacts.emb.service.standard;

import java.util.ArrayList;
import java.util.List;

import contacts.commun.dto.DtoCompte;
import contacts.commun.service.IServiceCompte;
import contacts.commun.util.ExceptionValidation;
import contacts.emb.dao.IDaoCompte;
import contacts.emb.dao.IManagerTransaction;
import contacts.emb.data.Compte;
import contacts.emb.data.mapper.IMapper;
import contacts.emb.service.util.IManagerSecurite;
import contacts.emb.service.util.UtilServices;


public class ServiceCompte implements IServiceCompte {

	
	// Champs 

	private IManagerSecurite	managerSecurite;
	private	IManagerTransaction	managerTransaction;
	private IMapper				mapper;
	private IDaoCompte			daoCompte;
	
	
	// Injecteurs
	
	public void setManagerSecurite( IManagerSecurite managerSecurite ) {
		this.managerSecurite = managerSecurite;
	}
	
	public void setManagerTransaction( IManagerTransaction managerTransaction ) {
		this.managerTransaction = managerTransaction;
	}
	
	public void setMapper( IMapper mapper ) {
		this.mapper = mapper;
	}

	public void setDaoCompte( IDaoCompte daoCompte ) {
		this.daoCompte = daoCompte;
	}

	
	// Actions 

	@Override
	public int inserer( DtoCompte dtoCompte ) throws ExceptionValidation {
		
		try {
			
			managerSecurite.verifierAutorisationAdmin();
			verifierValiditeDonnees( dtoCompte );

			managerTransaction.begin();
			int id = daoCompte.inserer( mapper.map( dtoCompte ) );
			managerTransaction.commit();
			return id;

		} catch ( Exception e ) {
	    	managerTransaction.rollbackSiApplicable();
			throw UtilServices.exceptionValidationOuAnomalie(e);
		}
	}

	@Override
	public void modifier( DtoCompte dtoCompte ) throws ExceptionValidation { 
		try {

			managerSecurite.verifierAutorisationAdmin();
			verifierValiditeDonnees( dtoCompte );

			managerTransaction.begin();
			daoCompte.modifier( mapper.map( dtoCompte ) );
			managerTransaction.commit();

		} catch ( Exception e ) {
	    	managerTransaction.rollbackSiApplicable();
			throw UtilServices.exceptionValidationOuAnomalie(e);
		}
		
	}

	@Override
	public void supprimer( int idCompte ) throws ExceptionValidation  {
		try {
			
			managerSecurite.verifierAutorisationAdmin();
			if ( managerSecurite.getIdCompteConnecte() == idCompte ) {
				throw new ExceptionValidation("Vous ne pouvez pas supprimer le compte avec lequel vous êtes connecté !");
			}

			managerTransaction.begin();
			daoCompte.supprimer(idCompte);
			managerTransaction.commit();

		} catch ( Exception e ) {
	    	managerTransaction.rollbackSiApplicable();
			throw UtilServices.exceptionValidationOuAnomalie(e);
		}
	}

	
	@Override
	public DtoCompte retrouver( int idCompte ) {
		try {
			
			managerSecurite.verifierAutorisationAdmin();
			return mapper.map( daoCompte.retrouver(idCompte) );

		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomalie(e);
		}
	}

	
	@Override
	public List<DtoCompte> listerTout() {
		try {

			managerSecurite.verifierAutorisationAdmin();
			List<DtoCompte> liste = new ArrayList<>();
			for( Compte compte : daoCompte.listerTout() ) {
				liste.add( mapper.map( compte ) );
			}
			return liste;

		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomalie(e);
		}
	}
	
	
	// Méthodes auxiliaires
	
	private void verifierValiditeDonnees( DtoCompte dtoCompte ) throws ExceptionValidation {
		
		StringBuilder message = new StringBuilder();
		
		if ( dtoCompte.getPseudo() == null || dtoCompte.getPseudo().isEmpty() ) {
			message.append( "\nLe pseudo est absent." );
		} else 	if ( dtoCompte.getPseudo().length() < 3 ) {
			message.append( "\nLe pseudo est trop court." );
		} else 	if ( dtoCompte.getPseudo().length() > 25 ) {
			message.append( "\nLe pseudo est trop long." );
		} else 	if ( ! daoCompte.verifierUnicitePseudo( dtoCompte.getPseudo(), dtoCompte.getId() ) ) {
			message.append( "\nLe pseudo " + dtoCompte.getPseudo() + " est déjà utilisé." );
		}
		
		if ( dtoCompte.getMotDePasse() == null || dtoCompte.getMotDePasse().isEmpty() ) {
			message.append( "\nLe mot de passe est absent." );
		} else 	if ( dtoCompte.getMotDePasse().length() < 3 ) {
			message.append( "\nLe mot de passe est trop court." );
		} else 	if ( dtoCompte.getMotDePasse().length() > 25 ) {
			message.append( "\nLe mot de passe est trop long." );
		}
		
		if ( dtoCompte.getEmail() == null || dtoCompte.getEmail().isEmpty() ) {
			message.append( "\nL'adresse e-mail est absente." );
		}
		
		if ( message.length() > 0 ) {
			throw new ExceptionValidation( message.toString().substring(1) );
		}
	}
	
}
