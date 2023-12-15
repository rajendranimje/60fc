package in.gov.cgg.alumni.trial.fcmMine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FCMResponse {

    @SerializedName("multicast_id")
    @Expose
    private double multicastId;

    public double getMessage_id() {
        return message_id;
    }

    public void setMessage_id(double message_id) {
        this.message_id = message_id;
    }

    @SerializedName("message_id")
    @Expose
    private double message_id;


    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("failure")
    @Expose
    private Integer failure;
    @SerializedName("canonical_ids")
    @Expose
    private Integer canonicalIds;
    @SerializedName("results")
    @Expose
    private List<MsgResult> results = null;

    public double getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(double multicastId) {
        this.multicastId = multicastId;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public Integer getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(Integer canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public List<MsgResult> getResults() {
        return results;
    }

    public void setResults(List<MsgResult> results) {
        this.results = results;
    }


}
