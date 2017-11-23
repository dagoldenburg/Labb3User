package UI;


/**
 * Created by douglas on 11/23/17.
 */
public class MessageViewModel {
    private long id ;
    private String Content;
    private String date;

    public MessageViewModel(long id, String content, String date) {
        this.id = id;
        Content = content;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
