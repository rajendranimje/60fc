package in.gov.cgg.alumni.fcmfinal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinalData {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("datestamp")
    @Expose
    private String datestamp;


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    @SerializedName("message")
    @Expose

    private String message;

    @SerializedName("fromname")
    @Expose
    private String fromname;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("img")
    @Expose
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
