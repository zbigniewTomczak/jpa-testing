package tomczak.hibernate;

import org.apache.derby.tools.JDBCDisplayUtil;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runners.MethodSorters;
import tomczak.Company;
import tomczak.Employee;
import tomczak.rules.rules.SelectFromAllEntityTables;
import tomczak.rules.rules.StandaloneEntityManagerFactory;
import tomczak.rules.rules.StandaloneTransactionalEntityManager;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaTest {

    @ClassRule
    public static StandaloneEntityManagerFactory emf = new StandaloneEntityManagerFactory("hibernate-jpa");

    public StandaloneTransactionalEntityManager em = new StandaloneTransactionalEntityManager(emf);

    @Rule
    public TestRule chain= RuleChain
            .outerRule(em).around(new SelectFromAllEntityTables(em));

	@Test
	public void _1createUserWithCompany() {
        Employee e = new Employee();
        Company c  = new Company("Cisco");
        Set<Company> companies = new HashSet<>();
        companies.add(c);
        e.setCompanies(companies);
        em.persist(e);
        Assert.assertNotNull(e.getId());
        Assert.assertNotNull(c.getId());


	}

    @Test
    public void _2changeCompanyName() {
        TypedQuery<Employee> select = em.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.companies", Employee.class);
        List<Employee> resultList = select.getResultList();
        Assert.assertTrue(resultList.size() > 0);
        Employee e = resultList.get(0);
        Assert.assertTrue(e.getCompanies().size() > 0);
        e.getCompanies().iterator().next().setName("Microsoft");
        em.persist(e);
    }

    @Test
    public void _3checkComapnyNameChanged() {
        TypedQuery<Employee> select = em.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.companies", Employee.class);
        List<Employee> resultList = select.getResultList();
        Assert.assertTrue(resultList.size() > 0);
        Employee e = resultList.get(0);
        Assert.assertTrue(e.getCompanies().size() > 0);
        Assert.assertEquals("Microsoft", e.getCompanies().iterator().next().getName());
        em.persist(e);
    }

    @Test
    public void _4readAndSaveEmployeeWithLazyComapany() {
        TypedQuery<Employee> select = em.createQuery("SELECT e FROM Employee e", Employee.class);
        List<Employee> resultList = select.getResultList();
        Assert.assertTrue(resultList.size() > 0);
        Employee e = resultList.get(0);
        em.persist(e);
    }

    @Test
    public void _5checkCompanyIsStillThere() {
        TypedQuery<Employee> select = em.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.companies", Employee.class);
        List<Employee> resultList = select.getResultList();
        Assert.assertTrue(resultList.size() > 0);
        Employee e = resultList.get(0);
        Assert.assertTrue(e.getCompanies().size() > 0);
    }

    @Test
    public void _6removeComapany() {
        TypedQuery<Employee> select = em.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.companies", Employee.class);
        List<Employee> resultList = select.getResultList();
        Assert.assertTrue(resultList.size() > 0);
        Employee e = resultList.get(0);
        Assert.assertTrue(e.getCompanies().size() > 0);
        e.setCompanies(Collections.<Company>emptySet());
        em.persist(e);
    }

    @Test
    public void _7checkCompanyRemoved() {
        TypedQuery<Employee> select = em.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.companies", Employee.class);
        List<Employee> resultList = select.getResultList();
        Assert.assertTrue(resultList.size() > 0);
        Employee e = resultList.get(0);
        Assert.assertFalse(e.getCompanies().size() > 0);
    }
}
