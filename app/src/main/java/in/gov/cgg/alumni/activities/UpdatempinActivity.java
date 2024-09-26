package in.gov.cgg.alumni.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.databinding.ActivityUpdatempinBinding;

public class UpdatempinActivity extends Activity {

    //PinView pinview1, pinview2, pin_current;
    private DatabaseReference databaseMembers;
    String otp1;
    boolean otp1ok = false, otp2ok = false;
    //Button generate;
    String mpin;

ActivityUpdatempinBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_updatempin);
        //setContentView(R.layout.activity_updatempin);
        databaseMembers = FirebaseDatabase.getInstance().getReference("member");
      /*  pinview1 = findViewById(R.id.pin1);
        pin_current = findViewById(R.id.pin_current);
        pinview2 = findViewById(R.id.pin2);
        generate = findViewById(R.id.generate_btn);*/

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UpdatempinActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        mpin = preferences.getString("mpin", "");

        binding.generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp1 = binding.pin1.getText().toString().trim();
                if (validateFields()) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UpdatempinActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();

//                    editor.putString("mpin", GlobalDeclaration.profileDetails.getMpin());
//                    editor.putString("mobile", GlobalDeclaration.profileDetails.getMobileNo());


                    // final String id = preferences.getString("id", "");
                    String id = GlobalDeclaration.profileDetails.getMemberId();
                    databaseMembers.child(id).child("mpin").setValue(otp1);
                    databaseMembers.child(id).child("isSet").setValue("true");

                    editor.putString("mpin", otp1);
                    editor.apply();

                    startActivity(new Intent(UpdatempinActivity.this, EnterMPinActivity.class).putExtra("mpin", otp1).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            }
        });
    }


    private boolean validateFields() {


        if (binding.pinCurrent.getText().toString().trim().length() == 0 || binding.pinCurrent.getText().toString().trim().length() < 6) {
            // pinview1.setError("Please Enter Valid MPIN");
            Toast.makeText(UpdatempinActivity.this, "Please Enter Valid Current MPIN", Toast.LENGTH_SHORT).show();
            binding.pinCurrent.setText("");
            return false;
        }
        else if (!mpin.equalsIgnoreCase(binding.pinCurrent.getText().toString())) {
            Toast.makeText(UpdatempinActivity.this, "Invalid current MPIN", Toast.LENGTH_SHORT).show();

            return false;
        }



        else if (binding.pin1.getText().toString().trim().length() == 0 || binding.pin1.getText().toString().trim().length() < 6) {
            // pinview1.setError("Please Enter Valid MPIN");
            Toast.makeText(UpdatempinActivity.this, "Please Enter Valid MPIN in New MPIN", Toast.LENGTH_SHORT).show();
            binding.pin1.setText("");
            return false;
        } else if (binding.pin2.getText().toString().trim().length() == 0 || binding.pin2.getText().toString().trim().length() < 6) {
            //pinview2.setError("Please Enter Valid MPIN");
            Toast.makeText(UpdatempinActivity.this, "Please Enter Valid MPIN in Re-enter MPIN", Toast.LENGTH_SHORT).show();
            binding.pin2.setText("");
            return false;
        }

        else if (!binding.pin1.getText().toString().equalsIgnoreCase(binding.pin2.getText().toString())) {
            //  pinview2.setError("MPIN does not matched");
            Toast.makeText(UpdatempinActivity.this, "New MPIN and Re-entered MPINs are mismatched", Toast.LENGTH_SHORT).show();
            binding.pin2.setText("");

            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdatempinActivity.this, EditProfileActivity.class));
    }
}

