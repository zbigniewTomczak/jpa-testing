package tomczak.eclipselink;

import org.apache.derby.tools.JDBCDisplayUtil;
import org.junit.*;
import tomczak.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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
        emf = Persistence.createEntityManagerFactory("eclipselink-jpa");
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
        Connection conn = em.unwrap(Connection.class);
        Statement st = conn.createStatement();
        for(EntityType<?> et : emf.getMetamodel().getEntities()) {
            String query = "SELECT * FROM " + et.getName();
            System.out.println("> " + query);
            st.execute(query);
            JDBCDisplayUtil.DisplayResults(System.out, st, conn);
        }

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
