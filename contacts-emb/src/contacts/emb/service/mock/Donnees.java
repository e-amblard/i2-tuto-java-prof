package contacts.emb.service.mock;

import java.util.HashMap;
import java.util.Map;

import contacts.commun.dto.DtoCompte;
import contacts.commun.dto.DtoPersonne;
import contacts.commun.dto.DtoTelephone;
import contacts.commun.util.Roles;


public class Donnees {

	
    // Champs 

    private final Map<Integer, DtoCompte>  		mapComptes 		= new HashMap<>();
	private final Map<Integer, DtoPersonne>		mapPersonnes	= new HashMap<>();

	private int 	dernierIdTelephone;

	
	// Getters
    
	public Map<Integer, DtoCompte> getMapComptes() {
		return mapComptes;
	}

	public Map<Integer, DtoPersonne> getMapPersonnes() {
		return mapPersonnes;
	}
	
	public int getProchainIdTelephone() {
		return ++dernierIdTelephone;
	}
	
	
	// Constructeur
	
	public Donnees() {
		initialiserDonnees();
	}
	
	
	// Méthodes auxiliaires

	private void initialiserDonnees() {
		
		
		// Comptes
		
		DtoCompte compte;
		compte = new DtoCompte( 1, "geek", "geek", "geek@3il.fr" );
		compte.getRoles().add( Roles.ADMINISTRATEUR  );
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );

		compte = new DtoCompte(2, "chef", "chef", "chef@3il.fr");
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
		
		compte = new DtoCompte( 3, "job", "job", "job@3il.fr" );
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );

		compte = new DtoCompte(4, "_" + this.getClass().getPackage().getName(), "xxx", "xxx@3il.fr");
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );

    	
    	// Personnes

        DtoPersonne personne;

        personne = new DtoPersonne( 1, "VERLAINE", "Paul" );
        personne.getTelephones().add(new DtoTelephone(31, "Portable", "06 33 33 33 33"));
        personne.getTelephones().add(new DtoTelephone(32, "Domicile", "05 55 33 33 33"));
        personne.getTelephones().add(new DtoTelephone(33, "Travail", "05 55 99 33 33"));
        mapPersonnes.put(personne.getId(), personne);

        personne = new DtoPersonne( 2, "HUGO", "Victor" );
        personne.getTelephones().add(new DtoTelephone(11, "Portable", "06 11 11 11 11"));
        personne.getTelephones().add(new DtoTelephone(12, "Domicile", "05 55 11 11 11"));
        personne.getTelephones().add(new DtoTelephone(13, "Travail", "05 55 99 11 11"));
        mapPersonnes.put(personne.getId(), personne);

        personne = new DtoPersonne( 3, "TRIOLET", "Elsa" );
        personne.getTelephones().add(new DtoTelephone(21, "Portable", "06 22 22 22 22"));
        personne.getTelephones().add(new DtoTelephone(22, "Domicile", "05 55 22 22 22"));
        personne.getTelephones().add(new DtoTelephone(23, "Travail", "05 55 99 22 22"));
        mapPersonnes.put(personne.getId(), personne);
        
        dernierIdTelephone = 100;
	
	}
	
	
}
