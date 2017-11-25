package DB;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by douglas on 11/7/17.
 */
public class JPAUtil {
    private  static  final EntityManagerFactory emf;
    private static final SessionFactory sessFac;

    static {
        try {
            Configuration cfg = new Configuration();
            sessFac = cfg.buildSessionFactory();
            Session sess = sessFac.openSession();
                    emf= Persistence.createEntityManagerFactory("NewPersistenceUnit");
        }catch (Throwable e){
            throw new ExceptionInInitializerError(e);
        }
    }
    public static Session getSession(){
        return sessFac.openSession();
    }

    public static EntityManagerFactory getEntityManagerFactory(){
        return emf;
    }
}
