package in.gov.cgg.alumni.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.gov.cgg.alumni.R;

/**
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    public boolean isConnected = true;
    String status;
    Context Cnt;
    Activity activity;
    Activity parent;
    AlertDialog alert;


    public NetworkChangeReceiver(Activity a) {
        // TODO Auto-generated constructor stub
        parent = a;

    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        activity = (Activity) context;
        status = NetworkUtil.getConnectivityStatusString(context);
        if (status.equals("Not connected to Internet")) {
            //Toast.makeText(context, "Internet connection required", Toast.LENGTH_LONG).showPdf();
        }
        returnStatus(status, context);
    }

    public void returnStatus(String s, final Context cnt) {
        if (s.equals("Mobile data enabled")) {
            isConnected = true;

        } else if (s.equals("Wifi enabled")) {
            isConnected = true;

        } else {
            isConnected = false;

//            final Dialog dialog = new Dialog(cnt, R.style.AppTheme);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.nointernet);
//            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
//            TextView tv_subtitle = (TextView) dialog.findViewById(R.id.tv_subtitle);
//            Button btn_tryagain = (Button) dialog.findViewById(R.id.btn_tryagain);
//            btn_tryagain.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                   /* activity.finish();
//                    Intent intent = new Intent(activity, activity.getClass());
//                    activity.startActivity(intent);*/
//                    if(isConnectedToInternet(cnt)){
//                        dialog.dismiss();
//                    }
//                   else {
//                        Toast.makeText(cnt, "Please check your internet connection", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
//            });
//
//            dialog.show();
//

        }

    }

    public boolean is_connected() {
        return isConnected;
    }
    public boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}
