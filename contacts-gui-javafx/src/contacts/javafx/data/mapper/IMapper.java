package contacts.javafx.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import contacts.commun.dto.DtoCompte;
import contacts.commun.dto.DtoPersonne;
import contacts.commun.dto.DtoTelephone;
import contacts.javafx.data.Compte;
import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
   

@Mapper( uses=IMapper.FactoryObsservableList.class  )
public interface IMapper { 
	
	IMapper INSTANCE = Mappers.getMapper( IMapper.class );
	
	
	// Compte
	
	Compte update( Compte source, @MappingTarget Compte target );
	
	Compte map( DtoCompte source );
	
	DtoCompte map( Compte source );
	
	
	// Personne
	
	Personne map(DtoPersonne source);

	DtoPersonne map(Personne source);	
	
	Personne update( Personne source, @MappingTarget Personne target );
	Telephone duplicate( Telephone source );
    ObservableList<Telephone> duplicate( ObservableList<Telephone> source );
	
    
	// Telephone
	
	Telephone map( DtoTelephone source );
	
	DtoTelephone map( Telephone source );
	
	
    // Classe auxiliaire
    
    public static class FactoryObsservableList {

    	ObservableList<String> createObsListString() {
    		return FXCollections.observableArrayList();
    	}

    	ObservableList<Telephone> createObsListTelephone() {
    		return FXCollections.observableArrayList();
    	}

    }
    
}