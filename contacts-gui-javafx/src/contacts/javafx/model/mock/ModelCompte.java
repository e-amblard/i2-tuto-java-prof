package contacts.javafx.model.mock;


import static contacts.javafx.model.EnumModeVue.CREER;
import static contacts.javafx.model.EnumModeVue.MODIFIER;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import contacts.javafx.data.Compte;
import contacts.javafx.data.mapper.IMapper;
import contacts.javafx.model.EnumModeVue;
import contacts.javafx.model.IModelCompte;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ModelCompte implements IModelCompte {
	
	
	// Données pour les vues 
	
	private final ObservableList<Compte> comptes = FXCollections.observableArrayList( 
			c ->  new Observable[] { c.pseudoProperty()  } 
		);
	
	private final Compte		compteVue = new Compte();
	
	
	// Objet courant

	private Compte				compteCourant;
    private EnumModeVue			modeVue;
	
	
	// Autres champs
    private  Map<Integer, Compte>	mapComptes;
	private IMapper					mapper;
	
	
	// Getters 
	
	@Override
	public ObservableList<Compte> getComptes() {
		return comptes;
	}

	@Override
	public Compte getCompteVue() {
		return compteVue;
	}
	
	
	// Injecteurs
	
	public void setDonnees(Donnees donnees) {
		mapComptes = donnees.getMapComptes();
	}
	
	public void setMapper(IMapper mapper) {
		this.mapper = mapper;
	}
	
	
	// Actualisations
	
	@Override
	public void actualiserListe()  {
        comptes.clear();
        for ( Compte compte : mapComptes.values() ) {
        	comptes.add( compte );
        }
        trierListe();
 	}
	
	
	// Actions
	
	@Override
	public void preparerAjouter() {
        modeVue = CREER;
		mapper.update( new Compte(), compteVue );		
	}
	
	@Override
	public void preparerModifier( Compte compte ) {
        modeVue = MODIFIER;
		compteCourant = compte;
		mapper.update( compte, compteVue );	
	}
	
	
	@Override
	public void validerMiseAJour()   {
		
		// Effectue la mise à jour
        if ( modeVue == CREER ) {
            if (mapComptes.isEmpty()) {
            	compteVue.setId(1);
            } else {
            	compteVue.setId(Collections.max(mapComptes.keySet()) + 1);
            }
            compteCourant = mapper.update( compteVue, new Compte() );
			mapComptes.put( compteCourant.getId(), compteCourant );
			comptes.add( compteCourant );
		}
        if ( modeVue == MODIFIER ) {
			mapper.update( compteVue, compteCourant );		
		}

        // Trie la liste
        trierListe();
	}
	
	
	@Override
	public void supprimer( Compte compte )   {
		mapComptes.remove( compte.getId() );
		comptes.remove( compte );
	}
	
	
	// Initialisaiton
	
	public void refresh() throws Exception {
		actualiserListe();
	}
    
    
    // Méthodes auxiliaires
    
    private void trierListe() {
		FXCollections.sort( comptes,
            (Comparator<Compte>) ( item1, item2) -> {
                return ( item1.pseudoProperty().get().toUpperCase().compareTo(item2.pseudoProperty().get().toUpperCase()));
		});
    }
    
}
