package in.gov.cgg.alumni.activities;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import in.gov.cgg.alumni.databinding.ActivityAdduserBinding;
import in.gov.cgg.alumni.utils.CheckInternet;

public class AddUserActivity extends AppCompatActivity {

   /* private TextInputLayout input_layout_username, input_layout_des, input_layout_dept, input_layout_date,
            input_layout_mbl, input_layout_address, input_layout_city, input_layout_email;*/
    //private Button btn_signup;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference databaseMembers;
    private StorageReference mstorageReference;
    // private CircleImageView profile_imageview, family_imageview;
    private String flag;
    private Uri mProfilePicUri, mFamilyPicUri;
    private String userName = "", designation = "", department = "", dateOfPosting = "", mobileNo = "", address = "",
            profilePicUrl = "", familyPicUrl = "", city = "", email = "", type = "";
    private StorageTask<UploadTask.TaskSnapshot> mProfileUploadTask;
    private StorageTask<UploadTask.TaskSnapshot> mFamilyUploadTask;
    private Details details;
    private List<String> servicelist, batcheslist;
    SharedPreferences sharedPreferences;
    private String sp_name, sp_key;
    List<Details> proDetailsList, temp, members;
    ProgressDialog pd;
  // Spinner spinner, spinnerbatch;
    String selectedItem = "", selectedBatch = "";
    //Button home;
    private DatabaseReference databaseArtists;
    private DatabaseReference batches;
    TextInputEditText id_code;
ActivityAdduserBinding binding;
    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_adduser);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adduser);
        pd = new ProgressDialog(AddUserActivity.this);
        pd.setMessage("Please wait,Loading");
        pd.setCancelable(false);
        pd.show();

       /* input_layout_username = findViewById(R.id.input_layout_username);
        id_code = findViewById(R.id.id_code);
        input_layout_des = findViewById(R.id.input_layout_des);
        input_layout_dept = findViewById(R.id.input_layout_dept);
        input_layout_date = findViewById(R.id.input_layout_date);
        input_layout_mbl = findViewById(R.id.input_layout_mbl);
        input_layout_address = findViewById(R.id.input_layout_address);
        input_layout_city = findViewById(R.id.input_layout_city);
        input_layout_email = findViewById(R.id.input_layout_email);
        //profile_imageview = findViewById(R.id.profile_pic);
        // family_imageview = findViewById(R.id.family_pic);
        btn_signup = findViewById(R.id.btn_signup);
        spinner = findViewById(R.id.spinner1);
        spinnerbatch = findViewById(R.id.spinner_batch);
        home = findViewById(R.id.btn_home);
*/
        servicelist = new ArrayList<>();
        batcheslist = new ArrayList<>();

        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddUserActivity.this, DashboardActivity.class));
            }
        });


        databaseArtists = FirebaseDatabase.getInstance().getReference("services");
        batches = FirebaseDatabase.getInstance().getReference("batches");

        try {
            databaseArtists.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    servicelist.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting artist
                        String artist = postSnapshot.getValue(String.class);
                        //adding artist to the list
                        servicelist.add(artist);
                    }

                    Log.e("servicesalll", servicelist.get(0));
                    GlobalDeclaration.services = servicelist;
                    if (GlobalDeclaration.services != null) {
                        if (GlobalDeclaration.services.size() > 0) {
                            //create an ArrayAdaptar from the String Array
                            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(AddUserActivity.this,
                                    android.R.layout.simple_spinner_item, GlobalDeclaration.services);
                            //set the view for the Drop down list
                            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //set the ArrayAdapter to the spinner
                            binding.spinner1.setAdapter(dataAdapter1);
                        }
                    }

                    pd.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            batches.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    batcheslist.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting artist
                        String artist = postSnapshot.getValue(String.class);
                        //adding artist to the list
                        batcheslist.add(artist);
                    }

                    Log.e("batchesss", batcheslist.get(0));
                    GlobalDeclaration.batcheslist = batcheslist;
                    if (GlobalDeclaration.batcheslist != null) {
                        if (GlobalDeclaration.batcheslist.size() > 0) {

                            //create an ArrayAdaptar from the String Array
                            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(AddUserActivity.this,
                                    android.R.layout.simple_spinner_item, GlobalDeclaration.batcheslist);
                            //set the view for the Drop down list
                            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //set the ArrayAdapter to the spinner
                            binding.spinnerBatch.setAdapter(dataAdapter2);


                        }
                    }
                    pd.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


//        List<String> list = new ArrayList<String>();
//        list.add("IAS");
//        list.add("IPS");
//        list.add("IFS");
//        list.add("IFoS");

//        List<String> list2 = new ArrayList<String>();
//        list2.add("1994");


        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBatch = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //  input_layout_mbl.getEditText().setText(GlobalDeclaration.mobileNumber);

