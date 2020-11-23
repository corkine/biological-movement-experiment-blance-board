package sample;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class User{
    SimpleStringProperty startTime1;
    SimpleStringProperty startTime2;
    SimpleStringProperty endTime1;
    SimpleStringProperty endTime2;
    SimpleStringProperty mPath;
    SimpleStringProperty bPath;
    SimpleStringProperty name;
    SimpleStringProperty cond;
    SimpleStringProperty id;
    User(String s1,String s2,String e1,String e2,String mp,String bp,String name,String cond,String id) {
        this.startTime1 = new SimpleStringProperty(s1) ; this.startTime2 = new SimpleStringProperty(s2);
        this.endTime1 = new SimpleStringProperty(e1); this.endTime2 = new SimpleStringProperty(e2);
        this.mPath = new SimpleStringProperty(mp); this.bPath = new SimpleStringProperty(bp);
        this.name = new SimpleStringProperty(name); this.cond = new SimpleStringProperty(cond); this.id = new SimpleStringProperty(id);
    }

    public String getStartTime1() {
        return startTime1.get();
    }

    public SimpleStringProperty startTime1Property() {
        return startTime1;
    }

    public void setStartTime1(String startTime1) {
        this.startTime1.set(startTime1);
    }

    public String getStartTime2() {
        return startTime2.get();
    }

    public SimpleStringProperty startTime2Property() {
        return startTime2;
    }

    public void setStartTime2(String startTime2) {
        this.startTime2.set(startTime2);
    }

    public String getEndTime1() {
        return endTime1.get();
    }

    public SimpleStringProperty endTime1Property() {
        return endTime1;
    }

    public void setEndTime1(String endTime1) {
        this.endTime1.set(endTime1);
    }

    public String getEndTime2() {
        return endTime2.get();
    }

    public SimpleStringProperty endTime2Property() {
        return endTime2;
    }

    public void setEndTime2(String endTime2) {
        this.endTime2.set(endTime2);
    }

    public String getmPath() {
        return mPath.get();
    }

    public SimpleStringProperty mPathProperty() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath.set(mPath);
    }

    public String getbPath() {
        return bPath.get();
    }

    public SimpleStringProperty bPathProperty() {
        return bPath;
    }

    public void setbPath(String bPath) {
        this.bPath.set(bPath);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCond() {
        return cond.get();
    }

    public SimpleStringProperty condProperty() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond.set(cond);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

}
