package BO;

import UI.UserViewModel;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by douglas on 11/22/17.
 */
@Path("hello")
public class Test {
    private String data;
    @GET
    @Path("2")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHello(){
        Gson gson = new Gson();
        long id = (long) 19;
        UserViewModel userViewModel =new UserViewModel("Douglas","1993-10-14",10);
        return gson.toJson( userViewModel);
    }
    @GET
    @Path("1")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHello2(){
        return "hello world1";
    }
}
