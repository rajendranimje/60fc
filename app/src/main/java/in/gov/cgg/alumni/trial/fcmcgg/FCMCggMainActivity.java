package in.gov.cgg.alumni.trial.fcmcgg;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import in.gov.cgg.alumni.R;

public class FCMCggMainActivity extends AppCompatActivity {

    EditText title, body, topic, sound, api_key, click_action;
    Button push;
    public static final String API_KEY = "AAAAoGRZoPo:APA91bG81jex4eiiZ7Cihpz-68ei7eFfxuM7y7EVwsXgj2wU-3HP82YqN8M6JST17exQjOGGvx9igFjGP7s_txEQUAkLVLCh0nWCrYrjVdVnrtQo4Z9Qk34U7i6LcgGfMFQFjSvehX2r";
    private NotificationChannel mChannel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmcgg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.ntitle);
        body = findViewById(R.id.nbody);
        topic = findViewById(R.id.ntopic);
        sound = findViewById(R.id.nsound);
        api_key = findViewById(R.id.api_key);
        click_action = findViewById(R.id.nclick_action);

        push = findViewById(R.id.push);

        api_key.setText(API_KEY);
        topic.setText("weather");

       // showNotification(FCMCggMainActivity.this, "hI", "hello", new Intent(FCMCggMainActivity.this, FCMCggMainActivity.class));


        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.getText().length() == 0 || body.getText().length() == 0 || topic.getText().length() == 0 ||
                        sound.getText().length() == 0 || api_key.getText().length() == 0 || click_action.getText().length() == 0) {
                    Snackbar.make(v, "Please fill all the textBoxes", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    callEasyNotify();
                }

            }
        });
    }

    private void callEasyNotify() {
        CggText cggText = new CggText(api_key.getText().toString());
        cggText.setSendBy(CggText.TOPIC);
        cggText.setTopic(topic.getText().toString());
        cggText.setTitle(title.getText().toString());
        cggText.setBody(body.getText().toString());
        cggText.setClickAction("DashboardActivity");
        cggText.setSound("default");
        cggText.nPush();
        cggText.setCggTextListener(new CggText.CggTextListener() {
            @Override
            public void onNotifySuccess(String s) {

                Toast.makeText(FCMCggMainActivity.this, s, Toast.LENGTH_SHORT).show();
//                showNotification(FCMCggMainActivity.this, "hI", "hello",
//                        new Intent(FCMCggMainActivity.this, FCMCggMainActivity.class));

            }

            @Override
            public void onNotifyError(String s) {
                Toast.makeText(FCMCggMainActivity.this, s, Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(context);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addNextIntent(intent);
        }
        PendingIntent resultPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
