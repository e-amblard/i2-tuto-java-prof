package contacts.javafx.view.compte;

import contacts.commun.util.ExceptionValidation;
import contacts.javafx.data.Compte;
import contacts.javafx.model.IModelCompte;
import contacts.javafx.view.EnumView;
import contacts.javafx.view.IManagerGui;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class ControllerCompteListe  {
	
	
	// Composants de la vue

	@FXML
	private ListView<Compte>	listView;
	@FXML
	private Button				buttonModifier;
	@FXML
	private Button				buttonSupprimer;
	@FXML
	private Button				buttonMemos;

	
	// Autres champs
	
	private IManagerGui			managerGui;
	private IModelCompte		modelCompte;

	
	// Injecteurs
	
	public void setManagerGui(IManagerGui managerGui) {
		this.managerGui = managerGui;
	}
	
	public void setModelCompte(IModelCompte modelCompte) {
		this.modelCompte = modelCompte;
	}
	
	
	// Initialisation du Controller

	public void init() {
		
		// Configuration de l'objet ListView
		
		// Data binding
		listView.setItems( modelCompte.getComptes() );

		// Affichage
		listView.setCellFactory( (list) -> {
		    return new ListCell<Compte>() {
		        @Override
		        protected void updateItem(Compte item, boolean empty) {
		            super.updateItem(item, empty);
		            if (item == null) {
		                setText(null);
		            } else {
		                setText(item.pseudoProperty().get() );
		            }
		        }
		    };
		});		

		// Comportement si modificaiton de la séleciton
		listView.getSelectionModel().getSelectedItems().addListener( 
				(ListChangeListener<Compte>) (c) -> {
					 configurerBoutons();					
		});

		// Comportement si changement du contenu
		listView.getItems().addListener( (ListChangeListener<Compte>) (c) -> {
			c.next();
			// Après insertion d'un élément, le sélectionne
			// Après suppression d'un élément, sélectionne le suivant
			if ( c.wasAdded() || c.wasRemoved() ) {
				listView.getSelectionModel().clearSelection();
				listView.getSelectionModel().select( c.getFrom());
				listView.getFocusModel().focus(c.getFrom());
				listView.scrollTo( c.getFrom());
				listView.requestFocus();
			}
		});
	}

	
	// Actions

	@FXML
	private void doActualiser() {
		modelCompte.actualiserListe();
		listView.getSelectionModel().clearSelection();;
	}

	@FXML
	private void doAjouter() {
		modelCompte.preparerAjouter();
		managerGui.showView( EnumView.CompteForm );
	}

	@FXML
	private void doModifier() {
		modelCompte.preparerModifier( listView.getSelectionModel().getSelectedItem() );
		managerGui.showView( EnumView.CompteForm );
	}

	@FXML
	private void doSupprimer() throws ExceptionValidation {
		boolean reponse = managerGui.demanderConfirmation( "Confirmez-vous la suppresion ?" );
		if ( reponse ) {
			modelCompte.supprimer( listView.getSelectionModel().getSelectedItem() );
		}
	}
	
	
	// Gestion des évènements

	// Clic sur la liste
	@FXML
	private void gererClicSurListe( MouseEvent event ) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (event.getClickCount() == 2) {
				doModifier();
			}
		}
	}


	// Méthodes auxiliaires
	
	private void configurerBoutons() {
		int nbSelections = listView.getSelectionModel().getSelectedItems().size();
		if ( nbSelections == 1 ) {
			buttonModifier.setDisable(false);
			buttonSupprimer.setDisable(false);
		} else {
			buttonModifier.setDisable(true);
			buttonSupprimer.setDisable(true);
		}
	}

}
