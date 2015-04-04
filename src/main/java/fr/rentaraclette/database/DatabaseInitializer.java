package fr.rentaraclette.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import fr.rentaraclette.dto.ExamlpleDto;

public class DatabaseInitializer {
	
	private static DatabaseInitializer		instance = null;
	
	private SessionFactory			sessionFactory;
	private ServiceRegistry 		serviceRegistry;

	private DatabaseInitializer() {
		setUpFactory();
	}
	
	public static DatabaseInitializer getInstance() {
		if (instance == null)
			instance = new DatabaseInitializer();
		return instance;
	}
	
	/* 
	 * Setting the factory (give the package where to find the DTOs and the classes
	 * YOU NEED TO ADD ALL YOUR NEW DTO WITH .addAnnotatedClass(NewDto.class)
	 */
	public void setUpFactory() {
		Configuration cfg = new Configuration();
		cfg.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		sessionFactory = cfg
        .addPackage("fr.rentaraclette.dto")
        .addAnnotatedClass(ExamlpleDto.class)
        // add other DTO here
        .buildSessionFactory(serviceRegistry);
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
}
