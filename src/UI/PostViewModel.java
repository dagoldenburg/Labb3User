package UI;

import java.util.Date;

public class PostViewModel implements Comparable<PostViewModel>{
    private long id;
    private String title;
    private String text;
    private Date date;
    private UserViewModel owner;

    public PostViewModel(long id,String title,String text,Date date,UserViewModel owner){
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.owner = owner;
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

    public UserViewModel getOwner() {
        return owner;
    }

    public void setOwner(UserViewModel owner) {
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Override
    public int compareTo(PostViewModel o) {
        if(this.id<o.getId()){
            return 1;
        }else if(this.id>o.getId()){
            return -1;
        }else return 0;
    }
}
