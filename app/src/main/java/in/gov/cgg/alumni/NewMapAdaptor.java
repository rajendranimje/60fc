package in.gov.cgg.alumni;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import in.gov.cgg.alumni.activities.MapsActivity;

/**
 * Created by ranjani on 18-01-2018.
 */

public class NewMapAdaptor implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    public static final int LOCATION_REQUEST_CODE = 1;
    LocationManager locationManager;

    String url, name, dept, desg;


    public NewMapAdaptor(MapsActivity mapsActivity) {

        this.context = mapsActivity;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.newmap_adaptor, null);

        TextView name_tv = view.findViewById(R.id.name);



        name_tv.setText(name);



        // InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

       /* for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getLat().equals("null") && !list.get(i).getLon().equals("null")) {
                Logger.e("gmap_latlong", list.get(i).getLat() + "::::::::::" + list.get(i).getLon());

                latLng = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLon()));
                mMap.addMarker(new MarkerOptions().position(latLng).title("" + list.get(i).getWin_code()));

            }

        }*/


        //  img.setImageResource(R.drawable.turn_right_sign);

        return view;
    }

}
