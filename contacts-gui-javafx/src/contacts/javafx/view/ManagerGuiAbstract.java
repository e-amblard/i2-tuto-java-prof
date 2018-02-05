package contacts.javafx.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import contacts.commun.util.ExceptionAnomalie;
import contacts.commun.util.ExceptionAutorisation;
import contacts.commun.util.ExceptionValidation;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public abstract class ManagerGuiAbstract implements IManagerGui  {


	// Constants
	private static final KeyCodeCombination KCC_ENTER = new KeyCodeCombination( KeyCode.ENTER );
	private static final KeyCodeCombination KCC_ESCAPE = new KeyCodeCombination( KeyCode.ESCAPE );

	
	// Logger
	private static final Logger logger = Logger.getLogger( ManagerGuiAbstract.class.getName() );

	
	// Champs
	
	private final Set<Object>	models = new HashSet<>();

	private String				titre;
	private Stage				stage;
	private Scene				scene;
    private BorderPane			panePrincipal;
	private EnumView			viewPrecedente;
	
	
	// Initialisations
	
	@Override
	public void launch( String titre ) {
		this.titre = titre;
		ApplicationJavaFX.launch(this);
	}
	
	
	private void start( Stage stage ) throws Exception {

		// Traitement par défaut des exceptions
		Thread.setDefaultUncaughtExceptionHandler(this::afficherErreur);
	
		// Configure la scene
		String path ="systeme/PanePrincipal.fxml" ;
		FXMLLoader loader = new FXMLLoader(getClass().getResource( path ));
		panePrincipal = loader.load();
		initController( loader.getController() );
		scene = new Scene( panePrincipal );
		
		// Configure le stage
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setMinHeight(300);
		stage.setMinWidth(400);
		stage.setTitle( titre );
		stage.getIcons().add(new Image(getClass().getResource("icone.png").toExternalForm()));
		this.stage = stage;
		
		// Choisit la vue à afficher
		showView( EnumView.Connexion);
		
		// Affiche le stage
		stage.show();
	}
	
	
	// Actions
	
	// Affichage des vues

	@Override
	public void showView( EnumView view ) {
		
		try {
			
			Pane pane = view.getPane();
		
			if( pane == null ) {
				
				// Charge le panneau
				FXMLLoader loader = new FXMLLoader(getClass().getResource( view.getPathn() ));
				pane = loader.load(); 

				// Injecte les dépendances
				initController( loader.getController() );

			    // Enregistre le panneau dans la vue
				view.setPane( pane );
			}
			// Affiche la vue
			panePrincipal.setCenter(pane);	
			
			// Gère les boutons par défaut
			if ( viewPrecedente != null ) {
				if ( viewPrecedente.getRunnableEnter() == null ) {
					viewPrecedente.setRunnableEnter(scene.getAccelerators().get( KCC_ENTER ) );
				}
				if ( viewPrecedente.getRunnableEscape() == null ) {
					viewPrecedente.setRunnableCancel( scene.getAccelerators().get( KCC_ESCAPE ) );
				}
			} 
			if ( view.getRunnableEnter() != null ) {
				scene.getAccelerators().put( KCC_ENTER, view.getRunnableEnter() );
			}
			if ( view.getRunnableEscape() != null ) {
				scene.getAccelerators().put( KCC_ESCAPE, view.getRunnableEscape() );
			}
			viewPrecedente = view;
			
		} catch (Exception e) {
			afficherErreur(e);
		} 
	}
	
	@Override
	public void reinit() {
		for ( EnumView view : EnumView.values() ) {
			view.setPane(null);
		}
		models.clear();
	}
	
	@Override
	public void close() {
		stage.close();
	}
	
	
	// Actions générales
	
	@Override
	public void execTask( RunnableWithException runnable ) {

		final EventHandler<InputEvent> inputEventConsumer = 
				(event) -> event.consume();
		stage.addEventFilter(InputEvent.ANY, inputEventConsumer);  
		
		Task<Void> task = new Task<Void>() {
		    @Override
		    public Void call() throws Exception {
				stage.getScene().setCursor(Cursor.WAIT); 
				runnable.run();
		        return null ;
		    }
		};
		
		EventHandler<WorkerStateEvent> handlerEndOfTask = (event) -> {
			stage.removeEventFilter(InputEvent.ANY, inputEventConsumer);  
			stage.getScene().setCursor(Cursor.DEFAULT);
			if ( task.getException() != null ) {
				afficherErreur(task.getException());
			}
		};
		task.setOnCancelled(handlerEndOfTask);
		task.setOnFailed(handlerEndOfTask);
		task.setOnSucceeded(handlerEndOfTask);

		Thread thread = new Thread(task);
		thread.start();
	}
	
	@Override
	public void afficherMessage( String message ) {
		final Alert alert = new Alert(Alert.AlertType.INFORMATION);  
		alert.initOwner(stage); 
		alert.setHeaderText(message); 
		alert.showAndWait(); 
	}

	@Override
	public boolean demanderConfirmation( String message ) {
		final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);  
		alert.initOwner( stage ); 
		alert.setHeaderText( message); 
		final Optional<ButtonType> result = alert.showAndWait(); 
		return result.get() == ButtonType.OK;
	}

	
	@Override
	public void afficherErreur( Throwable exception ) {
		afficherErreur( (String) null, exception );
	}

	@Override
	public void afficherErreur( String message ) {
		afficherErreur( message, null );
	}

	@Override
	public void afficherErreur( String message, Throwable exception ) {

		String messageDefaut = null;

		if ( exception != null ) {
			
			if ( exception instanceof ExceptionValidation ) {
				messageDefaut = exception.getMessage();
			} else if ( exception instanceof ExceptionAutorisation
					|| exception.getClass().getName().equals( "javax.ejb.EJBAccessException") ) {
				messageDefaut = "Action non autoriésé !";
				logger.log(Level.FINEST, exception.getMessage(), exception );
			} else if ( exception instanceof ExceptionAnomalie
					|| exception.getClass().getName().equals( "javax.ejb.EJBException") 
					|| exception.getClass().getName().equals( "javax.ejb.EJBTransactionRolledbackException") ) {
				logger.log(Level.FINEST, exception.getMessage(), exception );
			} else if ( exception instanceof RuntimeException ) {
				logger.log(Level.SEVERE, exception.getMessage(), exception );
			} else {
				messageDefaut = exception.getMessage();
				logger.log(Level.SEVERE, exception.getMessage(), exception );
			}
		
			if (message == null ) {
				if ( messageDefaut == null ) {
					message = "Ecec du traitement demandé.";
				} else {
					message = messageDefaut;
				}
			}
		}
		
		final Alert alert = new Alert(Alert.AlertType.ERROR);  
		alert.initOwner(stage); 
		alert.setHeaderText( message ); 
		alert.showAndWait(); 
	}
	
	
	// Méthodes auxiliaires
	
	
	protected abstract <T> T getModelFromContext( Class<T> type );

	
	private <T> T getModel( Class<T> type ) {
		
		T model = getModelFromContext(type);
		
		// Lors de la première utilisation après la connexion de l'utilisateur, 
		// appelle la méthode refresh() si elle existe
		if ( ! models.contains(model) ) {
			try {
				Method method = model.getClass().getMethod( "refresh" );
				method.invoke(model);
			} catch (RuntimeException e) {
				throw e;
			} catch (NoSuchMethodException e) {
			} catch (InvocationTargetException e) {
				if ( e.getCause() instanceof RuntimeException ) {
					throw (RuntimeException) e.getCause();
				} else {
					throw new RuntimeException(e.getCause());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			models.add(model);
		}
		return model;
	}
	
	
	private void initController( Object controller ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// Injecte les dépandances dans le controller
		for( Method method : controller.getClass().getDeclaredMethods() ) {
			if ( method.getName().startsWith("set") 
					&& method.getParameterCount() == 1 )  {
				Class<?> typeParam = method.getParameterTypes()[0];
				if ( method.getName().startsWith( "setModel" ) ) {
					method.invoke( controller, this.getModel( typeParam ) );
				} else if ( IManagerGui.class.isAssignableFrom( typeParam  ) ) {
					method.invoke( controller, this );
				}
			}
		}

		// Exécute la méthode init() du controller
		try {
			Method method = controller.getClass().getDeclaredMethod( "init" );
			method.setAccessible(true);
			method.invoke(controller);
		} catch (NoSuchMethodException e) {
		}
		
	}
	
	
	private void afficherErreur(Thread t, Throwable e) {
		
			if ( e != null
					&& e instanceof RuntimeException
					&& e.getCause() != null 
					&& e.getCause() instanceof InvocationTargetException
					&& e.getCause().getCause() != null
			) {
				e = e.getCause().getCause();
			}
		
		afficherErreur(e);
	}
	
	
	// Types auxiliaires
	

	public static class ApplicationJavaFX extends Application {
		
		private static ManagerGuiAbstract	managerGui;
		
		public static void launch( ManagerGuiAbstract managerGui ) {
			ApplicationJavaFX.managerGui = managerGui;
			launch();
		}
		
		@Override
		public void start(Stage stage) throws Exception {
			managerGui.start(stage);
		}
	}	
	
	
	public static interface RunnableWithException {
		void run() throws Exception;
	}
	
}
