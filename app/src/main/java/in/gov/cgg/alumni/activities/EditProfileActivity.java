package in.gov.cgg.alumni.activities;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.databinding.ActivityEditprofileBinding;
import in.gov.cgg.alumni.utils.CheckInternet;

public class EditProfileActivity extends AppCompatActivity {

    View background;
   /* private TextInputLayout input_layout_username, input_layout_des, input_layout_dept, input_layout_date,
            input_layout_mbl, input_layout_address, input_layout_city, input_layout_email;*/
   // private Button btn_signup, btn_cancel;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference databaseMembers;
    private StorageReference mstorageReference;
    //private CircleImageView profile_imageview, family_imageview;
    private String flag;
    private Uri mProfilePicUri, mFamilyPicUri;
    private String userName = "", designation = "", department = "", dateOfPosting = "", mobileNo,
            address = "", profilePicUrl = "", familyPicUrl = "", city = "", email = "";
    private StorageTask<UploadTask.TaskSnapshot> mProfileUploadTask, mFamilyUploadTask;
    ProgressDialog pd;
    Details bean;
    String profileID;
    //Spinner spinner, spinnerbatch;
    String type, batchno;
    String selectedItem = "", selectedbatch = "";
    private DatePickerDialog dobDatePickerDialog;
    //TextInputEditText input_date;
    private DatabaseReference databaseArtists;
    private DatabaseReference batches;
    private List<String> servicelist, batcheslist;
    //FloatingActionButton fabkey;
ActivityEditprofileBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_editprofile);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editprofile);

        pd = new ProgressDialog(EditProfileActivity.this);
        pd.setMessage("Please wait,Loading");
        pd.setCancelable(false);

       /* fabkey = findViewById(R.id.fabkey);
        input_layout_username = findViewById(R.id.input_layout_username);
        input_date = findViewById(R.id.input_date);
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
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_signup.setText("UPDATE");
        spinnerbatch = findViewById(R.id.spinner_batch);
        spinner = findViewById(R.id.spinner1);
//        spinner.setEnabled(false);
        spinnerbatch.setEnabled(false);
        spinnerbatch.setClickable(false);
//        spinner.setClickable(false);*/
        binding.btnSignup.setText("UPDATE");
        binding.spinnerBatch.setEnabled(false);
        binding.spinnerBatch.setClickable(false);
        servicelist = new ArrayList<>();
        batcheslist = new ArrayList<>();

