package in.gov.cgg.alumni.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import in.gov.cgg.alumni.Details;
import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.activities.ListViewAdapter;

public class ListInfoFragment extends androidx.fragment.app.Fragment {
    RecyclerView rv_list;
    ListViewAdapter adapter;
    DatabaseReference databaseArtists;
    List<Details> members, tempList;
    Set<String> byNameListSet, byDesgListSet, byDeptListSet, byCityListSet;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SearchView searchView;
    List<String> byNameList, byDesgList, byDeptList, byCityList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true); // Add this! (as above)
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_list = view.findViewById(R.id.rv_list);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        databaseArtists = FirebaseDatabase.getInstance().getReference("member");
        members = new ArrayList<>();
        byCityListSet = new LinkedHashSet<>();
        byDesgListSet = new LinkedHashSet<>();
        byDeptListSet = new LinkedHashSet<>();
        byNameListSet = new LinkedHashSet<>();
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

                for (int i = 0; i < members.size(); i++) {

                    // byCityList.add(members.get(i).getCity());
                    byCityListSet.add(members.get(i).getCity());
                    byNameListSet.add(members.get(i).getName());
                    byDeptListSet.add(members.get(i).getDepartment());
                    byDesgListSet.add(members.get(i).getDesignation());
                }

                GlobalDeclaration.tempList = members;

                rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new ListViewAdapter(getActivity(), GlobalDeclaration.tempList);
                byCityList = new ArrayList<>(byCityListSet);
//                searchView.setSuggestions(byCityList);
                rv_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                mSwipeRefreshLayout.setRefreshing(false);
                if (members != null) {
                    try {
                        refreshDetails();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    private void refreshDetails() {

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

                for (int i = 0; i < members.size(); i++) {
                    // byCityList.add(members.get(i).getCity());
                    byCityListSet.add(members.get(i).getCity());
                    byNameListSet.add(members.get(i).getName());
                    byDeptListSet.add(members.get(i).getDepartment());
                    byDesgListSet.add(members.get(i).getDesignation());
                }

                GlobalDeclaration.tempList = members;

                rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new ListViewAdapter(getActivity(), GlobalDeclaration.tempList);
                rv_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Here is where we are going to implement the filter logic
                //Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
                if (adapter != null) {
                    adapter.filter(newText);
                }
                return true;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_search:
//                ((AdminTesttAc)getActivity()).changeLook();
                return true;*/
            default:
                return super.onContextItemSelected(item);
        }
    }
}
