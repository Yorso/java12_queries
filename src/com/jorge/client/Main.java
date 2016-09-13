package com.jorge.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.jorge.entity.Guide;
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
public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		BasicConfigurator.configure(); // Necessary for configure log4j. It must be the first line in main method
	       					           // log4j.properties must be in /src directory

		Logger  logger = Logger.getLogger(Main.class.getName());
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
			 * Querying
			 */
			
			logger.debug("Selecting all fields from Guide");
			Query query = em.createQuery("select guide from Guide as guide"); // JPQL query converted in SQL at Runtime. ...from Guide as guide == ...from Guide guide
																			  // We are selecting all fields from Guide
																			  // HQL: select guide from com.jorge.entity.Guide as guide
																			  // SQL: select guide0_.id as id1_1_, guide0_.name as name2_1_, guide0_.salary as salary3_1_, guide0_.staff_id as staff_id4_1_ from guide guide0_
			List<Guide> guides = query.getResultList();
			
			for(Guide guide : guides)
				System.out.println("ROW: " + guide);
			
			// ***********************************
			
			logger.debug("Selecting name field from Guide");
			query = em.createQuery("select guide.name from Guide as guide"); // JPQL query converted in SQL at Runtime
																			 // We are selecting name field from Guide
																			 // HQL: select guide.name from com.jorge.entity.Guide as guide
																			 // SQL: select guide0_.name as col_0_0_ from guide guide0_
			List<String> names = query.getResultList();
			
			for(String name : names)
				System.out.println("NAME: " + name);
			
			
			
			/*******************************************
			 * Filtering queries
			 */
			
			logger.debug("Selecting all fields from Guide where salary is 1200");
			query = em.createQuery("select guide from Guide guide where guide.salary = 1200"); // JPQL query converted in SQL at Runtime
																			 				   // We are selecting all field from Guide where salary is 1200
																							   // HQL: select guide from com.jorge.entity.Guide guide where guide.salary = 1200
																					           // SQL: select guide0_.id as id1_1_, guide0_.name as name2_1_, guide0_.salary as salary3_1_, guide0_.staff_id as staff_id4_1_ from guide guide0_ where guide0_.salary=1200	
			guides = query.getResultList();
			
			for(Guide guide : guides)
				System.out.println("ROW AGAIN: " + guide);
			
			
			
			/*******************************************
			 * Reporting queries => setting results in objects
			 */
			
			logger.debug("Setting results in objects");
			query = em.createQuery("select guide.name, guide.salary from Guide guide"); // JPQL query converted in SQL at Runtime
																			 			// We are selecting name and salary fields from Guide
																						// HQL: select guide.name, guide.salary from com.jorge.entity.Guide guide
																						// SQL: select guide0_.name as col_0_0_, guide0_.salary as col_1_0_ from guide guide0_
			List<Object[]> resultList = query.getResultList();
			
			for(Object[] objects : resultList)
				System.out.println("Object[] {objects[0]: " + objects[0] + ", objects[1]: " + objects[1] + "}"); // Object[] {objects[0]: Homer Simpson, objects[1]: 1200}
																												 // Object[] {objects[0]: Marge Simpson, objects[1]: 1600}
			
			
			
			/*******************************************
			 * Dynamic query
			 */
			
			String name = "Homer Simpson"; // Simulating dynamic parameter. This name must exist in DB
			
			logger.debug("Selecting row from Guide where name is set dynamically");
			query = em.createQuery("select guide from Guide guide where guide.name = '" + name + "'"); // JPQL query converted in SQL at Runtime
																			 				           // We are selecting row from Guide where name is set dynamically
																									   // HQL: select guide from com.jorge.entity.Guide guide where guide.name = 'Homer Simpson'
																									   // SQL: select guide0_.id as id1_1_, guide0_.name as name2_1_, guide0_.salary as salary3_1_, guide0_.staff_id as staff_id4_1_ from guide guide0_ where guide0_.name='Homer Simpson'
			Guide guide = (Guide) query.getSingleResult();
			
			System.out.println("SINGLE RESULT: " + guide);
			
			// ***********************************
			
			logger.debug("Selecting row from Guide where name is set dynamically");
			query = em.createQuery("select guide from Guide guide where guide.name = :name"); // JPQL query converted in SQL at Runtime
																			 				  // We are selecting row from Guide where name is set dynamically
																							  // HQL: select guide from com.jorge.entity.Guide guide where guide.name = :name
																							  // SQL: select guide0_.id as id1_1_, guide0_.name as name2_1_, guide0_.salary as salary3_1_, guide0_.staff_id as staff_id4_1_ from guide guide0_ where guide0_.name=?
			query.setParameter("name", "Homer Simpson"); // Simulating dynamic parameter. This name must exist in DB
			guide = (Guide) query.getSingleResult();
			
			// Chaining methods
			//guide = (Guide) em.createQuery("select guide from Guide guide where guide.name = :name").setParameter("name", "Homer Simpson").getSingleResult();
			
			System.out.println("SINGLE RESULT AGAIN: " + guide);
			
			
			
			/*******************************************
			* Wildcards
			*/
			
			logger.debug("Selecting row from Guide using wildcards");
			query = em.createQuery("select guide from Guide guide where guide.name like 'M%'"); // JPQL query converted in SQL at Runtime
				           																	    // We are selecting rows whose name starts with M
																								// like: checks if a specified string matches with a specified pattern
																								// %: wildcard (a substitute for zero or more characters)
																								// HQL: select guide from com.jorge.entity.Guide guide where guide.name like 'M%'
																								// SQL: select guide0_.id as id1_1_, guide0_.name as name2_1_, guide0_.salary as salary3_1_, guide0_.staff_id as staff_id4_1_ from guide guide0_ where guide0_.name like 'M%'
			guides = query.getResultList();
			
			for(Guide guide2 : guides)
				System.out.println("WILDCARD: " + guide2); // WILDCARD: Guide [id=2, staffId=GD200331, name=Marge Simpson, salary=1600]
			
			
			
			
			/*******************************************
			* Native SQL Query
			*/
			
			logger.debug("Using native SQL query");
			query = em.createNativeQuery("select * from guide", Guide.class); // We are selecting all fields from Guide
																			  // SQL: select * from guide
			guides = query.getResultList();
			
			for(Guide guide2 : guides)
				System.out.println("NATIVE SQL QUERY: " + guide2);
			
			
			
			
			/*******************************************
			* Named Query. We need a orm.xml file in /META-INF folder to do this. Query is defined in orm.xml
			*/
			
			logger.debug("Named query");
			guides = em.createNamedQuery("findByGuide") // Same name than in orm.xml file (<named-query name="findByGuide">)
					 .setParameter("name",  "Homer Simpson") // Must be in DB
					 .getResultList(); // SQL: select guide0_.id as id1_1_, guide0_.name as name2_1_, guide0_.salary as salary3_1_, guide0_.staff_id as staff_id4_1_ from guide guide0_ where guide0_.name=?
			
			for(Guide guide2 : guides)
				System.out.println("NAMED QUERY: " + guide2); // NAMED QUERY: Guide [id=1, staffId=GD200331, name=Homer Simpson, salary=1200]
			
			
			
			
			/*******************************************
			* Aggregate Functions
			*/
			
			logger.debug("Aggregate Functions - No aggregate function in this example");
			int numOfGuides = em.createQuery("select guide from Guide guide").getResultList().size(); // JPQL query converted in SQL at Runtime
				           																	    	  // We are getting the number of guides in DB
																									  // HQL: select guide from com.jorge.entity.Guide guide
																									  // SQL: select guide0_.id as id1_1_, guide0_.name as name2_1_, guide0_.salary as salary3_1_, guide0_.staff_id as staff_id4_1_ from guide guide0_
			
			System.out.println("NUMBER OF GUIDES WITHOUT AGGREGATE FUNCTIONS: " + numOfGuides); // NUMBER OF GUIDES WITHOUT AGGREGATE FUNCTIONS: 2
			
			// ***********************************
			
			// Better than above
			logger.debug("Agregate Functions - COUNT");
			query = em.createQuery("select count(guide) from Guide guide"); // JPQL query converted in SQL at Runtime
   																	    	// We are getting the number of guides in DB
																			// HQL: select count(guide) from com.jorge.entity.Guide guide
																			// SQL: select count(guide0_.id) as col_0_0_ from guide guide0_
			
			Long numOfGuides2 = (Long) query.getSingleResult();
			
			System.out.println("NUMBER OF GUIDES AGAIN USING COUNT AGGREGATE FUNCTION: " + numOfGuides2); // NUMBER OF GUIDES AGAIN USING COUNT AGGREGATE FUNCTION: 2
			
			// ***********************************
			
			// Getting maximum salary
			logger.debug("Agregate Functions - MAX");
			query = em.createQuery("select max(guide.salary) from Guide guide"); // JPQL query converted in SQL at Runtime
   																	    	// We are getting the number of guides in DB
																			// HQL: select max(guide.salary) from com.jorge.entity.Guide guide
																		    // SQL: select max(guide0_.salary) as col_0_0_ from guide guide0_
			
			Integer maximumSalary = (Integer) query.getSingleResult();
			
			System.out.println("MAXIMUM SALARY USING MAX AGGREGATION FUNCTION: " + maximumSalary); // MAXIMUM SALARY USING MAX AGGREGATION FUNCTION: 1600
			
			
			
			
			/*******************************************
			* Joining associations - First running MainJopiningAssociations.java
			*/
			
			// Join (Inner Join)
			logger.debug("Join (Inner Join)");
			query = em.createQuery("select student from Student student join student.guide guide"); // JPQL query converted in SQL at Runtime
																									// student.guide => this 'guide' of 'student.guide' is the name of the attribute in Student.java class @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
																									//																			                           @JoinColumn(name="guide_id")
																									//																			                           private Guide guide; <= refers to this 'guide'
																									//
   																	    						    // We are making a inner join with student and guide entities to get students with guide value assigned
																									// HQL: select student from com.jorge.entity.Student student join student.guide guide
																									// SQL: select student0_.id as id1_0_, student0_.enrollment_id as enrollme2_0_, student0_.guide_id as guide_id4_0_, student0_.name as name3_0_ from Student student0_ inner join guide guide1_ on student0_.guide_id=guide1_.id
			
			List<Student> students = query.getResultList();
			
			for(Student student : students)
				System.out.println("STUDENT - INNER JOIN: " + student); // STUDENT - INNER JOIN: Student [id=1, enrollmentId=ST109883, name=Bart Simpson, guide=Guide [id=1, staffId=GD200331, name=Homer Simpson, salary=1200]]
																		// STUDENT - INNER JOIN: Student [id=2, enrollmentId=ST109884, name=Lisa Simpson, guide=Guide [id=2, staffId=GD200332, name=Marge Simpson, salary=1600]]
																		// Sheldon doesn't appear because he doesn't have guide value assigned (guide_id = null, check database)
			
			// ***********************************
			
			// Left Join = Left Outer Join
			logger.debug("Left Join = Left Outer Join");
			query = em.createQuery("select student from Student student left join student.guide guide"); // JPQL query converted in SQL at Runtime
																									     // student.guide => this 'guide' of 'student.guide' is the name of the attribute in Student.java class @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
																										 //																			                            @JoinColumn(name="guide_id")
																										 //																			                            private Guide guide; <= refers to this 'guide'
																										 //
	   																	    						     // We are making a left outer join with student and guide entities to get students, getting Sheldom information too though his guide_id is null (because of it is a left outer join and student table is on the left)
																										 // HQL: select student from com.jorge.entity.Student student left join student.guide guide
																										 // SQL: select student0_.id as id1_0_, student0_.enrollment_id as enrollme2_0_, student0_.guide_id as guide_id4_0_, student0_.name as name3_0_ from Student student0_ left outer join guide guide1_ on student0_.guide_id=guide1_.id
			students = query.getResultList();
			
			for(Student student : students)
				System.out.println("STUDENT - LEFT JOIN: " + student); // STUDENT - LEFT JOIN: Student [id=1, enrollmentId=ST109883, name=Bart Simpson, guide=Guide [id=1, staffId=GD200331, name=Homer Simpson, salary=1200]]
																	   // STUDENT - LEFT JOIN: Student [id=2, enrollmentId=ST109884, name=Lisa Simpson, guide=Guide [id=2, staffId=GD200332, name=Marge Simpson, salary=1600]]
																	   // STUDENT - LEFT JOIN: Student [id=3, enrollmentId=1299384FFG, name=Sheldon Cooper, guide=null]
			
			// ***********************************
			
			// Right Join = Right Outer Join
			logger.debug("Right Join = Right Outer Join");
			query = em.createQuery("select student from Student student right join student.guide guide"); // JPQL query converted in SQL at Runtime
																									      // student.guide => this 'guide' of 'student.guide' is the name of the attribute in Student.java class @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
																										  //																			                            @JoinColumn(name="guide_id")
																										  //																			                            private Guide guide; <= refers to this 'guide'
																										  //
	   																	    						      // We are making a right outer join with student and guide entities to get students. This time we don't get Sheldom information because his guide_id is null (because of it is a right outer join and guide table is on the right)
																										  // HQL: select student from com.jorge.entity.Student student right join student.guide guide
																										  // SQL: select student0_.id as id1_0_, student0_.enrollment_id as enrollme2_0_, student0_.guide_id as guide_id4_0_, student0_.name as name3_0_ from Student student0_ right outer join guide guide1_ on student0_.guide_id=guide1_.id
			students = query.getResultList();	
			
			for(Student student : students)
				System.out.println("STUDENT - RIGHT JOIN: " + student); // STUDENT - RIGHT JOIN: Student [id=1, enrollmentId=ST109883, name=Bart Simpson, guide=Guide [id=1, staffId=GD200331, name=Homer Simpson, salary=1200]]
																		// STUDENT - RIGHT JOIN: Student [id=2, enrollmentId=ST109884, name=Lisa Simpson, guide=Guide [id=2, staffId=GD200332, name=Marge Simpson, salary=1600]]
			
			// ***********************************
			
			// Join Fetch = Inner Join Fetch
			logger.debug("Join Fetch = Inner Join Fetch");
			
			//query = em.createQuery("select guide from Guide guide join guide.students student"); // Without using 'fetch' in the query, it doesn't initialize the collection of students (the same action that assign fetch=FetchType.LAZY to 'private Set<Student> students' collection in Guide.java class)
			
			query = em.createQuery("select guide from Guide guide join fetch guide.students student"); // Using 'fetch' in the query initializes the collection of students (the same action that assign fetch=FetchType.EAGER to 'private Set<Student> students' collection in Guide.java class)
																									   // HQL: select guide from com.jorge.entity.Guide guide join fetch guide.students student
																									   // SQL: select guide0_.id as id1_1_0_, students1_.id as id1_0_1_, guide0_.name as name2_1_0_, guide0_.salary as salary3_1_0_, guide0_.staff_id as staff_id4_1_0_, students1_.enrollment_id as enrollme2_0_1_, students1_.guide_id as guide_id4_0_1_, students1_.name as name3_0_1_, students1_.guide_id as guide_id4_1_0__, students1_.id as id1_0_0__ from guide guide0_ inner join Student students1_ on guide0_.id=students1_.guide_id
			
			guides = query.getResultList();
			
			for(Guide guide2 : guides)
				System.out.println("GUIDE - JOIN FETCH: " + guide2); // GUIDE - JOIN FETCH: Guide [id=1, staffId=GD200331, name=Homer Simpson, salary=1200]
																	 // GUIDE - JOIN FETCH: Guide [id=2, staffId=GD200332, name=Marge Simpson, salary=1600]
			
			
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
  