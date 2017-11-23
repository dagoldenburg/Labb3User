package UI;

import java.io.Serializable;

public class UserViewModel implements Serializable {

    private String name;
    private String birthday;
    private long id;
    public UserViewModel(){

    }
    public UserViewModel(String name, String s, long id) {
        this.name=name;
        this.id=id;
        this.birthday = s;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
