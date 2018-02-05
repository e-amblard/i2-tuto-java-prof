package contacts.emb.service.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import contacts.commun.dto.DtoPersonne;
import contacts.commun.dto.DtoTelephone;
import contacts.commun.service.IServicePersonne;
import contacts.commun.util.ExceptionValidation;
import contacts.emb.service.util.IManagerSecurite;
import contacts.emb.service.util.UtilServices;


public class ServicePersonne implements IServicePersonne {

	
	// Champs 

	private Donnees						donnees;
	private Map<Integer, DtoPersonne>	mapPersonnes;
	
	private IManagerSecurite			managerSecurite;
	
	
	// Injecteurs
	
	public void setDonnees( Donnees donnees ) {
		this.donnees = donnees;
		mapPersonnes = donnees.getMapPersonnes();
	}
	
	public void setManagerSecurite(IManagerSecurite managerSecurite) {
		this.managerSecurite = managerSecurite;
	}
	

	// Actions 

	@Override
	public int inserer( DtoPersonne dtoPersonne ) throws ExceptionValidation {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			verifierValiditeDonnees( dtoPersonne );
			if ( mapPersonnes.isEmpty() ) {
				dtoPersonne.setId( 1 );
			} else {
				dtoPersonne.setId( Collections.max( mapPersonnes.keySet() ) + 1 );
			}
        	affecterIdTelephones(dtoPersonne);
			mapPersonnes.put( dtoPersonne.getId(), dtoPersonne );
			return dtoPersonne.getId();
		} catch ( Exception e ) {
			throw UtilServices.exceptionValidationOuAnomalie(e);
		}
	}

	@Override
	public void modifier( DtoPersonne dtoPersonne ) throws ExceptionValidation {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			verifierValiditeDonnees( dtoPersonne );
        	affecterIdTelephones(dtoPersonne);
			mapPersonnes.replace( dtoPersonne.getId(), dtoPersonne );
		} catch ( Exception e ) {
			throw UtilServices.exceptionValidationOuAnomalie(e);
		}
	}

	@Override
	public void supprimer( int idPersonne ) throws ExceptionValidation  {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			mapPersonnes.remove( idPersonne );
		} catch ( Exception e ) {
			throw UtilServices.exceptionValidationOuAnomalie(e);
		}
	}

	@Override
	public DtoPersonne retrouver( int idPersonne ) {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			return mapPersonnes.get( idPersonne );
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomalie(e);
		}
	}

	@Override
	public List<DtoPersonne> listerTout() {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			return trierParNom( new ArrayList<>(mapPersonnes.values()) );
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomalie(e);
		}
	}
	
	
	// Méthodes auxiliaires

	private void verifierValiditeDonnees( DtoPersonne dtoPersonne ) throws ExceptionValidation {
		
		StringBuilder message = new StringBuilder();
		
		if ( dtoPersonne.getNom() == null || dtoPersonne.getNom().isEmpty() ) {
			message.append( "\nLe nom est absent." );
		} else 	if ( dtoPersonne.getNom().length() > 25 ) {
			message.append( "\nLe nom est trop long." );
		}

		if ( dtoPersonne.getPrenom() == null || dtoPersonne.getPrenom().isEmpty() ) {
			message.append( "\nLe prénom est absent." );
		} else 	if ( dtoPersonne.getPrenom().length() > 25 ) {
			message.append( "\nLe prénom est trop long." );
		}
		
		for( DtoTelephone telephoe : dtoPersonne.getTelephones() ) {
			if ( telephoe.getLibelle() == null || telephoe.getLibelle().isEmpty() ) {
				message.append( "\nLlibellé absent pour le téléphone : " + telephoe.getNumero() );
			} else 	if ( telephoe.getLibelle().length() > 25 ) {
				message.append( "\nLe libellé du téléphone est trop lon : " + telephoe.getLibelle() );
			}
			
			if ( telephoe.getNumero() == null || telephoe.getNumero().isEmpty() ) {
				message.append( "\nNuméro absent pour le téléphone : " + telephoe.getLibelle() );
			} else 	if ( telephoe.getNumero().length() > 25 ) {
				message.append( "\nLe numéro du téléphone est trop lon : " + telephoe.getNumero() );
			}
		}
		
		if ( message.length() > 0 ) {
			throw new ExceptionValidation( message.toString().substring(1) );
		}
	}
    
	private void affecterIdTelephones( DtoPersonne personne ) {
		for( DtoTelephone t : personne.getTelephones() ) {
			if ( t.getId() == 0 ) {
				t.setId( donnees.getProchainIdTelephone() );
			}
		}
	}
    
    private List<DtoPersonne> trierParNom( List<DtoPersonne> liste ) {
		Collections.sort( liste,
	            (Comparator<DtoPersonne>) ( item1, item2) -> {
	            	int resultat = item1.getNom().toUpperCase().compareTo( item2.getNom().toUpperCase() );
	            	if ( resultat  != 0 ) {
	            		return resultat;
	            	} else {
		                return ( item1.getPrenom().toUpperCase().compareTo( item2.getPrenom().toUpperCase() ) );
	            	}
		});
    	return liste;
    }
	
}
