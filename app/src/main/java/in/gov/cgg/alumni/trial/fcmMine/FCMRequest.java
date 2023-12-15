package in.gov.cgg.alumni.trial.fcmMine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FCMRequest {
    @SerializedName("notification")
    @Expose
    private NotificationRequest notification;
    @SerializedName("to")
    @Expose
    private String to;

    public NotificationRequest getNotification() {
        return notification;
    }

    public void setNotification(NotificationRequest notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
