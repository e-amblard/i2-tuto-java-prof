package contacts.javafx.model.mock;

import java.util.HashMap;
import java.util.Map;

import contacts.commun.util.Roles;
import contacts.javafx.data.Compte;


public class Donnees {

	
    // Champs 

    private final Map<Integer, Compte>  		mapComptes 		= new HashMap<>();

	
	// Getters
    
	public Map<Integer, Compte> getMapComptes() {
		return mapComptes;
	}
	
	
	// Constructeur
	
	public Donnees() {
		initialiserDonnees();
	}
	
	
	// Méthodes auxiliaires

	private void initialiserDonnees() {
		
		
		// Comptes
		
		Compte compte;
		compte = new Compte( 1, "geek", "geek", "geek@3il.fr" );
		compte.getRoles().add( Roles.ADMINISTRATEUR  );
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );

		compte = new Compte(2, "chef", "chef", "chef@3il.fr");
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
		
		compte = new Compte( 3, "job", "job", "job@3il.fr" );
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );

		compte = new Compte(4, "_" + this.getClass().getPackage().getName(), "xxx", "xxx@3il.fr");
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
	
	}
	
	
}
