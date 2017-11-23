package BO;

import DB.MessageDb;
import DB.PostDb;
import DB.UserDb;
import UI.MessageViewModel;
import UI.PostViewModel;
import UI.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by douglas on 11/4/17.
 */
@Path("resource")
public class Facade {
    private static UserDb userDb =new UserDb();
    private static MessageDb messageDb= new MessageDb();
    private static PostDb postDb = new PostDb();




    /**
     *this method creats a new user
     * @param name user's name
     * @param email user's email
     * @param password user's password
     * @return true if success
     */
    @POST
    @Path("createUser")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static String  createUser(@FormParam("name") String name, @FormParam("email") String email,@FormParam("password") String password){
        return String.valueOf( userDb.createUser(name,email,password));
    }

    /**
     * this method check if person tries to login has a account
     * @param email email from login form
     * @param password password from login from
     * @return user's object if that person has a account
     */
    @GET
    @Path("checkLogin/{email}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public static String checkLogin(@PathParam("email") String email, @PathParam("password") String password){
        Gson gson =new Gson();
        User user = userDb.checkLogin(email,password);
        if(user!=null) return gson.toJson(new UserViewModel(user.getName(),user.getBirthday().toString(),user.getId()));

        return null;
    }

    /**
     * This method is used to search all of other user
     * @return a list of alla userviewmodel with name and id
     */
    @GET
    @Path("getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public static String getUsers(){
        Gson gson =new Gson();
        LinkedList<UserViewModel> userViewModels= new LinkedList<>();
        Map<String,Long> users = userDb.getUsers();
        System.out.println(".........."+users.size());
        for (Map.Entry<String,Long> m:users.entrySet()) {
            UserViewModel userViewModel = new UserViewModel();
            userViewModel.setId(m.getValue());
            userViewModel.setName(m.getKey());
            userViewModels.add(userViewModel);
        }

        return gson.toJson(userViewModels);
    }

    /**
     * this method returns a user object by user's id
     * @param id user id
     * @return userview object
     */
    @GET
    @Path("user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static String getUserById(@PathParam("id") long id){
        Gson gson =new Gson();
        User user = userDb.getUserById(id);


        return gson.toJson(new UserViewModel(user.getName(),user.getBirthday().toString(),user.getId()));
    }

    /**
     * This method returns a list of all friend that current user has
     * @param listOwnerId is current user's id
     * @return list of all friend view model with only name
     */
    @GET
    @Path("friendList/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static String getFriendList(@PathParam("id") long listOwnerId){
        Gson gson =new Gson();

        LinkedList<UserViewModel> friends = new LinkedList<>();
        try{
            for(User p : userDb.getFriendList(listOwnerId)){
                friends.add(new UserViewModel(p.getName(),p.getEmail(),p.getId()));
            }
        }catch(NullPointerException e){
            System.out.println("NULLPOINTER EXCEPTION");
        }
        return gson.toJson(friends);
    }

    /**
     * This metod tries to add new friend to current user's friend list
     * @param listOwnerId current user's id
     * @param friendId new friend's id
     * @return true if it successed to add a new friend
     */
    @POST
    @Path("addFriend")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static String addFriend(@FormParam("ownerId") long listOwnerId ,@FormParam("friendId") long friendId){
        return  String.valueOf(userDb.addFriend(listOwnerId,friendId));
    }
    /**
     * This metod tries to remove a friend to current user's friend list
     * @param listOwnerId current user's id
     * @param friendId new friend's id
     * @return true if it successed to remove a new friend
     */
    @POST
    @Path("removeFriend")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    public static String removeFriend(@FormParam("ownerId") long listOwnerId ,@FormParam("friendId") long friendId){
        return  String.valueOf(userDb.removeFriend(listOwnerId,friendId));
    }

    /**
     * this method saves a new message to database
     * @param content
     * @param date
     * @param type
     * @param senderId
     * @param recipientId
     * @return
     */
    @POST
    @Path("createMessage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    public static String createMessage(@FormParam("content")String content, @FormParam("date")Date date, @FormParam("type")String type,@FormParam("sendId") long senderId,@FormParam("recipientId") long  recipientId){
        Gson gson = new Gson();
        return gson.toJson(messageDb.createMessage(content,date,type,senderId,recipientId));
    }

    /**
     * this method returns a list of messages for a recipient
     * @param id recipient's id
     * @return list of object messages
     */
    @GET
    @Path("messageByUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public  static String  getMessageByUserId(@PathParam("id")long id){
        Gson gson = new Gson();
        List<Message> messages =messageDb.getMessagesByUserId(id);
        LinkedList<MessageViewModel>  messageVMs=new LinkedList<>();
        for (Message m: messages) {
            messageVMs.add(new MessageViewModel(m.getId(),m.getContent(),m.getDate().toString()));
        }
        return gson.toJson(messageVMs);
    }
    @GET
    @Path("messageByEmail/{email}")
    @Produces(MediaType.TEXT_PLAIN)
    public static String getUserIdByEmail(@PathParam("email") String email){

        return new Gson().toJson(userDb.getUserIdByEmail(email));}

    /**
     * this method saves a new post to database
     * @param Content
     * @param title
     * @param date
     * @param creatorId
     * @return
     */
    @POST
    @Path("createPost")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    public static  String creatPost(@FormParam("content")String Content ,@FormParam("title")String title,@FormParam("date")Date date,@FormParam("creatorId")long creatorId){
        Gson gson =new Gson();
        return gson.toJson(postDb.createPost(Content,title,date,creatorId));
    }

    /**
     * this method return a list of all post by its creator's id
     * @param id creator id
     * @return list of post objects
     */
    @GET
    @Path("postByOwner/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public static  String getPostsByOwnerId(@PathParam("id") long id){
        Gson gson = new Gson();
        LinkedList<PostViewModel> posts = new LinkedList<>();
        //try {
            for (Post p : postDb.getPostsByOwnerId(id)) {
                posts.add(new PostViewModel(p.getTitle(), p.getContent(), p.getPublishDate()));
            }
        //}catch(NullPointerException e){
          //  System.out.println("NULLPOINTER EXCEPTION");
        //}
        return gson.toJson(posts);
    }
}
