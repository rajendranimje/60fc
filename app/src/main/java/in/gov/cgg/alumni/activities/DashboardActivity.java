package in.gov.cgg.alumni.activities;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.databinding.ActivityMainBinding;
import in.gov.cgg.alumni.fcmfinal.FinalTextActivity;
import in.gov.cgg.alumni.library.MaterialSearchView;
import in.gov.cgg.alumni.utils.CheckInternet;

public class DashboardActivity extends AppCompatActivity {

   // private MaterialSearchView searchView;
   // RecyclerView rv_list;
    ListViewAdapter adapter;
   /// LinearLayout ll1, ll;
   // TextView tv_location, tv_dept, tv_desgi, tv_name, tv_mbl;
    //boolean name, dept, desg, loc;
    DatabaseReference databaseArtists;
    List<Details> members, tempList;
    List<String> byNameList, byDesgList, byDeptList, byCityList, byMoblList;
    Set<String> byNameListSet, byDesgListSet, byDeptListSet, byCityListSet, byMblListSet;
   // CircleImageView img_profile;
    //ImageView img_notifyclick;
    //SwipeRefreshLayout mSwipeRefreshLayout;
   // Button btnmaps, btnlist;
   // FloatingActionMenu fabadmin;
    //FloatingActionButton fabadd, fabtext;
   // com.google.android.material.floatingactionbutton.FloatingActionButton fabtextuser;
    String typehere;
    ProgressDialog pd;
   // TextView tv_nodata;
    String mobile;
ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

      /*  rv_list = findViewById(R.id.rv_list);
        tv_nodata = findViewById(R.id.tv_nodata);
        img_profile = findViewById(R.id.img_profile);*/
        databaseArtists = FirebaseDatabase.getInstance().getReference("member");
      /*  mSwipeRefreshLayout = findViewById(R.id.swipe);
        // btnlist = findViewById(R.id.buttonList);
        //  btnmaps = findViewById(R.id.buttonMaps);
        // fabuser = findViewById(R.id.fab_user);
        fabadmin = findViewById(R.id.fab_admin);
        fabadd = findViewById(R.id.fabAdd);
        fabtext = findViewById(R.id.fabText);
        fabtextuser = findViewById(R.id.fabTextuser);
        // fabraise = findViewById(R.id.fab_raise);


        img_notifyclick = findViewById(R.id.img_notifyclick);*/
        pd= new ProgressDialog(DashboardActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading..");
        pd.show();

        // refreshDetails();

        pd.show();


        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(DashboardActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                startActivity(new Intent(DashboardActivity.this, AddUserActivity.class));

            }
        });

        binding.fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, FinalTextActivity.class));
                //Toast.makeText(DashboardActivity.this, "Text messgae coming soon", Toast.LENGTH_SHORT).show();
            }
        });

