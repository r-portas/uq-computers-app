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

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity{

    private MapFragment mapFragment;
    private GoogleMap gMap;

    private Location targetLocation;
    private Location myLocation;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        refreshButton = (Button) findViewById(R.id.button);

        targetLocation = new Location("");
        targetLocation.setLongitude(153.023);
        targetLocation.setLatitude(-27.4709);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        myLocation = new Location("");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.w("Location", "Location Updated: " + location.getLatitude() + " " + location.getLongitude());
                myLocation.setLatitude(location.getLatitude());
                myLocation.setLongitude(location.getLongitude());
                myLocation.setSpeed(location.getSpeed());
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
        gMap = mapFragment.getMap();

        // Set markers
        gMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        myLocation.getLatitude(),
                        myLocation.getLongitude()))
                .title("Current Position"));

    }

    /**
     * Refresh the location
     * @param view
     */
    public void refreshLocation(View view){
        gMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        myLocation.getLatitude(),
                        myLocation.getLongitude()))
                .title("Current Position"));

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
