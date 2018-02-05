package contacts.emb.dao;


public interface IContextDao {
	
	<T> T 	getDao(Class<T> type);

}