//        List<String> list = new ArrayList<String>();
//        list.add("IAS");
//        list.add("IPS");
//        list.add("IFS");
//        list.add("IFoS");
//
//        List<String> listbatch = new ArrayList<String>();
//        listbatch.add("1994");
//
//        //create an ArrayAdaptar from the String Array
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        //set the view for the Drop down list
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //set the ArrayAdapter to the spinner
//        spinner.setAdapter(dataAdapter);
//        //attach the listener to the spinner
//
//
//        //create an ArrayAdaptar from the String Array
//        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, listbatch);
//        //set the view for the Drop down list
//        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //set the ArrayAdapter to the spinner
//        spinnerbatch.setAdapter(dataAdapter2);
//        //attach the listener to the spinner
        binding.fabkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, UpdatempinActivity.class));
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
                            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(EditProfileActivity.this,
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
                            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(EditProfileActivity.this,
                                    android.R.layout.simple_spinner_item, GlobalDeclaration.batcheslist);
                            //set the view for the Drop down list
                            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //set the ArrayAdapter to the spinner
                            binding.spinner1.setAdapter(dataAdapter2);


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

        binding.inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar newCalendar = Calendar.getInstance();
// mDatePicker
                dobDatePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);


                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


                        dateOfPosting = format.format(newDate.getTime());

                        binding.inputLayoutDate.getEditText().setText(dateOfPosting);

                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                dobDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dobDatePickerDialog.show();
            }
        });
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
                selectedbatch = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (GlobalDeclaration.profileDetails != null) {
            bean = GlobalDeclaration.profileDetails;
            profileID = bean.getMemberId();
            userName = bean.getName();
            department = bean.getDepartment();
            designation = bean.getDesignation();
            mobileNo = bean.getMobileNo();
            email = bean.getEmail();
            address = bean.getOfficeAddress();
            dateOfPosting = bean.getDateOfPosting();
            familyPicUrl = bean.getFamilyPicUrl();
            profilePicUrl = bean.getProfilePicUrl();
            type = bean.getType();
            dateOfPosting = bean.getDateOfPosting();
            batchno = bean.getBatchno();
            selectedItem = bean.getService();
        }
        binding.inputLayoutUsername.getEditText().setText(bean.getName());
        binding.inputLayoutDept.getEditText().setText(bean.getDepartment());
        binding.inputLayoutDes.getEditText().setText(bean.getDesignation());
        binding.inputLayoutDate.getEditText().setText(bean.getDateOfPosting());
        binding.inputLayoutMbl.getEditText().setText(bean.getCountryCode() + bean.getMobileNo());
        binding.inputLayoutAddress.getEditText().setText(bean.getOfficeAddress());
        binding.inputLayoutCity.getEditText().setText(bean.getCity());
        binding.inputLayoutEmail.getEditText().setText(bean.getEmail());


        if (!bean.getFamilyPicUrl().isEmpty()) {
//            Picasso.with(EditProfileActivity.this)
//                    .load(bean.getFamilyPicUrl())
//                    .error(R.drawable.user).into(family_imageview);
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.loadingimg_red)
                            .error(R.drawable.user))
                    .load(bean.getFamilyPicUrl())
                    .into(binding.familyPic);

        } else {
            binding.familyPic.setImageResource(R.color.white);

        }
        if (!bean.getProfilePicUrl().isEmpty()) {

//            Picasso.with(EditProfileActivity.this)
//                    .load(bean.getProfilePicUrl())
//                    .error(R.drawable.user).into(profile_imageview);
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.loadingimg_red)
                            .error(R.drawable.user))
                    .load(bean.getProfilePicUrl())
                    .into(binding.profilePic);


        } else {
            binding.profilePic.setImageResource(0);
        }
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
                if (CheckInternet.isOnline(EditProfileActivity.this)) {
                    if (validatefields(v)) {
                        databaseMembers = FirebaseDatabase.getInstance().getReference("member").child(profileID);
                        mstorageReference = FirebaseStorage.getInstance().getReference("member_storage");
                        binding.btnSignup.setClickable(false);
                        binding.btnSignup.setEnabled(false);
                        userName = binding.inputLayoutUsername.getEditText().getText().toString().trim();
                        designation = binding.inputLayoutDes.getEditText().getText().toString().trim();
                        department = binding.inputLayoutDept.getEditText().getText().toString().trim();
                        dateOfPosting = binding.inputLayoutDate.getEditText().getText().toString().trim();
                        mobileNo = binding.inputLayoutMbl.getEditText().getText().toString().trim();
                        address = binding.inputLayoutAddress.getEditText().getText().toString().trim();
                        city = binding.inputLayoutCity.getEditText().getText().toString().trim();
                        email = binding.inputLayoutEmail.getEditText().getText().toString().trim();
                        uploadDetails();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, DashboardActivity.class));
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
//                Glide.with(this)
//                        .applyDefaultRequestOptions(new RequestOptions()
//                                .placeholder(R.drawable.loadingimg_red)
//                                .error(R.drawable.user))
//                        .load(bean.getProfilePicUrl())
//                        .into(profile_imageview);
//                Picasso.with(this).load(mProfilePicUri).into(profile_imageview);

                Glide.with(this)
                        .load(mProfilePicUri)
                        .into(binding.profilePic);
