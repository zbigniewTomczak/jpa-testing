package tomczak.hibernate;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import tomczak.Employee;
import tomczak.rules.rules.SelectFromAllEntityTables;
import tomczak.rules.rules.StandaloneEntityManagerFactory;
import tomczak.rules.rules.StandaloneTransactionalEntityManager;

public class JpaTest {

    @ClassRule
    public static StandaloneEntityManagerFactory emf = new StandaloneEntityManagerFactory("hibernate-jpa");

    public StandaloneTransactionalEntityManager em = new StandaloneTransactionalEntityManager(emf);

    @Rule
    public TestRule chain= RuleChain
            .outerRule(em).around(new SelectFromAllEntityTables(em));

	@Test
	public void test() {
        Employee e = new Employee();
        em.persist(e);
        Assert.assertNotNull(e.getId());
	}

}
