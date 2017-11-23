package DB;

import BO.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by douglas on 11/4/17.
 */
public class UserDb {
    private EntityManager entityManager;

    public boolean createUser(String name, String email, String password){
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            entityManager.getTransaction().begin();
            entityManager.persist(user);

            entityManager.getTransaction().commit();
            return true;
        }catch (PersistenceException e){
            System.out.println("Transaction aborted");

            entityManager.getTransaction().rollback();
            return false;
        }finally {
            entityManager.close();
            return true;
        }
    }

    public User checkLogin(String email , String password) {
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        User currentUser=null;
        try {
            currentUser = (User) entityManager.createNamedQuery("User.CheckLogin")
                    .setParameter("email", email)
                    .setParameter("password", password).getSingleResult();
        }catch (NoResultException e0){
            return null;
        } catch (PersistenceException e1) {
            System.out.println("Transaction aborted");

            return null;

        } finally {
            entityManager.close();
        }
        return currentUser;

    }
    public  Boolean removeFriend(long userId,long friendId){
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        User user=  entityManager.find(User.class,userId);
        User friend = entityManager.find(User.class,friendId);
        user.removeFriend(friend);
        friend.removeFriend(user);
        return UpdateFriedList(user ,friend);
    }
    public boolean addFriend(long userId, long friendId) {
        if(userId==friendId){
            return false;
        }
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        User user = entityManager.find(User.class, userId);
        User friend = entityManager.find(User.class, friendId);
        user.addFriend(friend);
        friend.addFriend(user);
        return UpdateFriedList(user, friend);
    }

    private boolean UpdateFriedList(User user, User friend){
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(friend);
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (PersistenceException e){
            entityManager.getTransaction().rollback();
            return false;
        }finally {
            entityManager.close();
        }
        return true;
    }


    public User getUserById(long id){
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        User user = (User) entityManager.createNamedQuery("User.FindUserById").setParameter("id",id).getSingleResult();
        entityManager.close();
        return user;
    }

   /* public List<User> getUsersByName(String name){
        List<User> users = null;
        try {
            entityManager.getTransaction().begin();
            users=(List<User>) entityManager.createNamedQuery("User.FindUsersByName").setParameter("name",name).getResultList();
            entityManager.getTransaction().commit();
        }catch (PersistenceException e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }
        return users;
    }*/

    public Map<String,Long> getUsers(){
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<User> users = null;
        List<Object[]> objects;
        try {
            objects= entityManager.createNamedQuery("User.FindUsers").getResultList();
            Map<String,Long> resultList = new HashMap<>(objects.size());
            for (Object[] result:objects ) {
                resultList.put((String) result[0],(long)result[1]);
            }

            return resultList;
        }catch (PersistenceException e){
            entityManager.close();
        }
        return null;
    }



    public List<User> getFriendList(long id){
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<User> friendList =null;
        try {
             friendList = entityManager.createNamedQuery("User.FindFriendsByUserId").setParameter("id",id).getResultList();
        }catch (Exception e){
            System.out.println("some error");
        }finally {
            entityManager.close();

        }
        return friendList;
    }

    public long getUserIdByEmail(String email){
        return (long) entityManager.createNamedQuery("User.FindUserIdByEmail").setParameter("email",email).getSingleResult();
    }

}