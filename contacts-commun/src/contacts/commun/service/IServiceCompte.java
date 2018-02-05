package contacts.commun.service;

import java.util.List;

import contacts.commun.dto.DtoCompte;
import contacts.commun.util.ExceptionValidation;


public interface IServiceCompte {
	
	int				inserer( DtoCompte dtoCompte ) throws ExceptionValidation;

	void			modifier( DtoCompte dtoCompte ) throws ExceptionValidation; 

	void			supprimer( int idCompte ) throws ExceptionValidation;

	DtoCompte 		retrouver( int idCompte ) ;

	List<DtoCompte>	listerTout() ;

}
