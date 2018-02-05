package contacts.javafx.view.util;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.Property;


public class StringBindingId extends StringBinding {

	private Property<Number>	id;
	
	public StringBindingId( Property<Number> id ) {
		this.id = id;
		super.bind(id);
	}
	
	@Override
	public void dispose() {
		super.unbind(id);
	}

	@Override
	protected String computeValue() {
		if( id == null) {
			return null;
		}
		if ( 0 == id.getValue().intValue() ) {
			return "";
		}
		return id.getValue().toString();
	}

}
