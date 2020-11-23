package sample;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class SerialUser implements Serializable {
    public String startTime1;
    public String startTime2;
    public String endTime1;
    public String endTime2;
    public String mPath;
    public String bPath;
    public String name;
    public String cond;
    public String id;
    public SerialUser(String s1,String s2,String e1,String e2,String mp,String bp,String name,String cond,String id) {
        this.startTime1 = s1 ; this.startTime2 = s2;
        this.endTime1 = e1; this.endTime2 = e2;
        this.mPath = mp; this.bPath = bp;
        this.name = name; this.cond = cond; this.id = id;
    }
    public SerialUser() {}
    public void transUser(User user) {
        this.startTime1 = user.getStartTime1() ; this.startTime2 = user.getStartTime2();
        this.endTime1 = user.getEndTime1(); this.endTime2 = user.getEndTime2();
        this.mPath = user.getmPath(); this.bPath = user.getbPath();
        this.name = user.getName(); this.cond = user.getCond(); this.id = user.getId();
    }
    public User toUser() {
        return new User(startTime1,startTime2,endTime1,endTime2,mPath,bPath,name,cond,id);
    }
}
