package in.gov.cgg.alumni.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {

    View background;
  /*  private TextInputLayout input_layout_username, input_layout_des, input_layout_dept, input_layout_date,
            input_layout_mbl, input_layout_address, input_layout_city, input_layout_email;
    private Button btn_signup;*/
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference databaseMembers;
    private StorageReference mstorageReference;
   // private CircleImageView profile_imageview, family_imageview;
    private String flag;
    private Uri mProfilePicUri, mFamilyPicUri;
    private String userName, designation, department, dateOfPosting, mobileNo, address, profilePicUrl, familyPicUrl, city, email;
    private StorageTask<UploadTask.TaskSnapshot> mProfileUploadTask;
    private StorageTask<UploadTask.TaskSnapshot> mFamilyUploadTask;
    private Details details;
    SharedPreferences sharedPreferences;
    private String sp_name, sp_key;
    List<Details> proDetailsList, temp, members;
    ProgressDialog pd;
    //Spinner spinner;
    String selectedItem;
ActivitySignupBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_signup);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        pd = new ProgressDialog(SignUpActivity.this);
        pd.setMessage("Please wait,Loading");
        pd.setCancelable(false);

      /*  input_layout_username = findViewById(R.id.input_layout_username);
        input_layout_des = findViewById(R.id.input_layout_des);
        input_layout_dept = findViewById(R.id.input_layout_dept);
        input_layout_date = findViewById(R.id.input_layout_date);
        input_layout_mbl = findViewById(R.id.input_layout_mbl);
        input_layout_address = findViewById(R.id.input_layout_address);
        input_layout_city = findViewById(R.id.input_layout_city);
        input_layout_email = findViewById(R.id.input_layout_email);
        profile_imageview = findViewById(R.id.profile_pic);
        family_imageview = findViewById(R.id.family_pic);
        btn_signup = findViewById(R.id.btn_signup);
        spinner=findViewById(R.id.spinner1);*/

        List<String> list = new ArrayList<String>();
        list.add("Select Service");
        list.add("IAS");
        list.add("IPS");
        list.add("IRS");
        list.add("IES");
        list.add("IFS");
        list.add("IFoS");

        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        binding.spinner1.setAdapter(dataAdapter);
        //attach the listener to the spinner

        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedItem = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.inputLayoutMbl.getEditText().setText(GlobalDeclaration.mobileNumber);

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "profilepic";
                openFileChooser();
            }
        });
        binding.familyPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "familypic";
                openFileChooser();
            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatefields(v)) {
                    databaseMembers = FirebaseDatabase.getInstance().getReference("member");
                    mstorageReference = FirebaseStorage.getInstance().getReference("member_storage");
                    binding.btnSignup.setClickable(false);
                    uploadDetails();
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            if (flag.equals("profilepic")) {
                mProfilePicUri = data.getData();
                //Picasso.with(this).load(mProfilePicUri).into(profile_imageview);
            } else {
                mFamilyPicUri = data.getData();
               // Picasso.with(this).load(mFamilyPicUri).into(family_imageview);
            }
        }
    }

    private void uploadDetails() {
        if (mProfilePicUri != null && mFamilyPicUri != null) {
            uploadProfilePic2();

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFamilyPic2() {
        pd.show();
        final StorageReference familyFileReference = mstorageReference.child(userName + "_family_" + System.currentTimeMillis()
                + "." + getFileExtension(mFamilyPicUri));
        mFamilyUploadTask = familyFileReference.putFile(mFamilyPicUri);

        Task<Uri> urlTask = mFamilyUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                   // throw task.getException();
                }
                // Continue with the task to get the download URL
                return familyFileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Uri downloadUri = task.getResult();
                    familyPicUrl = downloadUri.toString();
                    uploadData();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void uploadProfilePic2() {

        pd.show();
        final StorageReference profileFileReference = mstorageReference.child(userName + "_profile_" + System.currentTimeMillis()
                + "." + getFileExtension(mProfilePicUri));

        mProfileUploadTask = profileFileReference.putFile(mProfilePicUri);


        Task<Uri> urlTask = mProfileUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    //throw task.getException();
                }
                // Continue with the task to get the download URL
                return profileFileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Uri downloadUri = task.getResult();
                    if (downloadUri != null) {
                        profilePicUrl = downloadUri.toString();
                    }
                    uploadFamilyPic2();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

//    private void uploadProfilePic() {
//        pd.show();
//        StorageReference profileFileReference = mstorageReference.child(userName + "_profile_" + System.currentTimeMillis()
//                + "." + getFileExtension(mProfilePicUri));
//
//        mProfileUploadTask = profileFileReference.putFile(mProfilePicUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //mProgressBar.setProgress(0);
//                            }
//                        }, 500);
//                        // profilePicUrl = taskSnapshot.getDownloadUrl().toString();
//                        pd.dismiss();
//                        Toast.makeText(SignUpActivity.this, "Profile Image upload successful", Toast.LENGTH_LONG).show();
//                        uploadFamilyPic();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                        Toast.makeText(SignUpActivity.this, "Profile Image upload unsuccessful", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
////                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
////                            mProgressBar.setProgress((int) progress);
//
//                    }
//                });
//    }
//
//    private void uploadFamilyPic() {
//        pd.show();
//        StorageReference familyFileReference = mstorageReference.child(userName + "_family_" + System.currentTimeMillis()
//                + "." + getFileExtension(mFamilyPicUri));
//
//        mFamilyUploadTask = familyFileReference.putFile(mFamilyPicUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //mProgressBar.setProgress(0);
//                            }
//                        }, 500);
//                        //  familyPicUrl = taskSnapshot.getDownloadUrl().toString();
//                        pd.dismiss();
//                        Toast.makeText(SignUpActivity.this, "Family Image upload successful", Toast.LENGTH_LONG).show();
//                        uploadData();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                        Toast.makeText(SignUpActivity.this, "Family Image upload unsuccessful", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
////                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
////                            mProgressBar.setProgress((int) progress);
//                    }
//                });
//    }

    private void uploadData() {

        pd.show();
        String id = databaseMembers.push().getKey();

        details = new Details();
        details.setMemberId(id);
        details.setName(userName);
        details.setDesignation(designation);
        details.setDepartment(department);
        details.setDateOfPosting(dateOfPosting);
        details.setMobileNo(mobileNo);
        details.setOfficeAddress(address);
        details.setProfilePicUrl(profilePicUrl);
        details.setFamilyPicUrl(familyPicUrl);
        details.setCity(city);
        details.setEmail(email);
        details.setType("");
        details.setService(selectedItem);
        details.setMpin("");
        details.setIsSet("");
        //Saving the Artist
        if (id != null) {
            databaseMembers.child(id).setValue(details);
        }

        GlobalDeclaration.profileDetails=details;
        pd.dismiss();
        //displaying a success toast
        Toast.makeText(this, "Member added", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(SignUpActivity.this, SetMpinActivity.class);
        startActivity(intent);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private boolean validatefields(View view) {
        if ( binding.inputLayoutUsername.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutUsername.requestFocus();
            Snackbar.make(view, "Please enter valid Username", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        else if ( binding.inputLayoutDes.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutDes.requestFocus();
            Snackbar.make(view, "Please enter valid Designation", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if (selectedItem.isEmpty()||selectedItem.contains("Select")) {
            Snackbar.make(view, "Please Select Service", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        else if (binding.inputLayoutDept.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutDept.requestFocus();
            Snackbar.make(view, "Please enter valid Department/Ministry", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (binding.inputLayoutDate.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutDate.requestFocus();
            Snackbar.make(view, "Please enter valid Date of Posting", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if ((!(Pattern.matches("[6-9][0-9]{9}", binding.inputLayoutMbl.getEditText().getText().toString().trim())))) {
            binding.inputLayoutMbl.requestFocus();
            Snackbar.make(view, "Please enter valid MobileNo", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (binding.inputLayoutAddress.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutAddress.requestFocus();
            Snackbar.make(view, "Please enter valid  Office Address", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (binding.inputLayoutCity.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutCity.requestFocus();
            Snackbar.make(view, "Please enter City", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        final String emailp = binding.inputLayoutEmail.getEditText().getText().toString().trim();
        final String emailPattern = "[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*";
        if (!emailp.matches(emailPattern)) {
            binding.inputLayoutEmail.requestFocus();
            Snackbar.make(view, "Please enter email id", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (mProfilePicUri == null) {
            Snackbar.make(view, "Please choose Profile picture", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (mFamilyPicUri == null) {
            Snackbar.make(view, "Please choose Family picture", Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            userName = binding.inputLayoutUsername.getEditText().getText().toString().trim();
            designation = binding.inputLayoutDes.getEditText().getText().toString().trim();
            department = binding.inputLayoutDept.getEditText().getText().toString().trim();
            dateOfPosting = binding.inputLayoutDate.getEditText().getText().toString().trim();
            mobileNo = binding.inputLayoutMbl.getEditText().getText().toString().trim();
            address = binding.inputLayoutAddress.getEditText().getText().toString().trim();
            city = binding.inputLayoutCity.getEditText().getText().toString().trim();
            email = binding.inputLayoutEmail.getEditText().getText().toString().trim();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUpActivity.this);
        builder1.setMessage("Do you want to Exit from App?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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
