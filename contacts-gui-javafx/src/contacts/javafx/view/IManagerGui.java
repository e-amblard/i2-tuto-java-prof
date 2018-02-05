package contacts.javafx.view;

import contacts.javafx.view.ManagerGuiAbstract.RunnableWithException;

public interface IManagerGui {

	void launch( String titre );

	void showView(EnumView view);

	void reinit();

	void close();

	void execTask( RunnableWithException runnable );

	void afficherMessage(String message);

	void afficherErreur(Throwable exception);

	void afficherErreur(String message);

	void afficherErreur(String message, Throwable exception);

	boolean demanderConfirmation(String message);

}