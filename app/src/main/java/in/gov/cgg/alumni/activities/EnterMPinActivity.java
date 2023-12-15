package in.gov.cgg.alumni.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.databinding.ActivityEntermpinBinding;

public class EnterMPinActivity extends Activity {


   // PinView pinView;
    //TextView tv_forgot, tv_wrong, tv_log, tv_new;
    String mpin, pmpin;
    //Button validate_mpin_btn;
    //TextView tv_raise;
    ActivityEntermpinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_entermpin);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_entermpin);
      /*  pinView = findViewById(R.id.pin);
        tv_forgot = findViewById(R.id.tv_forgot);
        tv_log = findViewById(R.id.text_log);
        tv_new = findViewById(R.id.tv_new);
        tv_wrong = findViewById(R.id.tv_wrong);
        validate_mpin_btn = findViewById(R.id.validate_mpin_btn);

        tv_raise = findViewById(R.id.tv_raise);*/
        binding.tvRaise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnterMPinActivity.this, RaiseTicketActivity.class));
            }
        });



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String mpin = preferences.getString("mpin", "");
        final String mobile = preferences.getString("mobile", "");

        String first = mobile.substring(0, 3);
        String last = mobile.substring(7, 10);

        String newmobile = first + "*****" + last;

        binding.textLog.setText("Logged in with " + newmobile);
        binding.tvNew.setText("Not you?");
        Log.e("newmobile", newmobile);


        binding.pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.tvWrong.setVisibility(View.GONE);
            }
        });


        binding.tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.pin.setText("");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EnterMPinActivity.this);
                builder1.setMessage("Proceed to submit ticket");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int id) {

                                startActivity(new Intent(EnterMPinActivity.this, RaiseTicketActivity.class));

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        binding.validateMpinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields(v)) {
                    pmpin = binding.pin.getText().toString();
                    if (pmpin.equalsIgnoreCase(mpin)) {
                        binding.tvWrong.setVisibility(View.GONE);
                        startActivity(new Intent(EnterMPinActivity.this, DashboardActivity.class));

                    } else {

                        binding.pin.setText("");

//                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        hideKeyboardFrom(v);
                        binding.tvWrong.setVisibility(View.VISIBLE);
                        binding.tvWrong.requestFocus();
                    }
                }
            }
        });

        binding.tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EnterMPinActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("mobile", "");
                editor.putString("mpin", "");
                editor.apply();
                startActivity(new Intent(EnterMPinActivity.this, LoginActivity.class));

            }
        });

//        tv_forgot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(EnterMPinActivity.this);
//                builder1.setMessage("Press OK to Set MPIN");
//                builder1.setCancelable(false);
//
//                builder1.setPositiveButton(
//                        "OK",
//                        new DialogInterface.OnClickListener() {
//                            @SuppressLint("NewApi")
//                            public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(EnterMPinActivity.this, RaiseTicketActivity.class));
//                            }
//                        });
//                AlertDialog alert11 = builder1.create();
//                alert11.show();
//            }
//        });

    }


    private boolean validateFields(View v) {
        if (binding.pin.getText().toString().trim().length() == 0 || binding.pin.getText().toString().trim().length() < 6) {
            Toast.makeText(EnterMPinActivity.this, "Please Enter Valid MPIN", Toast.LENGTH_SHORT).show();
            binding.pin.setText("");
//
//            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            hideKeyboardFrom(v);
            return false;
        }
        return true;
    }
    public void hideKeyboardFrom( View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}