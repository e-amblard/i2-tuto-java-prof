package contacts.javafx.view.personne;

import contacts.commun.util.ExceptionValidation;
import contacts.javafx.data.Personne;
import contacts.javafx.model.IModelPersonne;
import contacts.javafx.view.EnumView;
import contacts.javafx.view.IManagerGui;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class ControllerPersonneListe {
    
	
    // Composants de la vue
	
    @FXML
    private ListView<Personne>    listViewPersonnes;
    @FXML
    private Button					buttonModifier;
    @FXML
    private Button					buttonSupprimer;
    
    
    // Champs
    
    private IModelPersonne           modelPersonne;
    private IManagerGui				managerGui;
    
    
    // Injecteur
    
    public void setManagerGui(IManagerGui managerGui) {
		this.managerGui = managerGui;
	}
    
    public void setModelPersonne(IModelPersonne modelPersonne) {
		this.modelPersonne = modelPersonne;
	}
    
    
    // Action
    
    @FXML
    private void doActualiser() {
    	modelPersonne.actualiserListe();
    }
    
    @FXML
    private void doAjouter() {
    	System.out.println("Ajouter");
    	modelPersonne.prepererAjouter();
    	managerGui.showView( EnumView.PersonneForm );
    }
    
    @FXML
    private void doModifier() {
    	System.out.println( "Modifier item " + listViewPersonnes.getSelectionModel().getSelectedIndex() );
    	modelPersonne.preparerModifier(listViewPersonnes.getSelectionModel().getSelectedItem());
    	managerGui.showView( EnumView.PersonneForm );
    }
    
    @FXML
    private void doSupprimer() throws ExceptionValidation {
    	System.out.println( "Supprimer item " + listViewPersonnes.getSelectionModel().getSelectedIndex() );
    	if (managerGui.demanderConfirmation( "Confirmez-vous la suppression ? " ) ) {
        	modelPersonne.supprimer(listViewPersonnes.getSelectionModel().getSelectedItem());
    	}
    }
    
    
    // Gestion des évènements
    
    @FXML
    private void gererClic( MouseEvent event )  {
        if ( event.getButton().equals(MouseButton.PRIMARY) )  {
            if (event.getClickCount() == 2)  {
                doModifier();
            }
        }
    }
    
    // Initialisations
    
    @FXML
    private void init() {
        
        // Data binding
        listViewPersonnes.setItems( modelPersonne.getPersonnes() );    
        
		ListChangeListener<Personne> listener = (c) -> {
			if (listViewPersonnes.getSelectionModel().getSelectedIndex() == -1) {
				buttonModifier.setDisable(true);
				buttonSupprimer.setDisable(true);
			} else {
				buttonModifier.setDisable(false);
				buttonSupprimer.setDisable(false);
			}
		};
       	listViewPersonnes.getSelectionModel().getSelectedItems().addListener( listener );  
		listener.onChanged(null);

    }    

}