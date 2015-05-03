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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
    private TextView distanceTextView;

    private boolean hasLoadedMap;
    private boolean hasInitialised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        hasLoadedMap = false;
        hasInitialised = false;

        refreshButton = (Button) findViewById(R.id.button);
        pbar = (ProgressBar) findViewById(R.id.progressBar);
        loadingView = (TextView) findViewById(R.id.textView2);
        distanceTextView = (TextView) findViewById(R.id.textView);

        pbar.setVisibility(View.VISIBLE);

        targetLocation = new Location("");
        targetLocation.setLongitude(153.023);
        targetLocation.setLatitude(-27.4709);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            LatLng loc = (LatLng) extras.get("location");
            targetLocation.setLatitude(loc.latitude);
            targetLocation.setLongitude(loc.longitude);
        }

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
                if (currentMarker != null){
                    // Update the user's position each time the location listener
                    // gets an update
                    currentMarker.setPosition(getLocationCoordinates(myLocation));
                    updateDistanceLabel(myLocation, targetLocation);
                }


                if (! hasInitialised) {
                    try {
                        mapFragment.getView().setVisibility(View.VISIBLE);
                        targetMarker = setupMarker(targetMarker, targetLocation, "Target");
                        currentMarker =  setupMarker(currentMarker, myLocation, "Current Location");
                        setupMap(gMap, currentMarker, targetMarker);
                        hasInitialised = true;
                        hasLoadedMap = true;
                        updateDistanceLabel(myLocation, targetLocation);
                    } catch (Exception e) {
                        Log.w("Error", "Could not obtain mapFragment view");
                    }
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

    /**
     * Updates the distance label
     * @param first First location to compare
     * @param second Second location to compare
     */
    private void updateDistanceLabel(Location first, Location second){
        float distance = first.distanceTo(second);
        distanceTextView.setText("Distance: " + distance + "m");
    }

    /**
     * Sets up the camera position of the google map object
     * @param map Google map to set camera of
     * @param current Current position of the person
     * @param target Target position of the library
     */
    private void setupMap(GoogleMap map, Marker current, Marker target){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(current.getPosition());
        builder.include(target.getPosition());

        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        map.animateCamera(cu);
    }

    /**
     * Creates a new marker on the map
     * @param m Marker to set
     * @param l Location of the target
     * @param title Title of the marker
     * @return The marker object
     */
    private Marker setupMarker(Marker m, Location l, String title){
        if (l != null && gMap != null){
            m = gMap.addMarker(new MarkerOptions()
                    .position(getLocationCoordinates(l))
                    .title(title));

            return m;
        } else {
            Log.w("ERROR", "Location is null");
            return null;
        }

    }

    /**
     * Gets a LatLng representation from a Location
     * @param l Location to get coords from
     * @return A new LatLng object
     */
    private LatLng getLocationCoordinates(Location l){
        return new LatLng(l.getLatitude(), l.getLongitude());
    }

    /**
     * Refresh the location
     * @param view
     */
    public void refreshLocation(View view){
        if (hasLoadedMap) {
            setupMap(gMap, currentMarker, targetMarker);
            updateDistanceLabel(myLocation, targetLocation);
        }
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
