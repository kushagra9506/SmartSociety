package app.smartsociety.Model;

public class Complaint {
    String complaint,date,uid,image;
    Boolean resolved;

    public Complaint() {
    }

    public Complaint(String complaint, String date, String uid, String image, Boolean resolved) {
        this.complaint = complaint;
        this.date = date;
        this.uid = uid;
        this.image = image;
        this.resolved = resolved;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }
}
