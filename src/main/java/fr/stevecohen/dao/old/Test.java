/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package fr.stevecohen.dao.old;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import fr.stevecohen.dto.MovieDto;

/**
 * Illustrates the use of Hibernate native APIs.  The code here is unchanged from the {@code basic} example, the
 * only difference being the use of annotations to supply the metadata instead of Hibernate mapping files.
 *
 * @author Steve Ebersole
 */
public class Test {
	private SessionFactory 			sessionFactory;
	private ServiceRegistry 		serviceRegistry;

	public static void main(String[] args) {
		Test test = new Test();
		test.setUpFactory();
		try {
			test.testBasicUsage();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		test.end();
	}
	
	public void setUpFactory() {
		Configuration cfg = new Configuration();
		cfg.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		sessionFactory = cfg
        .addPackage("fr.stevecohen.dto") // DTO package
        .addAnnotatedClass(MovieDto.class) // DTO
        .buildSessionFactory(serviceRegistry);
	}
	
	public void end() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
	
	public void testBasicUsage() throws JSONException {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		/*Serializable genId = */session.update(new MovieDto(new JSONObject("{\"id\":86,\"title\":\"Test2\",\"description\":\"Ceci est une description\",\"style\":\"On s'en fout\",\"time\":\"pareil\",\"downloaded\":false,\"deleted\":false}")));
		session.getTransaction().commit();
		session.close();
		//System.out.println("Generated id : " + genId.toString());

		// now lets pull events from the database and list them
		session = sessionFactory.openSession();
        session.beginTransaction();
        @SuppressWarnings("unchecked")
		List<MovieDto> result = session.createQuery("from MovieDto").list();
		for (MovieDto movie : (List<MovieDto>) result ) {
			System.out.println(movie.getTitle());
		}
        session.getTransaction().commit();
        session.close();
	}
}
