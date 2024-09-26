package in.gov.cgg.alumni.trial.fcmMine;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FCMMineMessageActivity extends Activity {
    private static final String TAG = "FCMCggMainActivity";
    String impRegId;
    String username = GlobalDeclaration.MYKEY;
    String ct = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmnew);

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
        Button subscribeButton = findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Subscribing to weather topic");
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("weather")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = getString(R.string.msg_subscribed);
                                if (!task.isSuccessful()) {
                                    msg = getString(R.string.msg_subscribe_failed);
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(FCMMineMessageActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                // [START retrieve_current_token]
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
                            Toast.makeText(FCMMineMessageActivity.this, msg, Toast.LENGTH_SHORT).show();
                            // Do whatever you want with your token now
                            // i.e. store it on SharedPreferences or DB
                            // or directly send it to server
                        });
                // [END retrieve_current_token]
            }
        });

        Button send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageRequest();
            }
        });

    }

    private void sendMessageRequest() {

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle("Title Text");
        notificationRequest.setBody("This is a topic  message");
        FCMRequest fcmRequest = new FCMRequest();
        fcmRequest.setTo(impRegId);
        fcmRequest.setTo("/topics/weather");
        fcmRequest.setNotification(notificationRequest);
        callsendMessage(fcmRequest);


    }

    private void callsendMessage(FCMRequest fcmRequest) {

        ApiInterface apiServiceSession = ApiClient.getClient().create(ApiInterface.class);


        Call<FCMResponse> call = apiServiceSession.sendMessage1(fcmRequest);
        Log.e("apiServiceSession_url: ", "" + call.request().url());

        call.enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if(response.body()!=null)
                {
//                    Toast.makeText(FCMMineMessageActivity.this,
//                            response.body()., Toast.LENGTH_SHORT).show();
                    Toast.makeText(FCMMineMessageActivity.this, "success", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(FCMMineMessageActivity.this, "failed", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                Toast.makeText(FCMMineMessageActivity.this, "on FAILUREE", Toast.LENGTH_SHORT).show();
                Log.e("Exp", t.toString());


            }
        });


    }


}
