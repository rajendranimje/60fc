package in.gov.cgg.alumni.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
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
    Details details;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        databaseArtists = FirebaseDatabase.getInstance().getReference("member");
        members = new ArrayList<>();

        //addUser();
        //  pd = new ProgressDialog(SplashActivity.this);
        //  pd.setCancelable(false);
        //pd.setMessage("Please wait");


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //mAuth.signInWithEmailAndPassword(BuildConfig.mailId, BuildConfig.pass)
        mAuth.signInWithEmailAndPassword("rajendranimje@gmail.com", "RA616490$")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in success, user is signed in
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null) {
                               /* Toast.makeText(SplashActivity.this, "Authentication Success: " + mAuth.getUid(),
                                        Toast.LENGTH_SHORT).show();*/
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


                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                                    final String mpin = preferences.getString("mpin", "");
                                    String mobile = preferences.getString("mobile", "");

                                    Log.d("mobileee", mobile);

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
                            // Proceed with your app logic
                        } else {
                            // If sign-in fails, handle specific exceptions
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidUserException) {
                                // Handle invalid user (user does not exist)
                                Log.d("TAG", "Invalid user: " + exception.getMessage());
                            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                // Handle invalid credentials (wrong password)
                                Log.d("TAG", "Invalid credentials: " + exception.getMessage());
                            } else {
                                // Handle other authentication failures
                                Log.d("TAG", "Authentication failed: " + exception.getMessage());
                            }
                            Toast.makeText(SplashActivity.this, "Authentication failed: " + exception.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void addUser() {
        String id = databaseArtists.push().getKey();

        details = new Details();
        details.setMemberId(id);
        details.setName("Rekha");
        details.setDesignation("PA");
        details.setDepartment("databaseArtists");
        details.setDateOfPosting("25/07/2012");
        details.setMobileNo("7036266270");
        details.setOfficeAddress("CGG Hyderabad Data is measured, collected and reported, and analyzed, whereupon it can be visualized using graphs, images or other analysis tools. Data as a general concept refers to the fact that some existing information or knowledge is represented or coded in some form suitable for better usage or processing.Data is measured, collected and reported, and analyzed, whereupon it can be visualized using graphs, images or other analysis tools. Data as a general concept refers to the fact that some existing information or knowledge is represented or coded in some form suitable for better usage or processing.");
        details.setProfilePicUrl("https://firebasestorage.googleapis.com/v0/b/dgproject-9990b.appspot.com/o/ProfilePics%2FMahesh%20RelluProfilePic.jpeg?alt=media&token=89c99710-1241-4414-9dd6-3ab1dda3c3cf");
        details.setFamilyPicUrl("https://firebasestorage.googleapis.com/v0/b/dgproject-9990b.appspot.com/o/FamilyPics%2FMahesh%20RelluFamilyPic.jpeg?alt=media&token=0a838487-53e0-40fa-a508-f1560bf957e5");
        details.setCity("Hyderabad");
        details.setEmail("R@gmail.com");
        details.setType("");
        details.setIsSet("false");
        details.setService("IPS");
        details.setMpin("");
        details.setBatchno("1994");
        details.setCountryCode("91");
        //Saving the Artist
        databaseArtists.child(id).setValue(details);


        //displaying a success toast
        Toast.makeText(this, "Member added Successfully", Toast.LENGTH_LONG).show();
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