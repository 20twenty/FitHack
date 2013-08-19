package com.liketivist.fithack;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnClickListener {

   private Button _theButton;
   static final LatLng HAMBURG = new LatLng(53.558, 9.927);
   static final LatLng KIEL = new LatLng(53.551, 9.993);
   private GoogleMap map;
   LocationManager _locationManager;
   double _latitude;
   double _longitude;
   boolean _ready;
   

   private final LocationListener _locationListener = new LocationListener() {

      public void onLocationChanged(Location location) {
         updateWithNewLocation(location);
         Log.d("FitHack",String.format("onLocationChanged: %f,%f", location.getLatitude(), location.getLongitude()));
         Log.d("FitHack",String.format("Accuracy: %f", location.getAccuracy()));
         stopLocationRequests();
      }

      public void onProviderDisabled(String provider) {
         updateWithNewLocation(null);
      }

      public void onProviderEnabled(String provider) {
      }

      public void onStatusChanged(String provider, int status, Bundle extras) {
      }
   };

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.activity_main);
      _theButton = (Button) this.findViewById(R.id.theButton);
      _theButton.setOnClickListener(this);

      _ready = false;
      
      _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      if(_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
         _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, _locationListener);
      } else if(_locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
         _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 500.0f, _locationListener);
      } else {
         Intent gpsOptionsIntent = new Intent(  
               android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
           startActivity(gpsOptionsIntent);
      }
      
//      Location lastLocationGPS = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//      if (lastLocationGPS != null) {
//         double latitude = lastLocationGPS.getLatitude();
//         double longitude = lastLocationGPS.getLongitude();
//         Log.d("FitHack",String.format("onCreate GPSLastLocation: %f,%f", lastLocationGPS.getLatitude(), lastLocationGPS.getLongitude()));
//      }
//      
//      Location lastLocationNetwork = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//      if (lastLocationNetwork != null) {
//         double latitude = lastLocationNetwork.getLatitude();
//         double longitude = lastLocationNetwork.getLongitude();
//         Log.d("FitHack",String.format("onCreate networkLastLocation: %f,%f", lastLocationNetwork.getLatitude(), lastLocationNetwork.getLongitude()));
//      }
      
   }
   
   private void stopLocationRequests() {
      _locationManager.removeUpdates(_locationListener);
   }

   private void updateWithNewLocation(Location location) {
      if (location != null) {
         _latitude = location.getLatitude();
         _longitude = location.getLongitude();
         _ready = true;
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

   @SuppressLint("NewApi")
   public void doTheMapYo() {
	    setContentView(R.layout.activity_main);
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	        .getMap();
	    Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
	        .title("Hamburg"));
	    Marker kiel = map.addMarker(new MarkerOptions()
	        .position(KIEL)
	        .title("Kiel")
	        .snippet("Kiel is cool")
	        .icon(BitmapDescriptorFactory
	            .fromResource(R.drawable.ic_launcher)));

	    // Move the camera instantly to hamburg with a zoom of 15.
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

	    // Zoom in, animating the camera.
	    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

   }
   
   @Override
   public void onClick(View v) {
      if (v == _theButton) {
         final Context c = this;
         if(_ready) {
            RelativeLayout start_layout = (RelativeLayout) findViewById(R.id.start_layout);
            start_layout.setVisibility(View.GONE);

            MapMyRunQuery mmrq = new MapMyRunQuery() {

               @Override
               public void onDone(ArrayList<RoutePoint> routePoints) {
                  // Make call to maps API here
                  Toast.makeText(
                        c,
                        String.format("route start: %.2f,%.2f", routePoints.get(0).getLatitude(), routePoints.get(0)
                              .getLongitude()), Toast.LENGTH_LONG).show();
               }

            };
            mmrq.getRoute(1.0, _latitude, _longitude);
         } else {
            Toast.makeText(c, "Waiting for current location", Toast.LENGTH_LONG).show();
         }
      }
   }

}
