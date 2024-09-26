package in.gov.cgg.alumni.trial.fcmMine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationRequest {

    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("title")
    @Expose
    private String title;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