//        fabtext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DashboardActivity.this, FinalTextActivity.class));
//                // Toast.makeText(DashboardActivity.this, "Text messgae coming soon", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        fabraise.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DashboardActivity.this, RaiseTicketActivity.class));
//                // Toast.makeText(DashboardActivity.this, "Text messgae coming soon", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        binding.fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, FinalTextActivity.class));
                // Toast.makeText(DashboardActivity.this, "Text messgae coming soon", Toast.LENGTH_SHORT).show();

            }
        });

        binding.imgNotifyclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, NotifyListActivity.class));
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mobile = preferences.getString("mobile", "");


        members = new ArrayList<>();
        byCityListSet = new LinkedHashSet<>();
        byDesgListSet = new LinkedHashSet<>();
        byDeptListSet = new LinkedHashSet<>();
        byNameListSet = new LinkedHashSet<>();
        byMblListSet = new LinkedHashSet<>();

        if (CheckInternet.isOnline(DashboardActivity.this)) {
            //attaching value event listener
            try {
                databaseArtists.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //clearing the previous artist list
                        members.clear();
                        GlobalDeclaration.tempList.clear();

                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting artist
                            Details artist = postSnapshot.getValue(Details.class);
                            //adding artist to the list
                            members.add(artist);
                        }

                        for (int i = 0; i < members.size(); i++) {

                            // byCityList.add(members.get(i).getCity());
                            byCityListSet.add(members.get(i).getCity());
                            byNameListSet.add(members.get(i).getName());
                            byDeptListSet.add(members.get(i).getDepartment());
                            byDesgListSet.add(members.get(i).getDesignation());
                            byMblListSet.add(members.get(i).getMobileNo());

//                    if (members.get(i).getMobileNo().trim().equalsIgnoreCase(mobile)) {
//                        GlobalDeclaration.profileDetails = members.get(i);
//                        if (GlobalDeclaration.profileDetails.getBatchno() != null) {
//                            if (!GlobalDeclaration.profileDetails.getBatchno().isEmpty()) {
//                                for (int j = 0; j < members.size(); j++) {
//                                    if (members.get(j).getBatchno().equalsIgnoreCase(GlobalDeclaration.profileDetails.getBatchno())) {
//                                        GlobalDeclaration.tempList.add(members.get(j));
//                                    }
//
//                                }
//                            }
//                        }
                            if (members.get(i).getMobileNo().trim().equalsIgnoreCase(mobile)) {
                                GlobalDeclaration.profileDetails = members.get(i);
                                GlobalDeclaration.id = i;
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

                        typehere = members.get(GlobalDeclaration.id).getType();
                        if (typehere != null) {
                            if (typehere.equalsIgnoreCase("admin")) {
                                binding.fabAdmin.setVisibility(View.VISIBLE);
                                binding.fabTextuser.setVisibility(View.GONE);
                            } else if (typehere.equalsIgnoreCase("user")) {
                                binding.fabAdmin.setVisibility(View.GONE);
                                binding.fabTextuser.setVisibility(View.VISIBLE);
                            } else {
                                binding.fabAdmin.setVisibility(View.GONE);
                                binding.fabTextuser.setVisibility(View.VISIBLE);

                            }
                        }

                        byNameList = new ArrayList<>(byNameListSet);

                        binding.searchView.setSuggestions(byNameList);

                        // GlobalDeclaration.tempList = members;

                        if (GlobalDeclaration.tempList != null) {
                            sortData(GlobalDeclaration.tempList);
                            binding.rvList.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                            adapter = new ListViewAdapter(DashboardActivity.this, GlobalDeclaration.tempList);
                            binding.rvList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {

                            binding.tvNodata.setVisibility(View.VISIBLE);
                        }
                        if (GlobalDeclaration.profileDetails.getProfilePicUrl().length() > 0) {
//                Picasso.with(context)
//                        .load(data_dashbord.get(i).getProfilePicUrl())
//                        .error(R.drawable.noimage).into(profile);
                            Glide.with(DashboardActivity.this)
                                    .applyDefaultRequestOptions(new RequestOptions()
                                            .placeholder(R.drawable.loadingimg_red)
                                            .error(R.drawable.userneww))
                                    .load(GlobalDeclaration.profileDetails.getProfilePicUrl())
                                    .into(binding.imgProfile);

                        } else {
                            binding.imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.userneww));

                        }

                        pd.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        pd.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(DashboardActivity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();

        }

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                binding.swipe.setRefreshing(false);
                if (members != null) {
                    try {
                        if (CheckInternet.isOnline(DashboardActivity.this)) {
                            try {
                                refreshDetails();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(DashboardActivity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(DashboardActivity.this, "Please try again", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  tv_location = findViewById(R.id.tv_loc);
        tv_dept = findViewById(R.id.tv_dept);
        tv_desgi = findViewById(R.id.tv_desgi);
        tv_name = findViewById(R.id.tv_name);
        tv_mbl = findViewById(R.id.tv_mblno);
        // rv_list = findViewById(R.id.rv_list);
        ll1 = findViewById(R.id.filter_ll);
        ll = findViewById(R.id.ll);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);*/
        binding.searchView.setEllipsize(true);
        binding.searchView.setHint("Search by Name");


//        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        binding.tvLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                binding.searchView.clearingall();

                byCityList = new ArrayList<>(byCityListSet);

                binding.searchView.setSuggestions(byCityList);

                binding.searchView.setVisibility(View.VISIBLE);

                binding.tvLoc.setBackgroundResource(R.drawable.inver_bt_background);
                binding.tvLoc.setTextColor(getResources().getColor(R.color.white));

                binding.tvName.setBackgroundResource(R.drawable.bt_background);
                binding.tvName.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvDept.setBackgroundResource(R.drawable.bt_background);
                binding.tvDept.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvDesgi.setBackgroundResource(R.drawable.bt_background);
                binding.tvDesgi.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvMblno.setBackgroundResource(R.drawable.bt_background);
                binding.tvMblno.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.searchView.setHint("Search by Location");


            }
        });
        binding.tvMblno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.searchView.clearingall();


                byMoblList = new ArrayList<>(byMblListSet);

                binding.searchView.setSuggestions(byMoblList);
                binding.searchView.setVisibility(View.VISIBLE);


                binding.tvMblno.setBackgroundResource(R.drawable.inver_bt_background);
                binding.tvMblno.setTextColor(getResources().getColor(R.color.white));

                binding.tvName.setBackgroundResource(R.drawable.bt_background);
                binding.tvName.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvDept.setBackgroundResource(R.drawable.bt_background);
                binding.tvDept.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvDesgi.setBackgroundResource(R.drawable.bt_background);
                binding.tvDesgi.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvLoc.setBackgroundResource(R.drawable.bt_background);
                binding.tvLoc.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.searchView.setHint("Search by Mobile number");


            }
        });


        binding.tvDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                name = false;
//                dept = true;
//                desg = false;

                binding.searchView.clearingall();

                binding.searchView.setVisibility(View.VISIBLE);

                byDeptList = new ArrayList<>(byDeptListSet);

                binding.searchView.setSuggestions(byDeptList);


                binding.tvDept.setBackgroundResource(R.drawable.inver_bt_background);
                binding.tvDept.setTextColor(getResources().getColor(R.color.white));

                binding.tvName.setBackgroundResource(R.drawable.bt_background);
                binding.tvName.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvLoc.setBackgroundResource(R.drawable.bt_background);
                binding.tvLoc.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvLoc.setBackgroundResource(R.drawable.bt_background);
                binding.tvLoc.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvDesgi.setBackgroundResource(R.drawable.bt_background);
                binding.tvDesgi.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.searchView.setHint("Search by Department/Ministry");
//                searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));


            }
        });
        binding.tvDesgi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchView.clearingall();

                binding.searchView.setVisibility(View.VISIBLE);

                byDesgList = new ArrayList<>(byDesgListSet);

                binding.searchView.setSuggestions(byDesgList);

                binding.tvDesgi.setBackgroundResource(R.drawable.inver_bt_background);
                binding.tvDesgi.setTextColor(getResources().getColor(R.color.white));


                binding.tvDept.setBackgroundResource(R.drawable.bt_background);
                binding.tvDept.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvName.setBackgroundResource(R.drawable.bt_background);
                binding.tvName.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvMblno.setBackgroundResource(R.drawable.bt_background);
                binding.tvMblno.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvLoc.setBackgroundResource(R.drawable.bt_background);
                binding.tvLoc.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.searchView.setHint("Search by Designation");
