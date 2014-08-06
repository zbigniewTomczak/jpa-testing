package tomczak.jpa.test.runner;

import org.junit.Ignore;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import javax.persistence.PersistenceUnit;

@Ignore
public class StandaloneJPATestRunnerTemplate extends BlockJUnit4ClassRunner {

    private String unitName;

    public StandaloneJPATestRunnerTemplate(Class<?> clazz) throws InitializationError {
        super(clazz);
        PersistenceUnit r = clazz.getAnnotation(PersistenceUnit.class);
        if (r == null) {
            throw new InitializationError("The test class "+ clazz.getCanonicalName() +" must be annotated with @PersistenceUnit(unitName=\"persistenceUnitName\") when using " + StandaloneJPATestRunnerTemplate.class.getName() +" test runner.");
        }
        unitName = r.unitName();
    }

    @Override
    protected Statement classBlock(final RunNotifier notifier) {
        final Statement statement = super.classBlock(notifier);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.println("beforeClass");
                try {
                    statement.evaluate();
                } finally {
                    System.out.println("afterClass");
                }
            }
        };

    }

    @Override
    protected void  runChild(FrameworkMethod child, RunNotifier notifier) {
        System.out.println("before");
        super.runChild(child, notifier);
        System.out.println("after");
    }
}
