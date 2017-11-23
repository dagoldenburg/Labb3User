import BO.Facade;
import BO.Message;
import DB.MessageDb;
import DB.UserDb;
import UI.UserViewModel;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import org.glassfish.jersey.client.ClientConfig;


/**
 * Created by douglas on 11/22/17.
 */
public class main {
    private static String Url ="http://localhost:8080/resource";

    public static void main(String[] args) {
        MessageDb messageDb = new MessageDb();
       List<Message> messages = messageDb.getMessagesByUserId(20);
        for (Message m: messages) {
            System.out.println(m.getDate());

        }
    }
}
