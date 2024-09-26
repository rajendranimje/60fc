package in.gov.cgg.alumni.trial.fcmMine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MsgResult {
    @SerializedName("message_id")
    @Expose
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