//                searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

            }
        });

        byNameList = new ArrayList<>(byNameListSet);

        binding.searchView.setSuggestions(byNameList);

        binding.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  name = false;
                //dept = false;
                //desg = true;
                //searchView.setVisibility(View.VISIBLE);

                binding.searchView.clearingall();

                byNameList = new ArrayList<>(byNameListSet);

                binding.searchView.setSuggestions(byNameList);


                binding.tvName.setBackgroundResource(R.drawable.inver_bt_background);
                binding.tvName.setTextColor(getResources().getColor(R.color.white));


                binding.tvDesgi.setBackgroundResource(R.drawable.bt_background);
                binding.tvDesgi.setTextColor(getResources().getColor(R.color.colorPrimary));


                binding.tvDept.setBackgroundResource(R.drawable.bt_background);
                binding.tvDept.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvMblno.setBackgroundResource(R.drawable.bt_background);
                binding.tvMblno.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.tvLoc.setBackgroundResource(R.drawable.bt_background);
                binding.tvLoc.setTextColor(getResources().getColor(R.color.colorPrimary));

                binding.searchView.setHint("Search By Name");
//                searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

            }
        });


        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalDeclaration.profileDetails != null) {
                    startActivity(new Intent(DashboardActivity.this, EditProfileActivity.class));
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
                    builder1.setMessage("Your Profile Details are missing ,Please contact admin");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                @SuppressLint("NewApi")
                                public void onClick(DialogInterface dialog, int id) {
                                    finishAffinity();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            }
        });
        binding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Snackbar.make(findViewById(R.id.container), "Query: " + query, Snackbar.LENGTH_LONG)
