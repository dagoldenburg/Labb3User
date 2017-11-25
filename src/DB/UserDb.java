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
        Session sess = JPAUtil.getSession();
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setBirthday(date);
            
            sess.getTransaction().begin();
            sess.persist(user);

            sess.getTransaction().commit();
            return true;
        }catch (PersistenceException e){
            System.out.println("Transaction aborted");

            sess.getTransaction().rollback();
            return false;
        }finally {
            sess.close();
            return true;
        }
    }

    public User checkLogin(String email ,String password) {
        Session sess = JPAUtil.getSession();
        User currentUser=null;
        try {
            currentUser = (User) sess.createNamedQuery("User.CheckLogin")
                    .setParameter("email", email)
                    .setParameter("password", password).getSingleResult();
        }catch (NoResultException e0){
            return null;
        } catch (PersistenceException e1) {
            System.out.println("Transaction aborted");

            return null;

        } finally {
            sess.close();
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
        Session sess = JPAUtil.getSession();
        User user=  sess.find(User.class,userId);
        User friend = sess.find(User.class,friendId);
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
        Session sess = JPAUtil.getSession();
        User user = sess.find(User.class, userId);
        User friend = sess.find(User.class, friendId);
        user.addFriend(friend);
        friend.addFriend(user);*/
        return true;
    }

    private boolean UpdateFriedList(User user, User friend){
        Session sess = JPAUtil.getSession();
        try{
            sess.getTransaction().begin();
            sess.merge(friend);
            sess.merge(user);
            sess.getTransaction().commit();
        } catch (PersistenceException e){
            sess.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }finally {
            sess.close();
        }
        return true;
    }

    public User getUserById(long id){
        Session sess = JPAUtil.getSession();
        User user = (User) sess.createNamedQuery("User.FindUserById").setParameter("id",id).getSingleResult();
        sess.close();
        return user;
    }

    /*public List<User> getUsersByName(String name){
        List<User> users = null;
        Session sess = JPAUtil.getSession();
        try {
            sess.getTransaction().begin();
            users=(List<User>) sess.createNamedQuery("User.FindUsersByName").setParameter("name",name).getResultList();
            sess.getTransaction().commit();
        }catch (PersistenceException e){
            sess.getTransaction().rollback();
        }finally {
            sess.close();
        }
        return users;
    }*/

    public Map<String,Long> getUsers(){
        Session sess = JPAUtil.getSession();
        List<User> users = null;
        List<Object[]> objects;
        try {
            objects= sess.createNamedQuery("User.FindUsers").getResultList();
            Map<String,Long> resultList = new HashMap<>(objects.size());
            for (Object[] result:objects ) {
                resultList.put((String) result[0],(long)result[1]);
            }

            return resultList;
        }catch (PersistenceException e){
            sess.close();
        }
        return null;
    }



    public List<User> getFriendList(long id){
        Session sess = JPAUtil.getSession();
        List<User> friendList =null;
        try {
            friendList = sess.createNamedQuery("User.FindFriendsByUserId").setParameter("id",id).getResultList();
        }catch (Exception e){
            System.out.println("some error");
        }finally {
            sess.close();

        }
        return friendList;
    }

    public long getUserIdByEmail(String email){
        Session sess = JPAUtil.getSession();
        long returnVal = (long) sess.createNamedQuery("User.FindUserIdByEmail").setParameter("email",email).getSingleResult();
        sess.close();
        return returnVal;
    }

}

