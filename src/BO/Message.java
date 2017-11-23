package BO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by douglas on 11/4/17.
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Message.FindMessgesByRecipient",query = "Select m From Message m Where m.recipient.id= :id"),
        @NamedQuery(name="Message.FindMessgesByRecipientAndSender",query = "Select m From Message m Where m.recipient.id= :id and m.sender.id = :id2")
})
@Table(name = "message", schema = "SocialDb")

public class Message {

    private long id ;
    private String Content;
    private Date date;
    private String type;

    private User sender;
    private User recipient;



    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Basic
    @Column(name = "content")
    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "sender_id")
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "recipient_id")
    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
