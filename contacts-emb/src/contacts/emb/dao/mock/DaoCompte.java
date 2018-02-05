package contacts.emb.dao.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import contacts.emb.dao.IDaoCompte;
import contacts.emb.data.Compte;


public class DaoCompte implements IDaoCompte {

	
	// Champs
	
	private Map<Integer, Compte>		mapComptes;
	
	
	// Injecteurs
	
	public void setDonnees( Donnees donnees ) {
		mapComptes = donnees.getMapComptes();
	}

	
	// Actions
	
	@Override
	public int inserer(Compte compte) {
        if (mapComptes.isEmpty()) {
            compte.setId(1);
        } else {
            compte.setId(Collections.max(mapComptes.keySet()) + 1);
        }
		mapComptes.put( compte.getId(), compte );
		return compte.getId();
	}

	@Override
	public void modifier(Compte compte) {
		mapComptes.replace( compte.getId(), compte );
	}

	@Override
	public void supprimer(int idCompte) {
		mapComptes.remove( idCompte );
	}

	@Override
	public Compte retrouver(int idCompte) {
		return mapComptes.get( idCompte );
	}

	@Override
	public List<Compte> listerTout() {
		return trierParPseudo( new ArrayList<>(mapComptes.values()) );
	}


	@Override
	public Compte validerAuthentification( String pseudo, String motDePasse )  {

		for ( Compte compte : mapComptes.values() ) {
			if ( compte.getPseudo().equals(pseudo) ) {
				if ( compte.getMotDePasse().equals(motDePasse) ) {
					return compte;
				}
				break;
			}
		}
		return null;
	}


	@Override
	public boolean verifierUnicitePseudo( String pseudo, int idCompte )  {
		
		for ( Compte compte : mapComptes.values() ) {
			if ( compte.getPseudo().equals(pseudo) ) {
				if ( compte.getId() != idCompte  ) {
					return false;
				}
			}
		}
		return true;
	}

	
	// Méthodes auxiliaires
    
    private List<Compte> trierParPseudo( List<Compte> liste ) {
		Collections.sort( liste,
	            (Comparator<Compte>) ( item1, item2) -> {
	                return ( item1.getPseudo().toUpperCase().compareTo( item2.getPseudo().toUpperCase() ) );
			});
    	return liste;
    }
	
	
}
