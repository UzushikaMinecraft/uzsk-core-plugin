package net.iamtakagi.uzsk.core.model;

import net.iamtakagi.uzsk.core.Database;
import net.iamtakagi.uzsk.core.impl.ProfileDaoImpl;

public class DaoFactory {

	private Database database;

	public DaoFactory(Database database) {
		this.database = database;
	}

    public ProfileDao createProfileDao() {
		return new ProfileDaoImpl(database);
	}
}
