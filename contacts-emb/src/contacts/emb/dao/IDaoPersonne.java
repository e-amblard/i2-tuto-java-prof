package contacts.emb.dao;

import java.util.List;

import contacts.emb.data.Personne;


public interface IDaoPersonne {

	int			inserer( Personne personne );

	void 		modifier( Personne personne );

	void 		supprimer( int idPersonne );

	Personne 	retrouver( int idPersonne );

	List<Personne> listerTout();

}
