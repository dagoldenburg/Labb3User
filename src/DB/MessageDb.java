package DB;

import BO.Message;
import BO.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by douglas on 11/4/17.
 */
public class MessageDb {


    public Message createMessage(String Content,Date date, String type, long senderId, long  recipientId){

        Message message = null;
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
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
        }catch (NoResultException e2){
            entityManager.getTransaction().rollback();
            System.out.println("Users Not Found");
        }catch (PersistenceException e) {

            System.out.println("Cant not save message");
            entityManager.getTransaction().rollback();


        }finally {
            entityManager.close();
        }
        return message;

    }

    public List<Message> getMessagesByUserId(long id){
        List<Message> listOfmessage= null;
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

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


    public List<Message> getChatHistory(long id,long id2){
        List<Message> listOfmessage= null;
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            entityManager.getTransaction().begin();

            listOfmessage=  entityManager.createNamedQuery("Message.FindMessgesByRecipientAndSender")
                    .setParameter("id",id).setParameter("id2",id2).getResultList();
            entityManager.getTransaction().commit();

        }catch (NoResultException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }
        return  listOfmessage;
    }


}


