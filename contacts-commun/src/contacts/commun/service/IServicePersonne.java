package contacts.commun.service;

import java.util.List;

import contacts.commun.dto.DtoPersonne;
import contacts.commun.util.ExceptionValidation;


public interface IServicePersonne {
	
	int				inserer( DtoPersonne dtoPersonne ) throws ExceptionValidation;
	
	void			modifier( DtoPersonne dtoPersonne ) throws ExceptionValidation;
	
	void			supprimer( int idPersonne ) throws ExceptionValidation;
	
	DtoPersonne 	retrouver( int idPersonne );
	
	List<DtoPersonne> listerTout();
	

}
