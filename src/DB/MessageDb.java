package DB;

import BO.Message;
import BO.User;
import org.hibernate.Session;

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
        Session sess = JPAUtil.getSession();
        try {
            sess.getTransaction().begin();
            message=new Message();
            message.setContent(Content);
            message.setType(type);
            message.setDate(date);
            User sender = (User) sess.find(User.class, senderId);
            User recipient = (User) sess.find(User.class,  recipientId);
            message.setSender(sender);
            message.setRecipient(recipient);



            sess.persist(message);
            sess.getTransaction().commit();
        }catch (NoResultException e2){
            sess.getTransaction().rollback();
            System.out.println("Users Not Found");
        }catch (PersistenceException e) {

            System.out.println("Cant not save message");
            sess.getTransaction().rollback();


        }finally {
            sess.close();
        }
        return message;

    }

    public List<Message> getMessagesByUserId(long id){
        List<Message> listOfmessage= null;
        Session sess = JPAUtil.getSession();

        try {
            sess.getTransaction().begin();

            listOfmessage=  sess.createNamedQuery("Message.FindMessgesByRecipient").setParameter("id",id).getResultList();

            sess.getTransaction().commit();

        }catch (NoResultException e){
            sess.getTransaction().rollback();
        }finally {
            sess.close();
        }
        return  listOfmessage;
    }


    public List<Message> getChatHistory(long id,long id2){
        List<Message> listOfmessage= null;
        Session sess = JPAUtil.getSession();

        try {
            sess.getTransaction().begin();

            listOfmessage=  sess.createNamedQuery("Message.FindMessgesByRecipientAndSender")
                    .setParameter("id",id).setParameter("id2",id2).getResultList();
            sess.getTransaction().commit();

        }catch (NoResultException e){
            sess.getTransaction().rollback();
        }finally {
            sess.close();
        }
        return  listOfmessage;
    }


}


