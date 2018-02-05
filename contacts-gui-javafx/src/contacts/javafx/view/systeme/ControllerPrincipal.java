package contacts.javafx.view.systeme;

import contacts.commun.util.Roles;
import contacts.javafx.model.IModelConnexion;
import contacts.javafx.view.EnumView;
import contacts.javafx.view.IManagerGui;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;


public class ControllerPrincipal  {
 	

	// Composants de la vue
	
	@FXML
	private MenuItem	menuItemSeDeconnecter;
	@FXML
	private Menu 		menuDonnees;
	@FXML
	private MenuItem	menuItemComptes;

	
	// Autres champs
	
	private IManagerGui		managerGui;
	private IModelConnexion	modelConnexion;

	
	// Injecteurs
	
	public void setManagerGui(IManagerGui managerGui) {
		this.managerGui = managerGui;
	}
	
	public void setModelConnexion(IModelConnexion modelConnexion) {
		this.modelConnexion = modelConnexion;
	}
	
	
	// Initialisation du Controller
	
	public void init() {
		
		// Le changement du compte connecté modifie automatiquement le menu
		modelConnexion.compteConnecteProperty().addListener(
				( ov, oldValue, newValue) -> {
					configurerMenu();
				}
			); 

		// Adapte le menu
		configurerMenu();
	}
	

	// Actions
	
	@FXML
	public void doSeDeconnecter() {
		modelConnexion.fermerSessionUtilisateur();
		managerGui.reinit();
		setManagerGui(managerGui);
		managerGui.showView( EnumView.Connexion );
	}
	
	@FXML
	public void doQuitter() {
		managerGui.close();
	}
	
	@FXML
	public void doAfficherListeComptes() {
		managerGui.showView( EnumView.CompteListe );;
	}
	
	@FXML
	public void doAfficherListePersonnes() {
		managerGui.showView( EnumView.PersonneListe );;
	}
	

	// Méthodes auxiliaires
	
	private void configurerMenu() {

		menuItemSeDeconnecter.setDisable(true);
		
		menuDonnees.setVisible(false);
		menuItemComptes.setVisible(false);
		
		if( modelConnexion.getCompteConnecte() != null ) {
			menuItemSeDeconnecter.setDisable(false);
			if( modelConnexion.getCompteConnecte().isInRole( Roles.UTILISATEUR) ) {
				menuDonnees.setVisible(true);
			}
			if( modelConnexion.getCompteConnecte().isInRole( Roles.ADMINISTRATEUR ) ) {
				menuDonnees.setVisible(true);
				menuItemComptes.setVisible(true);
			}
		}
	}

}