//        profile_imageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flag = "profilepic";
//                openFileChooser();
//            }
//        });
//        family_imageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flag = "familypic";
//                openFileChooser();
//            }
//        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isOnline(AddUserActivity.this)) {
                    if (validatefields(v)) {
                        try {
                            databaseMembers = FirebaseDatabase.getInstance().getReference("member");
                            mstorageReference = FirebaseStorage.getInstance().getReference("member_storage");
                            binding.btnSignup.setClickable(false);
                            // uploadDetails();

                            boolean check = false;
                            for (int i = 0; i < GlobalDeclaration.tempList.size(); i++) {

                                if (GlobalDeclaration.tempList.get(i).getMobileNo().equalsIgnoreCase(binding.inputLayoutMbl.getEditText().getText().toString().trim())
                                        ) {

                                    check = true;
                                    break;
                                }
                            }

                            if (check) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddUserActivity.this);
                                builder1.setMessage("User already registered with this mobile number");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            @SuppressLint("NewApi")
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {
                                uploadData();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(AddUserActivity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();

                    }
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
                // Picasso.with(this).load(mProfilePicUri).into(profile_imageview);
            } else {
                mFamilyPicUri = data.getData();
                //Picasso.with(this).load(mFamilyPicUri).into(family_imageview);
            }
        }
    }

    private void uploadDetails() {
        if (mProfilePicUri != null && mFamilyPicUri != null) {
            uploadProfilePic2();

        } else {
            // Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            uploadData();
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
                    //throw task.getException();
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
                    // throw task.getException();
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
                    profilePicUrl = downloadUri.toString();
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
        details.setDesignation("");
        details.setDepartment("");
        details.setDateOfPosting("");
        details.setMobileNo(mobileNo);
        details.setOfficeAddress("");
        details.setProfilePicUrl("");
        details.setFamilyPicUrl("");
        details.setCity("");
        details.setEmail("");
        details.setType("");
        details.setIsSet("");
        details.setService(selectedItem);
        details.setMpin("");
        details.setBatchno(selectedBatch);
        details.setCountryCode(binding.idCode.getText().toString().trim());
        //Saving the Artist
        databaseMembers.child(id).setValue(details);

        pd.dismiss();
        //displaying a success toast
        Toast.makeText(this, "Member added Successfully", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AddUserActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private boolean validatefields(View view) {
        if (binding.inputLayoutUsername.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutUsername.requestFocus();
            Snackbar.make(view, "Please enter valid user name", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (binding.idCode.getText().toString().isEmpty() && !(binding.idCode.getText().length() >= 1) && !(Pattern.matches("[+][0-9]{3}", binding.idCode.getText().toString()))) {
            Snackbar.make(view, "Please enter valid country code", Snackbar.LENGTH_SHORT).show();

            return false;
        }


        //else if (input_layout_des.getEditText().getText().toString().trim().isEmpty()) {
//            input_layout_des.requestFocus();
//            Snackbar.make(view, "Please enter valid Designation", Snackbar.LENGTH_SHORT).show();
//            return false;
//        } else if (input_layout_dept.getEditText().getText().toString().trim().isEmpty()) {
//            input_layout_dept.requestFocus();
//            Snackbar.make(view, "Please enter valid Department/Ministry", Snackbar.LENGTH_SHORT).show();
//            return false;
//        } else if (input_layout_date.getEditText().getText().toString().trim().isEmpty()) {
//            input_layout_date.requestFocus();
//            Snackbar.make(view, "Please enter valid Date of Posting", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//        else
//        if (input_layout_mbl.getEditText().getText().toString().trim().isEmpty()) {
//            input_layout_mbl.requestFocus();
//            Snackbar.make(view, "Please enter valid MobileNo", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
        else if ((!(Pattern.matches("[0-9]{10}", binding.inputLayoutMbl.getEditText().getText().toString().trim())
                && mobileNo.length() != 10))) {
            binding.inputLayoutMbl.requestFocus();
            Snackbar.make(view, "Invalid mobile number", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (selectedItem.equalsIgnoreCase("") || selectedItem.contains("Select")) {
            Snackbar.make(view, "Please Choose Service", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (selectedBatch.equalsIgnoreCase("") || selectedBatch.contains("Select")) {
            Snackbar.make(view, "Please Select Batch", Snackbar.LENGTH_SHORT).show();
            return false;
        }
//        } else if (input_layout_address.getEditText().getText().toString().trim().isEmpty()) {
//            input_layout_address.requestFocus();
//            Snackbar.make(view, "Please enter valid  Office Address", Snackbar.LENGTH_SHORT).show();
//            return false;
//        } else if (input_layout_city.getEditText().getText().toString().trim().isEmpty()) {
//            input_layout_city.requestFocus();
//            Snackbar.make(view, "Please enter City", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//        final String emailp = input_layout_email.getEditText().getText().toString().trim();
//        final String emailPattern = "[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*";
//        if (!emailp.matches(emailPattern)) {
//            input_layout_city.requestFocus();
//            Snackbar.make(view, "Please enter email id", Snackbar.LENGTH_SHORT).show();
//            return false;
//        } else if (mProfilePicUri == null) {
//            Snackbar.make(view, "Please choose Profile picture", Snackbar.LENGTH_SHORT).show();
//            return false;
//        } else if (mFamilyPicUri == null) {
//            Snackbar.make(view, "Please choose Family picture", Snackbar.LENGTH_SHORT).show();
//            return false;

        else {
            userName = binding.inputLayoutUsername.getEditText().getText().toString().trim();
//            designation = input_layout_des.getEditText().getText().toString().trim();
            //department = input_layout_dept.getEditText().getText().toString().trim();
            // dateOfPosting = input_layout_date.getEditText().getText().toString().trim();
            mobileNo = binding.inputLayoutMbl.getEditText().getText().toString().trim();
            //address = input_layout_address.getEditText().getText().toString().trim();
            // city = input_layout_city.getEditText().getText().toString().trim();
            //email = input_layout_email.getEditText().getText().toString().trim();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddUserActivity.this, DashboardActivity.class));
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
