package in.gov.cgg.alumni.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;

public class SplashActivity extends AppCompatActivity {
    // private ProgressDialog pd;
    private DatabaseReference databaseArtists;
    List<Details> members;
    boolean isExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        databaseArtists = FirebaseDatabase.getInstance().getReference("member");
        members = new ArrayList<>();

        //  pd = new ProgressDialog(SplashActivity.this);
        //  pd.setCancelable(false);
        //pd.setMessage("Please wait");

        try {
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                            }
                            Log.d("Token", msg);
                            //Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String mpin = preferences.getString("mpin", "");
            String mobile = preferences.getString("mobile", "");

            Log.d("mobileee", mobile);

//
//        if (CheckInternet.isOnline(SplashActivity.this)) {
//
//
//            //checkExistance(mobile);
//        } else {
//            Toast.makeText(SplashActivity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();
//
//        }

            if (mpin != null && !mpin.equalsIgnoreCase("") &&
                    mobile != null && !mobile.equalsIgnoreCase("")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //showAlert();
                        startActivity(new Intent(SplashActivity.this, EnterMPinActivity.class));
                        finish();
                    }
                }, 3000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //showAlert();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }, 3000);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkExistance(final String mobile) {
        //  pd.show();

        //attaching value event listener
        databaseArtists.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                members.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Details artist = postSnapshot.getValue(Details.class);
                    //adding artist to the list
                    members.add(artist);


                }
                // GlobalDeclaration.tempList = members;


                for (int i = 0; i < members.size(); i++) {
                    if (members.get(i).getMobileNo().trim().equalsIgnoreCase(mobile)) {
                        isExist = true;
                        GlobalDeclaration.profileDetails = members.get(i);
                        //GlobalDeclaration.type = members.get(i).getType();
                        GlobalDeclaration.id = i;
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("id", String.valueOf(i));
                        editor.putString("mpin", GlobalDeclaration.profileDetails.getMpin());
                    }
                    if (GlobalDeclaration.profileDetails != null) {
                        if (GlobalDeclaration.profileDetails.getBatchno() != null) {
                            if (!GlobalDeclaration.profileDetails.getBatchno().isEmpty()) {
                                if (members.get(i).getBatchno().equalsIgnoreCase(GlobalDeclaration.profileDetails.getBatchno())) {
                                    GlobalDeclaration.tempList.add(members.get(i));
                                }
                            }
                        }
                    }
                }

                //pd.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}