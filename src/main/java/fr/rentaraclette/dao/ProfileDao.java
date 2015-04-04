package fr.rentaraclette.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import fr.rentaraclette.dto.ProfileDto;

@SuppressWarnings("unchecked")
public class ProfileDao extends AbstractDao {
	
	private static ProfileDao 	instance = null;
	
	private ProfileDao() {
	}

	public static ProfileDao getInstance() {
		if (instance == null)
			instance = new ProfileDao();
		return instance;
	}

	public ProfileDto create(ProfileDto profileDto) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		int id = Integer.valueOf(session.save(profileDto).toString());
		session.getTransaction().commit();
		session.close();
		
		profileDto.setId(id);
		
		return profileDto;
	}
	
	public ProfileDto update(ProfileDto profileDto) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		session.update(profileDto);
		session.getTransaction().commit();
		session.close();
		
		return profileDto;
	}
	
	public List<ProfileDto> selectAll() {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		List<ProfileDto> result = session.createQuery("from ProfileDto").list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
	public List<ProfileDto> selectByName(String name) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from ProfileDto WHERE name LIKE :name");
		query.setParameter("name", '%' + name + '%');
		List<ProfileDto> result = query.list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
	public List<ProfileDto> selectByCity(String city) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from ProfileDto WHERE city LIKE :city");
		query.setParameter("city", '%' + city + '%');
		List<ProfileDto> result = query.list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
	public List<ProfileDto> selectByCountry(String country) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from ProfileDto WHERE country LIKE :country");
		query.setParameter("country", '%' + country + '%');
		List<ProfileDto> result = query.list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
	public List<ProfileDto> selectByPhone(String phone) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from ProfileDto WHERE phone LIKE :phone");
		query.setParameter("phone", '%' + phone + '%');
		List<ProfileDto> result = query.list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
	public ProfileDto selectById(int id) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		ProfileDto profile = (ProfileDto) session.get(ProfileDto.class, id);
		session.getTransaction().commit();
		session.close();
		
		return profile;
	}
	
	public void delete(ProfileDto profileDto) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(profileDto);
		session.getTransaction().commit();
		session.close();
	}
	
	public List<ProfileDto> selectBySkills(String[] skills) {
		Session session = hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		String seperatedSkills = StringUtils.join(skills, "&");
		SQLQuery query = session.createSQLQuery("SELECT * FROM profils WHERE skills REGEXP :skills");
		query.setParameter("skills", seperatedSkills);
		query.addEntity(ProfileDto.class);
		List<ProfileDto> result = query.list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
}
