package BO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by douglas on 11/4/17.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "User.CheckLogin",query = "SELECT  u FROM User u where u.email=  :email And u.password= :password"),
        @NamedQuery(name="User.FindUserByEmail",query = "SELECT u From User u Where u.email= :email"),
        @NamedQuery(name = "User.FindUsers",query = "select u.name ,u.id from User u"),
        @NamedQuery(name="User.FindUserIdByEmail",query = "select u.id from User  u where u.email =:email"),
        @NamedQuery(name="User.FindUserById",query = "SELECT u From User u Where u.id= :id"),
        @NamedQuery(name="User.FindFriendsByUserId", query = "select b from User a join a.friendList b where a.id=:id"),

})
@Table(name = "user", schema = "SocialDb")
public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private Date birthday;

    private List<Post> posts;


    private List<Message> SenderMesg;
    private List<Message> recepientMesg;



    private List<User> friendList;
    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator")
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }


    @OneToMany(mappedBy = "recipient")
    public List<Message> getRecepientMesg() {
        return recepientMesg;
    }

    public void setRecepientMesg(List<Message> recepientMesg) {
        this.recepientMesg = recepientMesg;
    }

    @OneToMany(mappedBy = "sender")
    public List<Message> getSenderMesg() {
        return SenderMesg;
    }

    public void setSenderMesg(List<Message> senderMesg) {
        SenderMesg = senderMesg;
    }

   @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "friend_list",joinColumns=@JoinColumn(name ="list_owner_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="friend_id",referencedColumnName = "id"))
    public List<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }




    public boolean addFriend(User newFriend ){
        if(friendList.contains(newFriend)){
            return false;
        }else {
            friendList.add(newFriend);
            return true;
        }
    }

    public boolean removeFriend(User friend){
        if(friendList.contains(friend)){
            friendList.remove(friend);
            return true;
        }else {
            return  false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
