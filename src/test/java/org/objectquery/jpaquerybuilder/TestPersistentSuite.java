package org.objectquery.jpaquerybuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.objectquery.jpaquerybuilder.domain.Dog;
import org.objectquery.jpaquerybuilder.domain.Home;
import org.objectquery.jpaquerybuilder.domain.Home.HomeType;
import org.objectquery.jpaquerybuilder.domain.Person;

@RunWith(Suite.class)
@SuiteClasses(TestPersistentSelect.class)
public class TestPersistentSuite {

	static EntityManagerFactory entityManagerFactory;

	private static void initData() {
		EntityManager entityManager;
		entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();

		Home tomHome = new Home();
		tomHome.setAddress("homeless");
		tomHome.setType(HomeType.HOUSE);
		tomHome = entityManager.merge(tomHome);

		Person tom = new Person();
		tom.setName("tom");
		tom.setHome(tomHome);
		tom = entityManager.merge(tom);

		Home dudHome = new Home();
		dudHome.setAddress("moon");
		dudHome.setType(HomeType.HOUSE);
		dudHome = entityManager.merge(dudHome);

		Person tomDud = new Person();
		tomDud.setName("tomdad");
		tomDud.setHome(dudHome);
		tomDud = entityManager.merge(tomDud);

		Person tomMum = new Person();
		tomMum.setName("tommum");
		tomMum.setHome(dudHome);

		tomMum = entityManager.merge(tomMum);

		Home dogHome = new Home();
		dogHome.setAddress("royal palace");
		dogHome.setType(HomeType.KENNEL);
		dogHome.setPrice(1000000);
		dogHome.setWeight(30);

		dogHome = entityManager.merge(dogHome);

		Dog tomDog = new Dog();
		tomDog.setName("cerberus");
		tomDog.setOwner(tom);
		tomDog.setHome(dogHome);
		tomDog = entityManager.merge(tomDog);

		tom.setDud(tomDud);
		tom.setMum(tomMum);
		tom.setDog(tomDog);
		entityManager.persist(tom);
		entityManager.getTransaction().commit();
		entityManager.close();

	}

	@BeforeClass
	public static void startup() {
		entityManagerFactory = Persistence.createEntityManagerFactory("test");
		initData();
	}

	@AfterClass
	public static void shutdown() {
		if (entityManagerFactory != null)
			entityManagerFactory.close();
		entityManagerFactory = null;
	}

}
