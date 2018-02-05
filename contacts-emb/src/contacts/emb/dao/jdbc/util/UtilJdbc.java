package contacts.emb.dao.jdbc.util;


public final class UtilJdbc {
	
	
	// Constructeur
	private UtilJdbc() {
	}

	
	// Méthodes utilitaires
	
	public static void close( AutoCloseable...closeables ) {
		for ( AutoCloseable closeable : closeables ) {
			if ( closeable != null ) {
				try {
					closeable.close();
				} catch( Exception e ) {
				}
			}
		}
		
	}
	
}
