package DB;

import BO.Message;
import BO.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

/**
 * Created by douglas on 11/4/17.
 */
public class MessageDb {
    private EntityManager entityManager;


    public boolean createMessage(String Content, Date date, String type, long senderId, long  recipientId){

        Message message = null;
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            message=new Message();
            message.setContent(Content);
            message.setType(type);
            message.setDate(date);
            User sender = (User) entityManager.find(User.class, senderId);
            User recipient = (User) entityManager.find(User.class,  recipientId);
            message.setSender(sender);
            message.setRecipient(recipient);



            entityManager.persist(message);
            entityManager.getTransaction().commit();
            return  true;
        }catch (NoResultException e2){
                entityManager.getTransaction().rollback();
                System.out.println("Users Not Found");
                return  false;
        }catch (PersistenceException e) {

            System.out.println("Cant not save message");
            entityManager.getTransaction().rollback();
            return  false;


        }finally {
            entityManager.close();
        }

    }

    public List<Message> getMessagesByUserId(long id){
        List<Message> listOfmessage= null;
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

          listOfmessage=  entityManager.createNamedQuery("Message.FindMessgesByRecipient").setParameter("id",id).getResultList();

            entityManager.getTransaction().commit();

        }catch (NoResultException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }
        return  listOfmessage;
    }


}


