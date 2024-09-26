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
import in.gov.cgg.alumni.databinding.ActivitySetmpinBinding;

public class SetMpinActivity extends Activity {

    //PinView pinview1, pinview2;
    private DatabaseReference databaseMembers;
    String otp1;
    boolean otp1ok = false, otp2ok = false;
   // Button generate;
ActivitySetmpinBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_setmpin);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setmpin);
        databaseMembers = FirebaseDatabase.getInstance().getReference("member");
       /* pinview1 = findViewById(R.id.pin1);
        pinview2 = findViewById(R.id.pin2);
        generate = findViewById(R.id.generate_btn);*/
        binding.generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp1 = binding.pin1.getText().toString().trim();
                if (validateFields()) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SetMpinActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();

//                    editor.putString("mpin", GlobalDeclaration.profileDetails.getMpin());
//                    editor.putString("mobile", GlobalDeclaration.profileDetails.getMobileNo());

                    editor.putString("mpin", otp1);
                    editor.apply();

                   // final String id = preferences.getString("id", "");
                    String id=GlobalDeclaration.profileDetails.getMemberId();
                    databaseMembers.child(id).child("mpin").setValue(otp1);
                    databaseMembers.child(id).child("isSet").setValue("true");


                    startActivity(new Intent(SetMpinActivity.this, EnterMPinActivity.class).putExtra("mpin",otp1));
                }
            }
        });
    }


    private boolean validateFields() {

        if (binding.pin1.getText().toString().trim().length() == 0 || binding.pin1.getText().toString().trim().length() < 6) {
            // pinview1.setError("Please Enter Valid MPIN");
            Toast.makeText(SetMpinActivity.this, "Please Enter Valid MPIN", Toast.LENGTH_SHORT).show();
            binding.pin1.setText("");
            return false;
        } else if (binding.pin2.getText().toString().trim().length() == 0 || binding.pin2.getText().toString().trim().length() < 6) {
            //pinview2.setError("Please Enter Valid MPIN");
            Toast.makeText(SetMpinActivity.this, "Please Enter Valid MPIN", Toast.LENGTH_SHORT).show();
            binding.pin2.setText("");
            return false;
        } else if (!binding.pin1.getText().toString().equalsIgnoreCase(binding.pin2.getText().toString())) {
            //  pinview2.setError("MPIN does not matched");
            Toast.makeText(SetMpinActivity.this, "MPIN Mismatched. Please Enter Again", Toast.LENGTH_SHORT).show();
            binding.pin2.setText("");

            return false;
        }
        return true;
    }



}

