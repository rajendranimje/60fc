package in.gov.cgg.alumni.fcmfinal;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.activities.DashboardActivity;
import in.gov.cgg.alumni.activities.NotifyListActivity;
import in.gov.cgg.alumni.activities.NotifyModel;
import in.gov.cgg.alumni.trial.fcmMine.ApiClient;
import in.gov.cgg.alumni.trial.fcmMine.ApiInterface;
import in.gov.cgg.alumni.trial.fcmMine.FCMResponse;
import in.gov.cgg.alumni.utils.CheckInternet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalTextActivity extends Activity {
    private static final String TAG = "FCMCggMainActivity";
    String impRegId;

    EditText et_to, et_subject, et_type, et_from;
    TextView tv_send,tv_char;
    Button home;
    String time1;
    ProgressDialog pd;
    private DatabaseReference databaseMembers;
    private TextWatcher mTextEditorWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_file);

        pd = new ProgressDialog(FinalTextActivity.this);
        pd.setCancelable(false);
        // et_to = findViewById(R.id.et_to);
        //et_from = findViewById(R.id.et_from);
        et_subject = findViewById(R.id.et_subject);
        et_type = findViewById(R.id.et_msg);
        tv_send = findViewById(R.id.tv_send);
        tv_char = findViewById(R.id.tv_char);
        home = findViewById(R.id.btn_home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinalTextActivity.this, DashboardActivity.class));
            }
        });


        mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
               //
                int newcount=350-(s.length());
                tv_char.setText(String.valueOf(newcount));

            }

            public void afterTextChanged(Editable s) {
            }
        };


        et_type.addTextChangedListener(mTextEditorWatcher);


        if (GlobalDeclaration.profileDetails != null)
//        et_from.setText(GlobalDeclaration.profileDetails.getName());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create channel to show notifications.
                String channelId = getString(R.string.default_notification_channel_id);
                String channelName = getString(R.string.default_notification_channel_name);
                NotificationManager notificationManager =
                        getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                        channelName, NotificationManager.IMPORTANCE_LOW));
            }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    String token = task.getResult();
                    // Log and toast
                    String msg = getString(R.string.msg_token_fmt, token);
                    Log.d(TAG, msg);
                    impRegId = token;
                    //Toast.makeText(FinalTextActivity.this, msg, Toast.LENGTH_SHORT).show();


                    // Do whatever you want with your token now
                    // i.e. store it on SharedPreferences or DB
                    // or directly send it to server
                });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isOnline(FinalTextActivity.this)) {
                    // FirebaseMessaging.getInstance().unsubscribeFromTopic("weather");
                    sendMessageRequest();
                    //Toast.makeText(FinalTextActivity.this, "unsubscribed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FinalTextActivity.this, "Please Check Internet", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void sendMessageRequest() {

        //String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        Date date = new Date();

        String date1 = java.text.DateFormat.getDateInstance().format(date);


        String time1 = new SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            time1 = DateTimeFormatter.ofPattern("hh:mm").format(LocalTime.now());
//        }


        // String time1 = DateFormat.getTimeFormat(FinalTextActivity.this).format(date);


        Log.d("format", date1 + "\n" + time1);

        FinalData notificationRequest = new FinalData();
        notificationRequest.setTitle(et_subject.getText().toString());
        notificationRequest.setMessage(et_type.getText().toString());
        notificationRequest.setFromname(GlobalDeclaration.profileDetails.getName());
        notificationRequest.setUserId(GlobalDeclaration.profileDetails.getName());
        notificationRequest.setImg(GlobalDeclaration.profileDetails.getProfilePicUrl());
        notificationRequest.setDatestamp(date1);
        notificationRequest.setTimestamp(time1);
        FinalFCMReq fcmRequest = new FinalFCMReq();
        fcmRequest.setTo(impRegId);
        fcmRequest.setTo("/topics/weather");
        fcmRequest.setData(notificationRequest);

        callsendMessage(fcmRequest);
//
// NotificationRequest notificationRequest = new NotificationRequest();
//        notificationRequest.setTitle(getResources().getString(R.string.app_name));
//        notificationRequest.setBody(et_subject.getText().toString());
//        FCMRequest fcmRequest = new FCMRequest();
//        fcmRequest.setTo(impRegId);
//        fcmRequest.setTo("/topics/weather");
//        fcmRequest.setNotification(notificationRequest);
//        callsendMessage(fcmRequest);


    }

    private void callsendMessage(final FinalFCMReq fcmRequest) {

        pd.show();
        ApiInterface apiServiceSession = ApiClient.getClient().create(ApiInterface.class);


        Call<FCMResponse> call = apiServiceSession.sendMessage(fcmRequest);
        Log.e("apiServiceSession_url: ", "" + call.request().url());

        call.enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.body() != null) {
                    pd.dismiss();
//                    Toast.makeText(FCMMineMessageActivity.this,
//                            response.body()., Toast.LENGTH_SHORT).show();
                    // Toast.makeText(FinalTextActivity.this, "success"+response.body().getMessage_id(), Toast.LENGTH_SHORT).show();
                    storeNotification(fcmRequest);
                    et_type.setText("");
                    Toast.makeText(FinalTextActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FinalTextActivity.this, DashboardActivity.class));

                } else {
                    //Toast.makeText(FinalTextActivity.this, "failed", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                pd.dismiss();
                //Toast.makeText(FinalTextActivity.this, "on FAILUREE", Toast.LENGTH_SHORT).show();
                Log.e("Exp", t.toString());


            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FinalTextActivity.this, DashboardActivity.class));
    }

    private void storeNotification(FinalFCMReq remoteMessage) {

        databaseMembers = FirebaseDatabase.getInstance().getReference("notify_list");
        String id = databaseMembers.push().getKey();

        NotifyModel notifyModel = new NotifyModel();

        notifyModel.setFromname(remoteMessage.getData().getFromname());
        notifyModel.setSubject(remoteMessage.getData().getTitle());
        notifyModel.setMessage(remoteMessage.getData().getMessage());
        notifyModel.setIma(remoteMessage.getData().getImg());
        notifyModel.setDatestamp(remoteMessage.getData().getDatestamp());
        notifyModel.setTimestamp(remoteMessage.getData().getTimestamp());
        notifyModel.setNid(id);

        if (id != null) {
            databaseMembers.child(id).setValue(notifyModel);
        }
        Log.e("Storinggg", "Stored details: " + notifyModel.toString());
    }

}
