package app.smartsociety.Model;

public class Event {

    private String name,date,description,place,updatedby,time,contact,roomno;

    public Event(String name, String date, String description, String place, String updatedby, String time, String contact, String roomno) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.place = place;
        this.updatedby = updatedby;
        this.time = time;
        this.contact = contact;
        this.roomno = roomno;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public Event() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
