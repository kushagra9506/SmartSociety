package app.smartsociety.Model;

public class Register {

    String name,roomno,email,password,image;
    Boolean head,admin;

    public Register() {
    }


    public Register(String name, String roomno, String email, String password, String image, Boolean head, Boolean admin) {
        this.name = name;
        this.roomno = roomno;
        this.email = email;
        this.password = password;
        this.image = image;
        this.head = head;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
