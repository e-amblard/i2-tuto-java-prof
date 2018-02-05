package contacts.javafx.view.util;

import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;

public class ListenerListViewSetFocus<E> implements ListChangeListener<E> {

	private ListView<E>		listView;
	
	public ListenerListViewSetFocus( ListView<E> listView ) {
		this.listView = listView;
	}
	
	@Override
	public void onChanged(javafx.collections.ListChangeListener.Change<? extends E> c) {
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
	}

}
