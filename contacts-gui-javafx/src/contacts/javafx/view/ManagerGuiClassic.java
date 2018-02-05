package contacts.javafx.view;

import contacts.javafx.model.IContextModel;

public class ManagerGuiClassic extends ManagerGuiAbstract {

	
	// Champs
	
	private final IContextModel		contextModel;


	// Constructeur
	
	public ManagerGuiClassic(IContextModel contextModel) {
		this.contextModel = contextModel;
	}


	// Actions
	
	@Override
	protected <T> T getModelFromContext(Class<T> type) {
		return contextModel.getModel(type);
	}
	
	
}