//                        .show();
//                Toast.makeText(Main2Activity.this,query,Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //  Toast.makeText(Main2Activity.this, newText, Toast.LENGTH_LONG).show();

                if (adapter != null) {
                    adapter.filter(newText);
                }
                return true;
            }
        });

        binding.searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                binding.searchView.setVisibility(View.VISIBLE);
                binding.filterLl.setVisibility(View.VISIBLE);
                binding.searchView.setSuggestions(null);
                binding.searchView.setHint("Search by Name");
                binding.tvLoc.setBackgroundResource(R.drawable.bt_background);
                binding.tvLoc.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.tvName.setBackgroundResource(R.drawable.inver_bt_background);
                binding.tvName.setTextColor(getResources().getColor(R.color.white));
                binding.tvDept.setBackgroundResource(R.drawable.bt_background);
                binding.tvDept.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.tvDesgi.setBackgroundResource(R.drawable.bt_background);
                binding.tvDesgi.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.tvMblno.setBackgroundResource(R.drawable.bt_background);
                binding.tvMblno.setTextColor(getResources().getColor(R.color.colorPrimary));


            }

            @Override
            public void onSearchViewClosed() {
                binding.searchView.setVisibility(View.GONE);
                binding.filterLl.setVisibility(View.GONE);
                binding.ll.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });
    }

    private void refreshDetails() {

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                members.clear();
                GlobalDeclaration.tempList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Details artist = postSnapshot.getValue(Details.class);
                    //adding artist to the list
                    members.add(artist);
                }

                for (int i = 0; i < members.size(); i++) {
                    // byCityList.add(members.get(i).getCity());
                    byCityListSet.add(members.get(i).getCity());
                    byNameListSet.add(members.get(i).getName());
                    byDeptListSet.add(members.get(i).getDepartment());
                    byDesgListSet.add(members.get(i).getDesignation());
                    byMblListSet.add(members.get(i).getMobileNo());
                    if (members.get(i).getMobileNo().trim().equalsIgnoreCase(mobile)) {
                        GlobalDeclaration.profileDetails = members.get(i);
                        GlobalDeclaration.id = i;
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

                //  GlobalDeclaration.tempList = members;

                typehere = members.get(GlobalDeclaration.id).getType();
                if (typehere != null) {
                    if (typehere != null) {
                        if (typehere.equalsIgnoreCase("admin")) {
                            binding.fabAdmin.setVisibility(View.VISIBLE);
                            binding.fabTextuser.setVisibility(View.GONE);
                        } else if (typehere.equalsIgnoreCase("user")) {
                            binding.fabAdmin.setVisibility(View.GONE);
                            binding.fabTextuser.setVisibility(View.VISIBLE);
                        } else {
                            binding.fabAdmin.setVisibility(View.GONE);
                            binding.fabTextuser.setVisibility(View.VISIBLE);

                        }
                    }
                }
                if (GlobalDeclaration.tempList != null) {
                    sortData(GlobalDeclaration.tempList);
                    binding.rvList.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                    adapter = new ListViewAdapter(DashboardActivity.this, GlobalDeclaration.tempList);
                    binding.rvList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    binding.tvNodata.setVisibility(View.VISIBLE);

                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        binding.searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
            builder1.setMessage("Do you want to Exit from App?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        @SuppressLint("NewApi")
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
    }

    private void sortData(List<Details> projectReportData) {
        Collections.sort(projectReportData, new Comparator<Details>() {
            public int compare(Details lhs, Details rhs) {
                return (lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase()));
            }
        });
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
