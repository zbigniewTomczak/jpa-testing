package tomczak.rules.rules;

import org.apache.derby.tools.JDBCDisplayUtil;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.rules.ExternalResource;

import javax.persistence.PersistenceException;
import javax.persistence.metamodel.EntityType;
import java.sql.Connection;
import java.sql.SQLException;

public class SelectFromAllEntityTables extends ExternalResource {

    private StandaloneTransactionalEntityManager em;

    public SelectFromAllEntityTables(StandaloneTransactionalEntityManager em) {
        this.em = em;
    }

    private Work selectWork = new Work() {
        @Override
        public void execute(Connection connection) throws SQLException {
            java.sql.Statement st = connection.createStatement();
            for (EntityType<?> et : em.getEntityManagerFactory().getMetamodel().getEntities()) {
                String query = "SELECT * FROM " + et.getName();
                System.out.println("> " + query);
                st.execute(query);
                JDBCDisplayUtil.DisplayResults(System.out, st, connection);
            }
        }
    };

    @Override
    protected void after() {
        try {
            Connection conn = em.unwrap(Connection.class);
            selectWork.execute(conn);
        } catch(SQLException e) {
           // do nothing
        } catch (PersistenceException e) {
            // do nothing
        }
        try {
            Session session = em.unwrap(Session.class);
            session.doWork(selectWork);
        } catch(PersistenceException e) {
            // do nothing
        }
    }
}
