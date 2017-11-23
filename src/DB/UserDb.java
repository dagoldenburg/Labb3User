package DB;

import BO.User;
import org.hibernate.PersistentObjectException;
import org.hibernate.Session;

import javax.persistence.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by douglas on 11/4/17.
 */
public class UserDb {

    public boolean createUser(String name, String email, String password,Date date){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setBirthday(date);
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

    public User checkLogin(String email ,String password) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
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
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/socialdb","root","admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM friend_list WHERE list_owner_id=? and friend_id=?");
            preparedStatement.setLong(1,userId);
            preparedStatement.setLong(2,friendId);
            preparedStatement.executeUpdate();
            preparedStatement = conn.prepareStatement("DELETE FROM friend_list WHERE list_owner_id=? and friend_id=?");
            preparedStatement.setLong(1,friendId);
            preparedStatement.setLong(2,userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
       /* if(userId==friendId){
            return false;
        }
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        User user=  entityManager.find(User.class,userId);
        User friend = entityManager.find(User.class,friendId);
        user.removeFriend(friend);
        friend.removeFriend(user);
        return true;*/
        return true;
    }

    public boolean addFriend(long userId, long friendId) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/socialdb","root","admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO friend_list (list_owner_id,friend_id) VALUES (?,?)");
            preparedStatement.setLong(1,userId);
            preparedStatement.setLong(2,friendId);
            preparedStatement.executeUpdate();
            preparedStatement = conn.prepareStatement("INSERT INTO friend_list (list_owner_id,friend_id) VALUES (?,?)");
            preparedStatement.setLong(1,friendId);
            preparedStatement.setLong(2,userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*if(userId==friendId){
            return false;
        }
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        User user = entityManager.find(User.class, userId);
        User friend = entityManager.find(User.class, friendId);
        user.addFriend(friend);
        friend.addFriend(user);*/
        return true;
    }

    private boolean UpdateFriedList(User user, User friend){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(friend);
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (PersistenceException e){
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }finally {
            entityManager.close();
        }
        return true;
    }

    public User getUserById(long id){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        User user = (User) entityManager.createNamedQuery("User.FindUserById").setParameter("id",id).getSingleResult();
        entityManager.close();
        return user;
    }

    /*public List<User> getUsersByName(String name){
        List<User> users = null;
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
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
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
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
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
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
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        long returnVal = (long) entityManager.createNamedQuery("User.FindUserIdByEmail").setParameter("email",email).getSingleResult();
        entityManager.close();
        return returnVal;
    }

}

