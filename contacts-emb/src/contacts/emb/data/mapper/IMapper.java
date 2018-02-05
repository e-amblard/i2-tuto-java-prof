package contacts.emb.data.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import contacts.commun.dto.DtoCompte;
import contacts.commun.dto.DtoPersonne;
import contacts.commun.dto.DtoTelephone;
import contacts.emb.data.Compte;
import contacts.emb.data.Personne;
import contacts.emb.data.Telephone;

 
@Mapper
public interface IMapper {  
	
	static final IMapper INSTANCE = Mappers.getMapper( IMapper.class );
	
	
	// Comptes
	
	Compte map( DtoCompte source );
	
	DtoCompte map( Compte source );
	
	
	// Personne
	
	Personne map(DtoPersonne source);

	DtoPersonne map(Personne source);	
	
    
	// Telephone
	
	@Mapping( target="personne", ignore=true )
	Telephone map( DtoTelephone source );
	
	DtoTelephone map( Telephone source );
	
	@AfterMapping
	default void ajouterRefPersonne( @MappingTarget Personne personne ) {
	    for ( Telephone telephone : personne.getTelephones() ) {
	        telephone.setPersonne( personne );
	    }
	}	
}
