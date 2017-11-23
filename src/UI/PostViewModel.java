package UI;

import java.util.Date;

public class PostViewModel {

    private String title;
    private String text;
    private Date date;

    public PostViewModel(String title,String text,Date date){
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public PostViewModel(){
        title=null;
        text=null;
        date=null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
