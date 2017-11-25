package DB;


import BO.Post;
import BO.User;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

/**
 * Created by douglas on 11/4/17.
 */
public class PostDb {

    public boolean createPost(String Content, String title, Date date, long userId){
        Post post=null;
        Session sess = JPAUtil.getSession();
        try {
            sess.getTransaction().begin();
            post =new Post();
            post.setContent(Content);
            post.setTitle(title);
            post.setPublishDate(date);
            User creator = (User) sess.find(User.class,userId);
            post.setCreator(creator);
            sess.persist(post);
            sess.getTransaction().commit();
        }catch (PersistenceException e){
            sess.getTransaction().rollback();
            e.printStackTrace();
            return  false;
        }finally {
            sess.close();
        }
        return true;
    }

    public List<Post> getPostsByOwnerId(long ownerid){
        Session sess = JPAUtil.getSession();
        List<Post> posts=null;
        try {
            posts = sess.createNamedQuery("Post.FindPostBycreatorId").setParameter("id",ownerid).getResultList();
        }catch (NoResultException e){
            System.out.println("No result");
        }finally {
            sess.close();
        }
        return posts;
    }
}
