package in.gov.cgg.alumni.fcmfinal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.gov.cgg.alumni.fcmfinal.FinalData;

public class FinalFCMReq {
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("data")
    @Expose
    private FinalData data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public FinalData getData() {
        return data;
    }

    public void setData(FinalData data) {
        this.data = data;
    }
}
