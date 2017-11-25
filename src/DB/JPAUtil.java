package DB;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by douglas on 11/7/17.
 */
public class JPAUtil {
    private  static  final EntityManagerFactory emf;

    static {
        try {
            emf= Persistence.createEntityManagerFactory("NewPersistenceUnit");
        }catch (Throwable e){
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}
