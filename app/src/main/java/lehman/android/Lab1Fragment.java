package lehman.android;

import android.app.Fragment;
import android.content.Intent;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.widget.EditText;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.content.IntentFilter;

public class Lab1Fragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

    public final String COORD_PATTERN = "([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)";

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private EditText mLatLon;
    private String mLastLocation;
    private StreetViewReceiver mReceiver;

    public Lab1Fragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (D) { Log.d(TAG, "Starting onCreateView");}
        View rootView = inflater.inflate(R.layout.fragment_lab1, container, false);
        mLatLon = (EditText)rootView.findViewById(R.id.latLon);

        Button loc = (Button)rootView.findViewById(R.id.getLocation);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

                if ( ((Button)v).getText().toString().equals(getResources().getString(R.string.get_location)) ) {
                    mLocationListener = new LocationListener() {
                        public void onLocationChanged(Location location) {
                            Log.d(TAG, "onLocationChanged");
                            CharSequence lanlon = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
                            mLatLon.setText(lanlon);
                        }

                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.d(TAG, "onStatusChanged");
                        }

                        public void onProviderEnabled(String provider) {
                            Log.d(TAG, "onProviderEnabled");
                        }

                        public void onProviderDisabled(String provider) {
                            Log.d(TAG, "onProviderDisabled");
                        }
                    };
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                    ((Button)v).setText(R.string.stop_gps);
                } else {
                    mLocationManager.removeUpdates(mLocationListener);
                    ((Button)v).setText(R.string.get_location);
                }
            }
        });

        Button start = (Button)rootView.findViewById(R.id.startService);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//here!
                String latlon = mLatLon.getText().toString();
                if (validateLatLon(latlon)) {
                    Intent intent = new Intent(getActivity(), StreetViewService.class);
                    intent.putExtra(StreetViewService.LAT_LON, latlon);
                    getActivity().startService(intent);
                    mLastLocation = latlon;
                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "coordinates not valid",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Button stop = (Button)rootView.findViewById(R.id.stopService);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getActivity(),
                        "Service Stopped",
                        Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getActivity(), StreetViewService.class);
                getActivity().stopService(intent);
            }
        });

        IntentFilter filter = new IntentFilter(StreetViewReceiver.ACTION_STREETVIEW);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new StreetViewReceiver(rootView);
        getActivity().registerReceiver(mReceiver, filter);

        if (D) { Log.d(TAG, "onCreateView completed");}
        return rootView;
    }

    private boolean validateLatLon(String latlon) {
        Pattern pattern = Pattern.compile(COORD_PATTERN);
        Matcher matcher = pattern.matcher(latlon);
        return matcher.matches();
    }
}