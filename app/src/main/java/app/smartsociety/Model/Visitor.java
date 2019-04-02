package app.smartsociety.Model;

import java.util.Date;

public class Visitor {

    String name,image,contact,roomno;
     String intime,outtime;

    public Visitor() {
    }

    public Visitor(String name, String image, String contact, String roomno, String intime, String outtime) {
        this.name = name;
        this.image = image;
        this.contact = contact;
        this.roomno = roomno;
        this.intime = intime;
        this.outtime = outtime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getIntime() {
        return intime;
    }

}
