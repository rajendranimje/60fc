package in.gov.cgg.alumni.fcmfinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.activities.DashboardActivity;
import in.gov.cgg.alumni.activities.NotifyListActivity;
import in.gov.cgg.alumni.activities.NotifyModel;
import in.gov.cgg.alumni.activities.SplashActivity;
import in.gov.cgg.alumni.trial.fcmnew.Config;
import in.gov.cgg.alumni.trial.fcmnew.NewFCMMessageActivity;
import in.gov.cgg.alumni.trial.fcmnew.NotificationsUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Bitmap bitmap;
    private DatabaseReference databaseMembers;
    private StorageReference mstorageReference;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                handleDataMessage(remoteMessage);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

//    private void storeNotification(RemoteMessage remoteMessage) {
//
//        databaseMembers = FirebaseDatabase.getInstance().getReference("notify_list");
//        String id = databaseMembers.push().getKey();
//
//
//        NotifyModel notifyModel = new NotifyModel();
//
//        notifyModel.setFromname(remoteMessage.getData().get("fromname"));
//        notifyModel.setSubject(remoteMessage.getData().get("title"));
//        notifyModel.setMessage(remoteMessage.getData().get("message"));
//        notifyModel.setIma(remoteMessage.getData().get("img"));
//        notifyModel.setTimestamp(remoteMessage.getData().get("timestamp"));
//        notifyModel.setNid(id);
//
//        if (id != null) {
//            databaseMembers.child(id).setValue(notifyModel);
//        }
//
//        Log.e("Storinggg", "Stored details: " + notifyModel.toString());
//
//    }

    private void handleDataMessage(RemoteMessage json) {
        Log.e(TAG, "push json: " + json.toString());

        try {

            Map<String, String> wholedata = json.getData();

            String title = wholedata.get("title");
            String message = wholedata.get("message");
            String from = wholedata.get("fromname");
            String uid = wholedata.get("userId");
            String img = wholedata.get("img");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "timestamp: " + from);
            Log.e(TAG, "userId: " + uid);
            Log.e(TAG, "img: " + img);

            if (img != null) {
                bitmap = getBitmapFromURL(img);
            }

            sendNotification(title, message, from, bitmap);
            //storeNotification(json);


//            if (uid.equalsIgnoreCase(from)) {
//                return;
//            } else {
//                sendNotification(title, message, from);
//            }
        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);

    }

    private void sendRegistrationToServer(String token) {
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void sendNotification(String messageBody, String msg, String from, Bitmap largeIcon) {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        notificationBuilder.setSmallIcon(R.drawable.baseline_message_black_18)
                .setContentTitle(from)
                .setContentText(messageBody + ": " + msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        if (largeIcon != null) {
            notificationBuilder.setLargeIcon(largeIcon);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}