package fr.rentaraclette.dao;

import fr.rentaraclette.database.Hibernate;

public class AbstractDao {
	protected Hibernate hibernate = Hibernate.getInstance();
}
