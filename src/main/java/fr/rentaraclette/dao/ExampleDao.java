package fr.rentaraclette.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import fr.rentaraclette.dto.ExamlpleDto;

@SuppressWarnings("unchecked")
public class ExampleDao extends AbstractDao {
	
	private static ExampleDao 	instance = null;
	
	private ExampleDao() {
	}

	public static ExampleDao getInstance() {
		if (instance == null)
			instance = new ExampleDao();
		return instance;
	}

	/* Create a database object from DTO */
	public ExamlpleDto create(ExamlpleDto exampleDto) {
		Session session = hibernate.getSessionFactory().openSession(); // Get the hibernate session factory to operate in database
		session.beginTransaction(); // Start a transaction (all changes will be done after commiting in the transaction)
		int id = Integer.valueOf(session.save(exampleDto).toString()); // Call the 'save' Hibernate function that return a Serializable representing the generating id
		session.getTransaction().commit(); // Commit all changes in database
		/* If something goes wrong before the commit, all changes will be undo */
		session.close();
		
		exampleDto.setId(id); //Update the DTO with the fresh generated id
		
		return exampleDto;
	}
	
	public ExamlpleDto update(ExamlpleDto exampleDto) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		session.update(exampleDto); // Call the 'update' function from Hibernate giving the DTO and the magic will be done (update the object using the unique id)
		session.getTransaction().commit();
		session.close();
		
		return exampleDto;
	}
	
	public List<ExamlpleDto> selectAll() {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		/* session.createQuery(String) is usefull to create custom query using HQL (go google)*/
		List<ExamlpleDto> result = session.createQuery("from ProfileDto").list(); // Get all objects in database that return a list of Object casted in ExampleDTO
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
	public List<ExamlpleDto> selectByName(String name) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from ProfileDto WHERE name LIKE :name"); // Use of variable for security and string validation
		query.setParameter("name", '%' + name + '%'); //Replace the ':name' by '%theName%' the name variable is checked and all invalid caracters are escaped
		List<ExamlpleDto> result = query.list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
	public ExamlpleDto selectById(int id) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		ExamlpleDto profile = (ExamlpleDto)session.get(ExamlpleDto.class, id); // Call the get function from Hibernate that get the DTO class type and the wanted object id
		session.getTransaction().commit();
		session.close();
		
		return profile;
	}
	
	public void delete(ExamlpleDto profileDto) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(profileDto); //Simply delete the object in database using the DTO id
		session.getTransaction().commit();
		session.close();
	}
	
	public List<ExamlpleDto> selectByMultipleFilter(String[] filters) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		String seperatedSkills = StringUtils.join(filters, "&"); //concat all filters with the '&' for REGEXP search in database
		SQLQuery query = session.createSQLQuery("SELECT * FROM profils WHERE skills REGEXP :skills"); // Create a SQLquery (because REGEXP is not supported by HQL)
		query.setParameter("skills", seperatedSkills);
		query.addEntity(ExamlpleDto.class); // I don't remember why there is this line...
		List<ExamlpleDto> result = query.list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
}
