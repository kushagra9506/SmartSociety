package app.smartsociety.Model;

public class Maintainance {

    String key,roomno,transactionid,paidby,paymentdate,paymentid,date,title,payerid;
    Boolean paid;
    long amount;

    public Maintainance() {
    }

    public Maintainance(String key, String roomno, String transactionid, String paidby, String paymentdate, String paymentid, String date, String title, String payerid, Boolean paid, long amount) {
        this.key = key;
        this.roomno = roomno;
        this.transactionid = transactionid;
        this.paidby = paidby;
        this.paymentdate = paymentdate;
        this.paymentid = paymentid;
        this.date = date;
        this.title = title;
        this.payerid = payerid;
        this.paid = paid;
        this.amount = amount;
    }

    public String getPayerid() {
        return payerid;
    }

    public void setPayerid(String payerid) {
        this.payerid = payerid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getPaidby() {
        return paidby;
    }

    public void setPaidby(String paidby) {
        this.paidby = paidby;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
}
