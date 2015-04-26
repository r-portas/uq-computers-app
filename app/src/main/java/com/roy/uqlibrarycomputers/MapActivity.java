package com.roy.uqlibrarycomputers;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class MapActivity extends Activity{

    private MapFragment mapFragment;
    private GoogleMap gMap;

    private Location targetLocation;
    private Location myLocation;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Marker currentMarker;
    private Marker targetMarker;

    private Button refreshButton;
    private ProgressBar pbar;
    private TextView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        refreshButton = (Button) findViewById(R.id.button);
        pbar = (ProgressBar) findViewById(R.id.progressBar);
        loadingView = (TextView) findViewById(R.id.textView2);

        pbar.setVisibility(View.VISIBLE);

        targetLocation = new Location("");
        targetLocation.setLongitude(153.023);
        targetLocation.setLatitude(-27.4709);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        myLocation = new Location("");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.w("Location", "Location Updated: " + location.getLatitude() + " " + location.getLongitude());
                myLocation.setLatitude(location.getLatitude());
                myLocation.setLongitude(location.getLongitude());
                myLocation.setSpeed(location.getSpeed());

                pbar.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);

                targetMarker = setupMarker(targetMarker, targetLocation, "Target");
                currentMarker =  setupMarker(currentMarker, myLocation, "Current Location");
                try {
                    mapFragment.getView().setVisibility(View.VISIBLE);
                } catch (Exception e){
                    Log.w("Error", "Could not obtain mapFragment view");
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getView().setVisibility(View.INVISIBLE);
        gMap = mapFragment.getMap();
    }

    public Marker setupMarker(Marker m, Location l, String title){
        if (l != null && gMap != null){
            m = gMap.addMarker(new MarkerOptions()
                    .position(new LatLng(l.getLatitude(), l.getLongitude()))
                    .title(title));

            return m;
        } else {
            Log.w("ERROR", "Location is null");
            return null;
        }

    }

    /**
     * Refresh the location
     * @param view
     */
    public void refreshLocation(View view){
        //TODO: Remove does not work as expected
        targetMarker.setPosition(new LatLng(0, 0));
        Log.w("Location", myLocation.getLatitude() + " " + myLocation.getLongitude());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
