
package contacts.emb.dao.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class DataSourceSingleConnection implements DataSource {
	
	
	// Champs

	private String			jndiName;
	private String			driver;
	private String			url;
	
	private String			user;
	private String			password;
	
	private DataSource		dataSourceInitial;
	
	private IProxyConnection connection;
	
	
	// Constructeurs
	
	public DataSourceSingleConnection() {
	}

	public DataSourceSingleConnection( DataSource dataSource ) {
		setDataSourceInitial(dataSource);
	}

	public DataSourceSingleConnection( Properties props ) {
		extractProperties(props);
	}
	
	public DataSourceSingleConnection( InputStream in ) {
		try {
			Properties props = new Properties();
			props.load(in);
			extractProperties(props);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public DataSourceSingleConnection( String jndiName ) {
		setJndiName(jndiName);
	}

	
	// Getters & Setters
	
	public DataSource getDataSourceInitial() {
		return dataSourceInitial;
	}
	
	public void setDataSourceInitial( DataSource dataSourceInitial ) {
		if ( connection != null ) {
			throw new IllegalStateException( "La connecxion est ouverte." );
		}
		if ( dataSourceInitial != null ) {
			setJndiName(null);
			setDriver(null);
			setUrl(null);
		}
		this.dataSourceInitial = dataSourceInitial;
	}
	
	public String getJndiName() {
		return jndiName;
	}
	
	public void setJndiName(String jndiName) {
		if ( connection != null ) {
			throw new IllegalStateException( "La connecxion est ouverte." );
		}
		if ( jndiName != null ) {
			setDataSourceInitial(null);
			setDriver(null);
			setUrl(null);
			try {
				Context nc = new InitialContext();
				dataSourceInitial = (DataSource) nc.lookup( jndiName );
			} catch ( Exception e) {
				throw new RuntimeException(e);
			}
		}
		this.jndiName = jndiName;
}
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		if ( connection != null ) {
			throw new IllegalStateException( "La connecxion est ouverte." );
		}
		if( driver != null ) {
			setDataSourceInitial(null);
			setJndiName(null);
		}
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if( url != null ) {
			setDataSourceInitial(null);
			setJndiName(null);
		}
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	// Actions

	@Override
	public Connection getConnection(String user, String password) throws SQLException {

		this.user = user;
		this.password = password;
		
		if ( connection == null ) {
			if ( dataSourceInitial != null ) {
				Connection cn;
				if ( user == null  ) {
					cn = dataSourceInitial.getConnection();
				} else {
					cn = dataSourceInitial.getConnection(user, password);
				}
				connection = FactoryProxyConnection.createProxy( cn );
			} else {
				if ( driver != null ) {
					try {
						Class.forName( driver );
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				connection = FactoryProxyConnection.createProxy( DriverManager.getConnection(url, user, password) );
			}
		}
		return connection;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getConnection(user, password);
	}
	
	public void closeConnection() {
		if ( connection != null ) {
			connection.closeConnection();
			connection = null;
		}
	}
	
	
	// Méthode auxiliaires
	
	private void extractProperties( Properties props ) {
		setUser(		props.getProperty( "jdbc.user" ) );
		setPassword(	props.getProperty( "jdbc.password" ) );
		setDriver(		props.getProperty( "jdbc.driver" ) );
		setUrl( 		props.getProperty( "jdbc.url" ) );
		setJndiName(	props.getProperty( "jdbc.jndi" ) );
	}
	
	
	// Actions non implémentées
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new UnsupportedOperationException();
	}
	

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}
	
	

}
