package contacts.javafx.model.mock;

import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import contacts.javafx.model.IModelPersonne;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ModelPersonne implements IModelPersonne {

	
	// Données observables
	
	private final ObservableList<Personne> personnes = FXCollections.observableArrayList(
				p ->  new Observable[ ] { p.nomProperty(), p.prenomProperty() }
			);

	private final Personne personneVue = new Personne( 99,"XXXXX","Xxxxx" ); 
	
	
	// Données courante
	
	private Personne		personneCourant;
	
	
	// Autres champs
	private int dernierIdTelephone = 0;
	
	
	// Initialisation
	
	public void refresh() {
		actualiserListe();
	}
	
	
	// Getters
	
	public ObservableList<Personne> getPersonnes() {
		return personnes;
	}
	
	public Personne getPersonneVue() {
		return personneVue;
	}
	
	
	// Actions


	public void actualiserListe() {
		personnes.clear();
		personnes.add( new Personne( 1, "DUBOIS", "Jean" ) );
		personnes.add( new Personne( 2, "DUPONT", "Marie" ) );
		personnes.add( new Personne( 3, "DURAND", "Pierre" ) );
	}
	
	
	public void prepererAjouter() {
		personneCourant = null;
		copierDonnées( new Personne(), personneVue );
	}
 	
	public void preparerModifier( Personne personne ) {
		personneCourant = personne;
		copierDonnées( personneCourant, personneVue );
	}
	
	public void validerMiseAJour() throws Exception {
		
		StringBuilder message = new StringBuilder();
		System.out.println(personneVue.getNom() );
		if( personneVue.getNom() == null || personneVue.getNom().isEmpty() ) {
			message.append( "\n").append( "Le nom ne doit pas être vide.");
		} else if( personneVue.getNom().length() > 25 ) {
			message.append( "\n").append( "Le nom est trop long.");
		}
		if( personneVue.getPrenom() == null || personneVue.getPrenom().isEmpty() ) {
			message.append( "\n").append( "Le prénom ne doit pas être vide.");
		} else if( personneVue.getPrenom().length() > 25 ) {
			message.append( "\n").append( "Le prénom est trop long.");
		}
		
		if ( message.length() != 0 ) {
			message.delete(0, 1);
			throw new Exception( message.toString() );
		}

		if ( personneCourant == null ) {
			personneCourant = copierDonnées( personneVue, new Personne() ); 
			int idMax = 0;
			for ( Personne p : personnes ) {
				if ( idMax < p.getId() ) {
					idMax = p.getId();
				}
			}
			personneCourant.setId( idMax + 1 );
			personnes.add( personneCourant );
		} else {
			copierDonnées( personneVue, personneCourant );
		}
	}
	
	public void supprimer( Personne personne ) {
		personnes.remove(personne);
	}
	
	public void ajouterTelphone() {
		Telephone telephone = new Telephone();
		telephone.setId( ++dernierIdTelephone );
		personneVue.getTelephones().add( telephone );
	}
	
	
	public void supprimerTelephone( Telephone telephone ) {
		personneVue.getTelephones().remove(telephone);
	}
	
	// Méthodes auxiliaires
	
	private Personne copierDonnées( Personne source, Personne cible ) {
		cible.setId( source.getId() );
		cible.setNom( source.getNom() );
		cible.setPrenom( source.getPrenom() );
		cible.getTelephones().clear();
		for( Telephone telphone : source.getTelephones() ) {
			cible.getTelephones().add( new Telephone( telphone.getId(), telphone.getLibelle(), telphone.getNumero() ) );
		}
		return cible;
	}
}