//

            } else {
                mFamilyPicUri = data.getData();
                Glide.with(this)
                        .load(mFamilyPicUri)
                        .into(binding.familyPic);
//                Picasso.with(this).load(familyPicUrl).into(family_imageview);

            }
        }
    }

    private void uploadDetails() {
        if (mProfilePicUri != null && mFamilyPicUri != null) {
            uploadProfilePic2();
            uploadFamilyPic2();

        }
        if (mProfilePicUri != null) {

            uploadProfilePic2();

        } else if (mFamilyPicUri != null) {
            uploadFamilyPic2();
        } else {
            uploadData();
            // Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadFamilyPic2() {
        pd.show();
        final StorageReference familyFileReference = mstorageReference.child(userName + "_family" +  "." + getFileExtension(mFamilyPicUri));
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
                    Toast.makeText(EditProfileActivity.this, "Family Image upload successful", Toast.LENGTH_LONG).show();
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
        final StorageReference profileFileReference = mstorageReference.child(userName + "_profile" + "." + getFileExtension(mProfilePicUri));

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

                    profilePicUrl = downloadUri.toString();

                   // String compimg=compressImage(profilePicUrl);

                    Toast.makeText(EditProfileActivity.this, "Profile Image upload successful", Toast.LENGTH_LONG).show();

                    uploadData();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void uploadData() {
        if (!EditProfileActivity.this.isFinishing()) {
            pd.show();
        }

        Details updatedUser = new Details();
        updatedUser.setMemberId(profileID);
        updatedUser.setName(userName);
        updatedUser.setCity(city);
        updatedUser.setOfficeAddress(address);
        updatedUser.setDateOfPosting(dateOfPosting);
        updatedUser.setDepartment(department);
        updatedUser.setDesignation(designation);
        updatedUser.setMobileNo(GlobalDeclaration.profileDetails.getMobileNo());
        updatedUser.setEmail(email);
        updatedUser.setProfilePicUrl(profilePicUrl);
        updatedUser.setFamilyPicUrl(familyPicUrl);
        updatedUser.setType(type);
        updatedUser.setService(selectedItem);
        updatedUser.setMpin(GlobalDeclaration.profileDetails.getMpin());
        updatedUser.setIsSet(GlobalDeclaration.profileDetails.getIsSet());
        updatedUser.setBatchno(selectedbatch);

        updatedUser.setCountryCode(GlobalDeclaration.profileDetails.getCountryCode());

        databaseMembers.setValue(updatedUser);
        pd.dismiss();


//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditProfileActivity.this);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("mobile", mobileNo);
//        editor.apply();

        //displaying a success toast
        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditProfileActivity.this);
        builder1.setMessage("Details updated successfully");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(EditProfileActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private boolean validatefields(View view) {

        if (binding.profilePic.getDrawable() == null) {
            Snackbar.make(view, "Please choose Profile picture", Snackbar.LENGTH_SHORT).show();
            binding.profilePic.requestFocus();
            return false;
        }
//        if (mProfilePicUri == null) {
//            Snackbar.make(view, "Please choose Profile picture", Snackbar.LENGTH_SHORT).show();
//            profile_imageview.requestFocus();
//            return false;
//        }
        if (binding.inputLayoutUsername.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutUsername.requestFocus();
            Snackbar.make(view, "Please enter valid Username", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (binding.inputLayoutDes.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutDes.requestFocus();
            Snackbar.make(view, "Please enter valid Designation", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (selectedItem.equalsIgnoreCase("") || selectedbatch.contains("Select")) {
            Snackbar.make(view, "Please Select Service", Snackbar.LENGTH_SHORT).show();
            return false;
        }
//
        else if (binding.inputLayoutDept.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutDept.requestFocus();
            Snackbar.make(view, "Please enter valid Department/Ministry", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (binding.inputLayoutDate.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutDate.requestFocus();
            Snackbar.make(view, "Please enter valid Date of Posting", Snackbar.LENGTH_SHORT).show();
            return false;
        }
//        else if (input_layout_mbl.getEditText().getText().toString().trim().isEmpty()) {
//            input_layout_mbl.requestFocus();
//            Snackbar.make(view, "Please enter valid MobileNo", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }
//
//        else if (input_layout_address.getEditText().getText().toString().trim().isEmpty()) {
//            input_layout_address.requestFocus();
//            Snackbar.make(view, "Please enter valid  Office Address", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }

        else if (binding.inputLayoutCity.getEditText().getText().toString().trim().isEmpty()) {
            binding.inputLayoutCity.requestFocus();
            Snackbar.make(view, "Please enter City", Snackbar.LENGTH_SHORT).show();
            return false;
        }

//        else if (mFamilyPicUri == null) {
//            Snackbar.make(view, "Please choose Family picture", Snackbar.LENGTH_SHORT).show();
//            return false;
//        }

        else {
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
        Intent intent = new Intent(EditProfileActivity.this, DashboardActivity.class);
        startActivity(intent);
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
