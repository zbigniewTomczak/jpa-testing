package tomczak.hibernate;

import org.apache.derby.tools.JDBCDisplayUtil;
import org.hibernate.internal.SessionImpl;
import org.hibernate.jdbc.Work;
import org.junit.*;
import tomczak.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.Session;
public class JpaTest {

	
	
	@Test
	public void test() {
        Employee e = new Employee();
        em.persist(e);
        Assert.assertNotNull(e.getId());
	}
	
	private EntityManager em;
	private static EntityManagerFactory emf;
	private EntityTransaction tx;
	
	@BeforeClass
	public static void setUp() {
        emf = Persistence.createEntityManagerFactory("hibernate-jpa");
	}
	
	@AfterClass
	public static void closePersistence() {
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
	}
	
	@Before
	public void before() {
		em = emf.createEntityManager();
		tx = em.getTransaction();
		tx.begin();
	}
	
	@After
	public void after() throws SQLException {
        em.flush();
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement st = connection.createStatement();
                for(EntityType<?> et : emf.getMetamodel().getEntities()) {
                    String query = "SELECT * FROM " + et.getName();
                    System.out.println("> " + query);
                    st.execute(query);
                    JDBCDisplayUtil.DisplayResults(System.out, st, connection);
                }
            }
        });


		if (tx != null && tx.isActive()) {
			tx.commit();
		}
		if (em != null && em.isOpen()) {
			em.close();
		}
		if (emf != null && emf.isOpen()) {
			emf.getCache().evictAll();
		}
	}
}
