package DB;

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
    public static EntityManagerFactory getEntityManagerFactory(){
        return emf;
    }
}
