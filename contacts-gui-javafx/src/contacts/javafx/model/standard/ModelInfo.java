package contacts.javafx.model.standard;

import contacts.javafx.model.IModelInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ModelInfo implements IModelInfo {
	
	
	// Données observables 
	
	private final StringProperty	titre = new SimpleStringProperty();
	private final StringProperty	message = new SimpleStringProperty();
	

	// Getters 
	
	@Override
	public StringProperty titreProperty() {
		return titre;
	}
	
	@Override
	public StringProperty messageProperty() {
		return message;
	}
	
	
	// Initialisaiton
	
//	public void init() throws Exception {
//	}
	
	
}
