package contacts.emb.dao.jdbc.util;

import java.sql.Connection;

public interface IProxyConnection extends Connection {
	
	public void closeConnection();

}
