package fr.stevecohen.dao;

import fr.stevecohen.database.Hibernate;

public class AbstractDao {
	protected Hibernate hibernate = Hibernate.getInstance();
}
