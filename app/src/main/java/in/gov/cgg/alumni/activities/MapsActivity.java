package in.gov.cgg.alumni.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import in.gov.cgg.alumni.GlobalDeclaration;
import in.gov.cgg.alumni.MapAdaptor;
import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.utils.CheckInternet;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap, newMap;
    Address address;
    String location, name, desg, dept, url;
    int mid;
    List<Address> addressList;
    FusedLocationProviderClient mFusedLocationClient;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LatLng latLng1;
    LatLng latLng2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // mid = getIntent().getIntExtra("mid", 0);
        mid = GlobalDeclaration.position;
        Log.e("position", String.valueOf(mid));

//        location = GlobalDeclaration.tempList.get(mid).getOfficeAddress();
//        name = GlobalDeclaration.tempList.get(mid).getName();
//        desg = GlobalDeclaration.tempList.get(mid).getDesignation();
//        dept = GlobalDeclaration.tempList.get(mid).getDepartment();
//        url = GlobalDeclaration.tempList.get(mid).getProfilePicUrl();

        if (GlobalDeclaration.details != null) {
            location = GlobalDeclaration.details.getOfficeAddress();
            name = GlobalDeclaration.details.getName();
            desg = GlobalDeclaration.details.getDesignation();
            dept = GlobalDeclaration.details.getDepartment();
            url = GlobalDeclaration.details.getProfilePicUrl();
        }

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                if (CheckInternet.isOnline(MapsActivity.this)) {
                    addressList = geocoder.getFromLocationName(location, 1);
                    // Toast.makeText(this, "" + addressList, Toast.LENGTH_SHORT).show();
                    if (addressList != null && addressList.size() > 0) {
                        address = addressList.get(0);
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace();
//                Toast.makeText(this, "No location Found", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
                builder1.setMessage("No Location Found");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MapsActivity.this, DashboardActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        newMap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//            @Override
//            public void onMapLoaded() {
//                //LatLngBound will cover all your marker on Google Maps
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                builder.include(latLng1); //Taking Point A (First LatLng)
//                builder.include(latLng2); //Taking Point B (Second LatLng)
//                LatLngBounds bounds = builder.build();
//                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
//                mMap.moveCamera(cu);
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
//
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
                newMap.setMyLocationEnabled(true);
                try {
                    getLocation();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
            newMap.setMyLocationEnabled(true);
        }
    }

//    private void getLocation() {
//        if (address != null) {
//            latLng2 = new LatLng(address.getLatitude(), address.getLongitude());
//            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//                @Override
//                public View getInfoWindow(Marker marker) {
//                    LinearLayout info = new LinearLayout(MapsActivity.this);
//                    info.setOrientation(LinearLayout.VERTICAL);
//                    info.setPadding(10, 5, 10, 5);
//                    info.setBackgroundColor(Color.WHITE);
//                    TextView title = new TextView(MapsActivity.this);
//                    // title.setPadding(2,2,2,2);
//                    title.setTextColor(Color.BLUE);
//                    title.setTextSize(14);
//                    title.setGravity(Gravity.CENTER);
//                    title.setTypeface(null, Typeface.BOLD);
//                    title.setText(marker.getTitle());
//                    TextView snippet = new TextView(MapsActivity.this);
//                    snippet.setTextColor(Color.BLACK);
//                    snippet.setPadding(2, 2, 2, 2);
//                    snippet.setTextSize(12);
//                    snippet.setText(marker.getSnippet());
//                    info.addView(title);
//                    info.addView(snippet);
//                    return info;
//                }
//
//                @Override
//                public View getInfoContents(Marker marker) {
//                    return null;
//                }
//            });
//
//            // BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.current_position_tennis_ball)
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng2);
//            markerOptions.title(name).snippet(location);
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            mMap.addMarker(markerOptions);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 7));
//            // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        } else {
//            Toast.makeText(this, "No location Found", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void getLocation() {
        if (address != null) {
            latLng2 = new LatLng(address.getLatitude(), address.getLongitude());
            MarkerOptions markerOne = new MarkerOptions().position(latLng2);
            mMap.addMarker(markerOne);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 5));
            MapAdaptor customInfoWindow = new MapAdaptor(MapsActivity.this, url, name, dept, desg);
            mMap.setInfoWindowAdapter((GoogleMap.InfoWindowAdapter) customInfoWindow);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 15));


        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
            builder1.setMessage("No Location Found");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        @SuppressLint("NewApi")
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MapsActivity.this, DashboardActivity.class);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                //    Toast.makeText(CurrentLocation.this,"Latitude"+location.getLatitude()+"\n"+"Longitude"+location.getLongitude(),Toast.LENGTH_LONG).show();
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                // Place current location marker
//                latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng1);
//                markerOptions.title("My Location");
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//                mCurrLocationMarker = newMap.addMarker(markerOptions);
                //move map camera
                //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
            } else {

            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                        newMap.setMyLocationEnabled(true);
                        try {
                            getLocation();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

}
