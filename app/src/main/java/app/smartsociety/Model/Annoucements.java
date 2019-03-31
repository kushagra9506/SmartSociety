package app.smartsociety.Model;

public class Annoucements {

    private String Title,Message,Date;

    public Annoucements() {
    }

    public Annoucements(String title, String message, String date) {
        Title = title;
        Message = message;
        Date = date;


    }



    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }



    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
