package fr.rentaraclette.dao;

import fr.rentaraclette.database.DatabaseInitializer;

public class AbstractDao {
	protected DatabaseInitializer hibernate = DatabaseInitializer.getInstance();
}
