package in.gov.cgg.alumni.activities;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.databinding.ActivityLoginBinding;
import in.gov.cgg.alumni.utils.CheckInternet;

public class LoginActivity extends AppCompatActivity {
    View background;
    //Button btn_submit;
    ////TextInputEditText et_mobile;
    String mobile;
    ProgressDialog pd;
    List<Details> members;
    boolean isExist = false;
    boolean isSet = false;
    //RelativeLayout relativelayout_details;
    private DatabaseReference databaseArtists;
    private boolean flag;
ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
      /*  et_mobile = findViewById(R.id.et_mobile);
        btn_submit = findViewById(R.id.btn_submit);
        relativelayout_details = findViewById(R.id.relativelayout_details);
*/

        pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait");

               members = new ArrayList<>();
        checkExistance();


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isOnline(LoginActivity.this)) {
                    try {

                        mobile = binding.etMobile.getText().toString().trim();
                        if (((Pattern.matches("[0-9][0-9]{9}", mobile)) && mobile.length() == 10)) {
                            GlobalDeclaration.mobileNumber = mobile;

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("mobile", mobile);
                            editor.apply();

                            if (members.size() > 0) {
                                for (int i = 0; i < members.size(); i++) {
                                    if (members.get(i).getMobileNo().trim().equalsIgnoreCase(mobile)) {
                                        isExist = true;
                                        GlobalDeclaration.profileDetails = members.get(i);
                                        // GlobalDeclaration.type = members.get(i).getType();
                                        GlobalDeclaration.id = i;
                                        editor.putString("id", String.valueOf(i));
                                        editor.putString("mpin", members.get(i).getMpin());
                                        editor.apply();
                                        if (members.get(i).getIsSet().equalsIgnoreCase("true")) {
                                            isSet = true;
                                            break;
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
                                        break;
                                    }


                                }
                            }

                            if (!isExist) {
                                pd.dismiss();
//                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                                builder1.setMessage("Entered mobile number not registered");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            @SuppressLint("NewApi")
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                            } else if (isExist && isSet) {
                                pd.dismiss();
                                startActivity(new Intent(LoginActivity.this, EnterMPinActivity.class));
                            } else if (isExist && !isSet) {
                                startActivity(new Intent(LoginActivity.this, SetMpinActivity.class));
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, "Please Enter valid mobile Number", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private List<Details> checkExistance() {
        pd.show();

        databaseArtists = FirebaseDatabase.getInstance().getReference("member");

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


//                //GlobalDeclaration.tempList = members;
//
//                if (members.size() > 0) {
//                    for (int i = 0; i < members.size(); i++) {
//                        if (members.get(i).getMobileNo().trim().equalsIgnoreCase(mobile)) {
//                            isExist = true;
//                            GlobalDeclaration.profileDetails = members.get(i);
//                            // GlobalDeclaration.type = members.get(i).getType();
//                            GlobalDeclaration.id = i;
//                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                            SharedPreferences.Editor editor = preferences.edit();
//                            editor.putString("id", String.valueOf(i));
//                            editor.putString("mpin", members.get(i).getMpin());
//                            editor.apply();
//                            if (members.get(i).getIsSet().equalsIgnoreCase("true")) {
//                                isSet = true;
//                                break;
//                            }
//
//                            if (GlobalDeclaration.profileDetails != null) {
//                                if (GlobalDeclaration.profileDetails.getBatchno() != null) {
//                                    if (!GlobalDeclaration.profileDetails.getBatchno().isEmpty()) {
//                                        if (members.get(i).getBatchno().equalsIgnoreCase(GlobalDeclaration.profileDetails.getBatchno())) {
//                                            GlobalDeclaration.tempList.add(members.get(i));
//
//                                        }
//                                    }
//                                }
//                            }
//                            return;
//                        }
//
//
//
//                    }
//                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });
        return members;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    @Override
    protected void onPause() {
        super.onPause();
        pd.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pd.dismiss();
    }
}
