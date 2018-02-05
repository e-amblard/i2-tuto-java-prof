package contacts.emb.dao.mock;

import java.util.logging.Logger;

import contacts.emb.dao.IManagerTransaction;


public class ManagerTransaction implements IManagerTransaction {
	
	
	// Logger
	private static final Logger logger = Logger.getLogger(ManagerTransaction.class.getName());
	
	
	// Actions

	@Override
	public void begin() {
		logger.finer("Transaction BEGIN");
	}

	@Override
	public void commit() {
		logger.finer("Transaction COMMIT");
	}

	@Override
	public void rollback() {
		logger.finer("Transaction ROLLBACK");
	}

	@Override
	public void rollbackSiApplicable() {
		rollback();
	}

}
