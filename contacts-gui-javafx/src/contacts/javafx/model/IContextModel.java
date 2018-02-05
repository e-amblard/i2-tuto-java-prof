package contacts.javafx.model;


public interface IContextModel {

	<T> T getModel(Class<T> type);

}