package com.liketivist.fithack;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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
	
   private final LocationListener locationListener = new LocationListener() {

      public void onLocationChanged(Location location) {
         updateWithNewLocation(location);
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
      setContentView(R.layout.activity_main);
      _theButton = (Button) this.findViewById(R.id.theButton);
      _theButton.setOnClickListener(this);

      LocationManager locManager;
      locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,
            500.0f, locationListener);
      Location location = locManager
            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
      if (location != null) {
         double latitude = location.getLatitude();
         double longitude = location.getLongitude();
      }
   }

   private void updateWithNewLocation(Location location) {
//      TextView myLocationText = (TextView) findViewById(R.id.text);
      String latLongString = "";
      if (location != null) {
         double lat = location.getLatitude();
         double lng = location.getLongitude();
         latLongString = "Lat:" + lat + "\nLong:" + lng;
      } else {
         latLongString = "No location found";
      }
      Toast.makeText(this, "Your Current Position is:\n" + latLongString, Toast.LENGTH_LONG);
//      myLocationText.setText("Your Current Position is:\n" + latLongString);
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
         // Toast.makeText(this, "You pressed THE BUTTON",
         // Toast.LENGTH_LONG).show();
         

         RelativeLayout start_layout = (RelativeLayout) findViewById(R.id.start_layout);
         start_layout.setVisibility(View.GONE);
         
         MapMyRunQuery mmrq = new MapMyRunQuery() {

            @Override
            public void onDone(ArrayList<RoutePoint> routePoints) {
               // Make call to maps API here
               Toast.makeText(c, String.format("route start: %.2f,%.2f", routePoints.get(0).getLatitude(), routePoints.get(0).getLongitude()), Toast.LENGTH_LONG).show();
            }

         };
         mmrq.getRoute(0.1f, 45.60f, -122.60f);
      }
   }

}
