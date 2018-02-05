package contacts.javafx.view.personne;

import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import contacts.javafx.model.IModelPersonne;
import contacts.javafx.view.EnumView;
import contacts.javafx.view.IManagerGui;
import contacts.javafx.view.util.EditingCell;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ControllerPersonneForm {
	
	
	// Composants visuels de la vue
	
	@FXML
	private TextField	textFieldId;
	@FXML
	private TextField	textFieldNom;
	@FXML
	private TextField	textFieldPrenom;
	@FXML
	private TableView<Telephone>	tableViewTelephones;
	@FXML
	private TableColumn<Telephone, Number> columnId;
	@FXML
	private TableColumn<Telephone, String> columnLibelle;
	@FXML
	private TableColumn<Telephone, String> columnNumero;
	

	
	// Autres champs
	
	private IModelPersonne	modelPersonne;
	private IManagerGui		managerGui;
    
    
    // Injecteur
    
    public void setManagerGui(IManagerGui managerGui) {
		this.managerGui = managerGui;
	}
    
    public void setModelPersonne(IModelPersonne modelPersonne) {
		this.modelPersonne = modelPersonne;
	}
	
	
	// Actions
	
	@FXML
	private void doValider() {
		try {
			modelPersonne.validerMiseAJour();
//			Main.showViewPersonneListe();
			managerGui.showView( EnumView.PersonneListe ) ;
		} catch (Exception e) {
			managerGui.afficherErreur(e );
		}
	}
	
	@FXML
	private void doAnnuler() {
//		Main.showViewPersonneListe();
		managerGui.showView( EnumView.PersonneListe ) ;
	}
	
	@FXML
	private void doAjouterTelephone() {
		modelPersonne.ajouterTelphone();
	}
	
	@FXML
	private void doSupprimerTelephone() {
		modelPersonne.supprimerTelephone( tableViewTelephones.getSelectionModel().getSelectedItem() );
	}
	
	
	// Initialisation
	
	@FXML
	private void init() {
		
		// Injection des dépendances
//		modelPersonne = Main.getModelPersonne();
		
		// Data dinding
		Personne  personneVue  =  modelPersonne.getPersonneVue();
		textFieldId.textProperty().bind( Bindings.convert( personneVue.idProperty() ) );
		textFieldNom.textProperty().bindBidirectional( personneVue.nomProperty() );
		textFieldPrenom.textProperty().bindBidirectional( personneVue.prenomProperty() );
		
		tableViewTelephones.setItems( modelPersonne.getPersonneVue().getTelephones() );
		columnId.setCellValueFactory( cellData -> cellData.getValue().idProperty() );
		columnLibelle.setCellValueFactory( cellData -> cellData.getValue().libelleProperty() );
		columnNumero.setCellValueFactory( cellData -> cellData.getValue().numeroProperty() );
		
		columnLibelle.setCellFactory( column -> new EditingCell<>() );
		columnNumero.setCellFactory( column -> new EditingCell<>() );
	}

}
