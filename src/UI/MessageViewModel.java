package UI;

import java.util.Date;

public class MessageViewModel implements Comparable<MessageViewModel> {

    private String content;
    private Date date;
    private long id;
    private UserViewModel owner;

    public MessageViewModel(String cont,Date dat,long id,UserViewModel send){
        this.content = cont;
        date = dat;
        this.id = id;
        this.owner = send;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(MessageViewModel o) {
        if(o.getId()>this.id){
            return 1;
        }else if(o.getId()<this.id){
            return -1;
        }else return 0;
    }

    public UserViewModel getOwner() {
        return owner;
    }

    public void setOwner(UserViewModel owner) {
        this.owner = owner;
    }
}
