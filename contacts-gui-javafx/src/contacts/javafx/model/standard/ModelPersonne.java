package contacts.javafx.model.standard;

import contacts.commun.dto.DtoPersonne;
import contacts.commun.service.IServicePersonne;
import contacts.commun.util.ExceptionValidation;
import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import contacts.javafx.data.mapper.IMapper;
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
	private IServicePersonne	servicePersonne;
	private IMapper				mapper;
	
	
	// Injecteurs
	
	public void setServicePersonne(IServicePersonne servicePersonne) {
		this.servicePersonne = servicePersonne;
	}
	
	public void setMapper(IMapper mapper) {
		this.mapper = mapper;
	}
	
	
	// Getters
	
	@Override
	public ObservableList<Personne> getPersonnes() {
		return personnes;
	}
	
	@Override
	public Personne getPersonneVue() {
		return personneVue;
	}

	
	// Initialisation
	
	@Override
	public void refresh() {
	    actualiserListe();
	}	
	
	// Actions


	@Override
	public void actualiserListe() {
		personnes.clear();
		for( DtoPersonne dto : servicePersonne.listerTout() ) {
			personnes.add( mapper.map(dto) );
		}
	}
	
	
	@Override
	public void prepererAjouter() {
		personneCourant = null;
		mapper.update( new Personne(), personneVue );
	}
 	
	@Override
	public void preparerModifier( Personne personne ) {
		personneCourant = personne;
		mapper.update( personneCourant, personneVue );
	}
	
	@Override
	public void validerMiseAJour() throws ExceptionValidation {
		
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
			throw new ExceptionValidation( message.toString() );
		}

		if ( personneCourant == null ) {
			int id = servicePersonne.inserer( mapper.map(personneVue) );
			personneVue.setId(id);
			personneCourant = new Personne();
			personnes.add( personneCourant );
		} else {
			servicePersonne.modifier( mapper.map(personneVue) );
		}
		mapper.update( mapper.map( servicePersonne.retrouver(personneVue.getId() )), personneCourant ); 
	}
	
	@Override
	public void supprimer( Personne personne ) throws ExceptionValidation {
		servicePersonne.supprimer( personne.getId() );
		personnes.remove(personne);
	}
	
	@Override
	public void ajouterTelphone() {
		Telephone telephone = new Telephone();
		personneVue.getTelephones().add( telephone );
	}
	
	
	@Override
	public void supprimerTelephone( Telephone telephone ) {
		personneVue.getTelephones().remove(telephone);
	}
	
	// Méthodes auxiliaires
	
//	private Personne copierDonnées( Personne source, Personne cible ) {
//		cible.setId( source.getId() );
//		cible.setNom( source.getNom() );
//		cible.setPrenom( source.getPrenom() );
//		cible.getTelephones().clear();
//		for( Telephone telphone : source.getTelephones() ) {
//			cible.getTelephones().add( new Telephone( telphone.getId(), telphone.getLibelle(), telphone.getNumero() ) );
//		}
//		return cible;
//	}
}
