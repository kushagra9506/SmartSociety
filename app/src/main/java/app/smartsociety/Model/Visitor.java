package app.smartsociety.Model;

import java.util.Date;

public class Visitor {

    String name,image,contact,roomno;
    Date intime,outtime;

    public Visitor() {
    }

    public Visitor(String name, String image, String contact, String roomno, Date intime, Date outtime) {
        this.name = name;
        this.image = image;
        this.contact = contact;
        this.roomno = roomno;
        this.intime = intime;
        this.outtime = outtime;
    }

    public Date getOutime() {
        return outtime;
    }

    public void setOutime(Date outtime) {
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

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}
