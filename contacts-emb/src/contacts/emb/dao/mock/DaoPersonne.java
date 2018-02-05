package contacts.emb.dao.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import contacts.emb.dao.IDaoPersonne;
import contacts.emb.data.Personne;
import contacts.emb.data.Telephone;


public class DaoPersonne implements IDaoPersonne {
	
	
	// Champs
	
	private Donnees					donnees;
	private Map<Integer, Personne>	mapPersonnes;

	
	// Injecteurs
	
	public void setDonnees( Donnees donnees ) {
		this.donnees = donnees;
		mapPersonnes = donnees.getMapPersonnes();
	}

	
	// Actions
	
	@Override
	public int inserer(Personne personne) {
		if ( mapPersonnes.isEmpty() ) {
			personne.setId( 1 );
		} else {
			personne.setId( Collections.max( mapPersonnes.keySet() ) + 1 );
		}
		affecterIdTelephones(personne);
		mapPersonnes.put( personne.getId(), personne );
		return personne.getId();
	}

	@Override
	public void modifier(Personne personne) {
		affecterIdTelephones(personne);
		mapPersonnes.replace( personne.getId(), personne );
	}

	@Override
	public void supprimer(int idPersonne) {
		mapPersonnes.remove( idPersonne );
	}

	@Override
	public Personne retrouver(int idPersonne) {
		return mapPersonnes.get( idPersonne );
	}

	@Override
	public List<Personne> listerTout() {
		return  trierParNom( new ArrayList<>(mapPersonnes.values() ) );
	}
	
	
	// Méthodes auxiliaires
	
	private void affecterIdTelephones( Personne personne ) {
		for( Telephone t : personne.getTelephones() ) {
			if ( t.getId() == 0 ) {
				t.setId( donnees.getProchainIdTelephone() );
			}
		}
	}
    
    private List<Personne> trierParNom( List<Personne> liste ) {
		Collections.sort( liste,
	            (Comparator<Personne>) ( item1, item2) -> {
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
