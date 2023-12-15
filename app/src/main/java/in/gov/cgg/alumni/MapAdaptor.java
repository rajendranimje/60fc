package in.gov.cgg.alumni;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.activities.MapsActivity;

/**
 * Created by ranjani on 18-01-2018.
 */

public class MapAdaptor implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    public static final int LOCATION_REQUEST_CODE = 1;
    LocationManager locationManager;

    String url, name, dept, desg;


    public MapAdaptor(MapsActivity mapsActivity, String url, String name, String dept, String desg) {

        this.context = mapsActivity;
        this.url = url;
        this.name = name;
        this.dept = dept;
        this.desg = desg;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.map_adaptor, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView dept1 = view.findViewById(R.id.dept);
        TextView desg1 = view.findViewById(R.id.desg);

        ImageView img = view.findViewById(R.id.pic);


        name_tv.setText(name);
        dept1.setText(dept);
        desg1.setText(desg);

        if (!url.isEmpty()) {
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.loadingimg_red)
                            .error(R.drawable.user))
                    .load(url)
                    .into(img);

        } else {
            img.setImageDrawable(context.getResources().getDrawable(R.drawable.user));

        }

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
