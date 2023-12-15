package in.gov.cgg.alumni.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.databinding.ActivityNotifylistBinding;
import in.gov.cgg.alumni.utils.CheckInternet;

public class NotifyListActivity extends Activity {

    DatabaseReference databaseArtists;

    NotifyListAdapter adapter;
   // RecyclerView rv_list;
    //Button home;
    private ProgressDialog pd;
    private List<NotifyModel> notifylist;
ActivityNotifylistBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_notifylist);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifylist);
       /* binding.rvList = findViewById(R.id.rv_list);
        home = findViewById(R.id.btn_home);*/
        pd = new ProgressDialog(NotifyListActivity.this);
        pd.show();


        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotifyListActivity.this, DashboardActivity.class));
            }
        });


        databaseArtists = FirebaseDatabase.getInstance().getReference("notify_list");
        notifylist = new ArrayList<>();


        if (CheckInternet.isOnline(NotifyListActivity.this)) {
            try {

                databaseArtists.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //clearing the previous artist list
                        notifylist.clear();

                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting artist
                            NotifyModel artist = postSnapshot.getValue(NotifyModel.class);
                            //adding artist to the list
                            notifylist.add(artist);
                        }

                        Collections.reverse(notifylist);

                        GlobalDeclaration.notifyList = notifylist;


                        binding.rvList.setLayoutManager(new LinearLayoutManager(NotifyListActivity.this));
                        adapter = new NotifyListAdapter(NotifyListActivity.this, GlobalDeclaration.notifyList);
                        binding.rvList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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
            Toast.makeText(NotifyListActivity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();
            pd.dismiss();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NotifyListActivity.this, DashboardActivity.class));
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