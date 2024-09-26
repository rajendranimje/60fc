package in.gov.cgg.alumni.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;


public class Utils {
	/**
	 * Check whether the internet connection is present or not. <uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE" />
	 */
	// To check whether network connection is available on device or not
	public static boolean checkInternetConnection(Activity _activity) {
		ConnectivityManager conMgr = (ConnectivityManager) _activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected())
			return true;
		else
			return false;
	}
	
	public static String nullToTrim(String fieldValue){
		if(fieldValue == null  || fieldValue.equalsIgnoreCase("null") || fieldValue.equalsIgnoreCase("") ){
			return "--";
		}else{
			return fieldValue;
		}
	}

	/**
	 * Show an alert dialog and navigate back to previous screen if goBack is
	 * true
	 */
	public static void showAlert(final Activity _activity, String title,
                                 String alertMsg, final boolean goBack) {
		AlertDialog.Builder alert = new AlertDialog.Builder(_activity);
		alert.setTitle(title);
		alert.setCancelable(false);
		alert.setMessage(alertMsg);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (goBack)
					_activity.finish();
			}
		});
		alert.show();
	}

	
	
	/**
	 * Show an alert dialog and navigate back to previous screen if goBack is
	 * true
	 */
	public static void showAlertOkCancel(final Activity _activity,
                                         String title, String alertMsg, final boolean goBack) {
		AlertDialog.Builder alert = new AlertDialog.Builder(_activity);
		alert.setTitle(title);
		alert.setCancelable(false);
		alert.setMessage(alertMsg);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (goBack)
					_activity.finish();
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alert.show();
	}

	public static void showAlert(final Context myContext, String alertMsg, Exception e,
                                 final boolean goBack) {
		AlertDialog.Builder alert = new AlertDialog.Builder(myContext);
		alert.setTitle(alertMsg);
		alert.setCancelable(false);
		alert.setMessage(alertMsg);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (goBack)
					((Activity) myContext).finish();
			}
		});
		alert.show();
	}


	/*public static void openBrowser(final Context myContext, String alertMsg, Exception e) {
		AlertDialog.Builder alert = new AlertDialog.Builder(myContext);
		//alert.setTitle(alertMsg);
		alert.setCancelable(false);
		alert.setMessage(myContext.getResources().getString(R.string.alert_browser)+" and this URL "+CommonData.archives_url+" will open in default browser.");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Uri url = Uri.parse(CommonData.archives_url);
				Intent intent = new Intent(Intent.ACTION_VIEW, url);
				myContext.startActivity(intent);
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alert.showPdf();
	}

	public static void openBrowserURL(final Context myContext, String alertMsg, final String url, Exception e) {
		AlertDialog.Builder alert = new AlertDialog.Builder(myContext);
		//alert.setTitle(alertMsg);
		final String link_url = url;
		alert.setCancelable(false);
		alert.setMessage(myContext.getResources().getString(R.string.alert_browser)+" and this URL "+link_url+" will open in default browser.");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Uri url = Uri.parse(link_url.toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, url);
				myContext.startActivity(intent);
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alert.showPdf();
	}
*/


	
	
	/*
	 * public static void showAlert(Context _activity, String title, String
	 * alertMsg, boolean goBack) { AlertDialog.Builder alert = new
	 * AlertDialog.Builder(_activity); alert.setTitle(title);
	 * alert.setCancelable(false); alert.setMessage(alertMsg);
	 * alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { } });
	 * 
	 * alert.showPdf(); }
	 * 
	 * public static boolean checkInternetConnection(Context _activity) {
	 * ConnectivityManager conMgr = (ConnectivityManager)
	 * _activity.getSystemService(Context.CONNECTIVITY_SERVICE); if
	 * (conMgr.getActiveNetworkInfo() != null &&
	 * conMgr.getActiveNetworkInfo().isAvailable() &&
	 * conMgr.getActiveNetworkInfo().isConnected()) return true; else return
	 * false; }
	 */
}
