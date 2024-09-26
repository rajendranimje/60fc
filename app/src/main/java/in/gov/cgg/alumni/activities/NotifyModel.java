package in.gov.cgg.alumni.activities;

public class NotifyModel {

    String fromname;
    String subject;
    String message;
    String ima;
    String nid;
    String timestamp;
    String datestamp;


    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIma() {
        return ima;
    }

    public void setIma(String ima) {
        this.ima = ima;
    }


    @Override
    public String toString() {
        return "NotifyModel{" +
                "fromname='" + fromname + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", ima='" + ima + '\'' +
                ", nid='" + nid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
