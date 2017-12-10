package BO;

import DB.UserDb;
import UI.UserViewModel;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by douglas on 11/4/17.
 */
@Path("resource")
public class Facade {
    private static UserDb userDb =new UserDb();
    
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
    public static String  createUser(@FormParam("name") String name, @FormParam("email") String email, @FormParam("password") String password, @FormParam("date")String date){
        return String.valueOf( userDb.createUser(name,email,password,new Date()));
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
     * @return a list of all userviewmodel with name and id
     */
    @GET
    @Path("getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public static String getUsers(){
        System.out.println("får ngt haheheh");
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
    public static String getUserById(@PathParam("id") String id){
        Gson gson =new Gson();
        User user = userDb.getUserById(Long.parseLong(id));

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
    public static String getFriendList(@PathParam("id") String listOwnerId){
        Gson gson =new Gson();

        LinkedList<UserViewModel> friends = new LinkedList<>();
        try{
            for(User p : userDb.getFriendList(Long.parseLong(listOwnerId))){
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
    public static String addFriend(@FormParam("ownerId") String listOwnerId , @FormParam("friendId") String friendId){
        return  String.valueOf(userDb.addFriend(Long.parseLong(listOwnerId),Long.parseLong(friendId)));
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

    public static String removeFriend(@FormParam("ownerId") String listOwnerId , @FormParam("friendId") String friendId){
        return  String.valueOf(userDb.removeFriend(Long.parseLong(listOwnerId),Long.parseLong(friendId)));
    }

    /**
     * gets a BO.UserViewModel object created where needed
     * @param id target user id
     * @return a userViewModel object
     */
    private static UserViewModel getUserObjectById(long id){
        User user = userDb.getUserById(id);
        return new UserViewModel(user.getName(),user.getBirthday().toString(),user.getId());
    }
    /**
     * gets a BO.UserViewModel object created where needed
     * @param listOwnerId target user id
     * @return list of post objects
     */
    private static LinkedList<UserViewModel> getFriendListObject(long listOwnerId){
        LinkedList<UserViewModel> friends = new LinkedList<>();
        try{
            for(User p : userDb.getFriendList(listOwnerId)){
                friends.add(new UserViewModel(p.getName(),p.getEmail(),p.getId()));
            }
        }catch(NullPointerException e){
            System.out.println("NULLPOINTER EXCEPTION");
        }
        return friends;
    }
}
