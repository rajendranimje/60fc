package in.gov.cgg.alumni.trial.fcmnew;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import androidx.core.app.NotificationCompat;
import in.gov.cgg.alumni.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationsUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//        }
//
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//        //sendNotification(remoteMessage.getNotification().getBody());
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            //handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                //JSONObject json = new JSONObject(String.valueOf(remoteMessage.getData()));
                handleDataMessage(remoteMessage);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(RemoteMessage json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
           // JSONObject data = json.getJSONObject("data");

            Map<String, String> wholedata = json.getData();

            String title = wholedata.get("title");
            String message = wholedata.get("message");
            //boolean isBackground = wholedata.get("is_background");
           // String imageUrl = wholedata.get("image");
            String from = wholedata.get("from");
            //JSONObject payload = wholedata.get("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
           // Log.e(TAG, "isBackground: " + isBackground);
           // Log.e(TAG, "payload: " + payload.toString());
           // Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + from);

            sendNotification(title,message,from);

//
//            if (!NotificationsUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationsUtils notificationUtils = new NotificationsUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            } else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), NewFCMMessageActivity.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
          //  }
        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }
        }
/*
    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationsUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationsUtils notificationUtils = new NotificationsUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), NewFCMMessageActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
*/

//    private void showNotificationMessageWithBigImage(Context applicationContext, String title, String message,
//                                                     String timestamp, Intent resultIntent, String imageUrl) {
//
//        notificationUtils = new NotificationsUtils(applicationContext);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timestamp, resultIntent, imageUrl);
//    }
//
//    private void showNotificationMessage(Context applicationContext, String title, String message, String timestamp,
//                                         Intent resultIntent) {
//
//        notificationUtils = new NotificationsUtils(applicationContext);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timestamp, resultIntent);
//    }


//    private void handleNotification(String message) {
//        if (!NotificationsUtils.isAppIsInBackground(getApplicationContext())) {
//            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            // play notification sound
//            NotificationsUtils notificationUtils = new NotificationsUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
//        } else {
//            // If the app is in background, firebase itself handles the notification
//        }
//    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // Saving reg id to shared preferences
       // storeRegIdInPref(token);

        // sending reg id to your server
        sendRegistrationToServer(token);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//        registrationComplete.putExtra("token", token);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

//    private void storeRegIdInPref(String token) {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("regId", token);
//        editor.apply();
//    }

    private void sendRegistrationToServer(String token) {
// sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void sendNotification(String messageBody, String msg, String from) {
        Intent intent = new Intent(this, NewFCMMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.baseline_message_black_18)
                        .setContentTitle(messageBody+"-"+from)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

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


}