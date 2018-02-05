package contacts.emb.dao;

public interface IManagerTransaction {
	
	void begin();
	
	void commit();
	
	void rollback();

	void rollbackSiApplicable();

}
