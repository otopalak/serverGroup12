package generator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;


public class IDGenerator extends SequenceStyleGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Long generatedID = (Long) super.generate(session, object);
        Long random = ThreadLocalRandom.current().nextLong(100000, 999999);
        return 10000000*generatedID+random;

    }
}
