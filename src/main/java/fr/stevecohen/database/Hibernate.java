package fr.stevecohen.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import fr.stevecohen.dto.ProfileDto;

public class Hibernate {
	
	private static Hibernate		instance = null;
	
	private SessionFactory			sessionFactory;
	private ServiceRegistry 		serviceRegistry;

	private Hibernate() {
		setUpFactory();
	}
	
	public static Hibernate getInstance() {
		if (instance == null)
			instance = new Hibernate();
		return instance;
	}
	
	public void setUpFactory() {
		Configuration cfg = new Configuration();
		cfg.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		sessionFactory = cfg
        .addPackage("fr.stevecohen.dto")
        .addAnnotatedClass(ProfileDto.class)
        .buildSessionFactory(serviceRegistry);
	}
	
	public boolean isConnected() {
		Session session = sessionFactory.openSession();
		boolean connected = session.isConnected();
		session.close();
		if (connected)
			return true;
		return false;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
}
