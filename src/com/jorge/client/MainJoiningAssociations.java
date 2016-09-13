package com.jorge.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.jorge.entity.Student;

/**
 * Querying entities
 * 
 * JPQL: JPQL queries entities. It is database independent. It is converted in SQL at Runtime
 * 		 Java Persistence Query Language -> select guide.id, guide.name from Guide as guide
 * 		 Guide is the name of the entity (public class Guide{...})
 * 		 id and name are the name of attributes in Guide.java
 *  	 JPQL is case sensitive: Guide can not be guide/GUIDE/GuIdE
 *                               guide.id can not be guide.Id/guide.ID
 *  	 JPQL is a heavily-inspired-by subset of HQL.
 *  	 JPQL is a portable query language that means if you change the provider, you don't have to change the queries
 * 
 * SQL: SQL queries database tables
 * 	    Structured Query Language -> select guide.id as id, guide.name as name from Guide as guide
 *      Guide is the name of the table
 *      id and name are the names of the columns
 *      SQL is not case sensitive (case insensitive): Guide=guide=GUIDE=GuIdE
 *                                                    guide.id=guide.Id=guide.ID
 *      
 *      
 * HQL: Hibernate Query Language. It is like JPQL. It is also database independent and converted in SQL at Runtime
 *	    A JPQL query is always a valid HQL query, the reverse is not true however
 *
 */
public class MainJoiningAssociations {

	public static void main(String[] args) {
		BasicConfigurator.configure(); // Necessary for configure log4j. It must be the first line in main method
	       					           // log4j.properties must be in /src directory

		Logger  logger = Logger.getLogger(MainJoiningAssociations.class.getName());
		logger.debug("log4j configured correctly and logger set");

		// How make the same things with JPA and Hibernate (commented)
		logger.debug("creating entity manager factory");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("helloworld"); // => SessionFactory sf = HibernateUtil.getSessionFactory(); HibernateUtil is a class created by us.
																						 // Persistence is imported from javax.persistence.Persistence package, it is not a class created by us
																						 // "helloworld" persistence unit name is the same name than "<persistence-unit name="helloworld"...>" element in persistence.xml file 
		logger.debug("creating entity manager");
		EntityManager em = emf.createEntityManager(); // => Session session = sf.openSession();
		
		logger.debug("getting transaction");
		EntityTransaction txn = em.getTransaction(); // => Transaction txn = session.getTransaction();
		
		try{
			logger.debug("beginning transaction");
			txn.begin();
			
			/*******************************************
			* Joining associations example - Run this main class first
			*/
			
			Student student = new Student("1299384FFG", "Sheldon Cooper", null); // No guide_id for this student (null). There is no guide for him
			em.persist(student);
			
			logger.debug("making commit");
			txn.commit();
			
			
		}
		catch (Exception e) {
			if (txn != null) {
				logger.error("something was wrong, making rollback of transactions");
				txn.rollback(); // If something was wrong, we make rollback
			}
			logger.error("Exception: " + e.getMessage().toString());
		} finally {
			if (em != null) { // => if (session != null) {
				logger.debug("close session");
				em.close(); // => session.close();
			}
		}
	}

}
  