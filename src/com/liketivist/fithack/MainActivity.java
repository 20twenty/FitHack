package com.liketivist.fithack;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements OnClickListener {

   private Button _theButton;
   private GoogleMap map;
   LocationManager _locationManager;
   double _latitude;
   double _longitude;
   boolean _ready;
   static private String _distance = "4.0";
   static private String _searchRadius = "2.0";
   private SharedPreferences _preferences;
   RelativeLayout _mainLayout;

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
      _mainLayout = (RelativeLayout) findViewById(R.id.start_layout);
      _theButton = (Button) this.findViewById(R.id.theButton);
      _theButton.setOnClickListener(this);
      //DISABLE THE BUTTON UNTIL THE LOCATION IS READY
      _theButton.setEnabled(false);
      
      //SUPPORT FOR HYPERLINK ON FRONT PAGE
      TextView textView =(TextView)findViewById(R.id.editText4);
      textView.setClickable(true);
      textView.setMovementMethod(LinkMovementMethod.getInstance());
      String text = "powered by <a href='https://pdxfithack.eventbrite.com/'>PDXFitHack</a>";
      textView.setText(Html.fromHtml(text));
      
      _preferences = PreferenceManager.getDefaultSharedPreferences(this);

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
   
   @Override
   public void onBackPressed() {
      if (_mainLayout.getVisibility() == View.GONE) {
         _mainLayout.setVisibility(View.VISIBLE);

      } else {
         finish();
      }
   }

   private void stopLocationRequests() {
      _locationManager.removeUpdates(_locationListener);
   }

   private void updateWithNewLocation(Location location) {
      if (location != null) {
         _latitude = location.getLatitude();
         _longitude = location.getLongitude();
         _ready = true;
         //ENABLE THE BUTTON NOW THAT THE LOCATION IS READY
         _theButton.setEnabled(true);
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }


   public void drawTheDataOnTheMap(LatLng CURRENT_LOCATION, ArrayList<RoutePoint> routePoints) {
	    
	    map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    //CLEAR THE MAP
	    map.clear();
	    
	 // Instantiates a new Polygon object and set options
	    PolylineOptions rectOptions = new PolylineOptions();
	    rectOptions.color(Color.BLUE);


	    //ADD A START MARKER
	    map.addMarker(new MarkerOptions()
        .position(new LatLng(routePoints.get(0).getLatitude(), routePoints.get(0).getLongitude()))
        .title("Start")
        //.snippet("Start")
        .draggable(false));

	    //ADD A STOP MARKER
	    map.addMarker(new MarkerOptions()
        .position(new LatLng(routePoints.get(routePoints.size()-1).getLatitude(), routePoints.get(routePoints.size()-1).getLongitude()))
        .title("Stop")
        //.snippet("Stop")
        .draggable(false));
	    
	    //ADD THE POINTS HERE
		for (int i = 0; i < routePoints.size(); i++) {
			final LatLng THIS_POINT = new LatLng(routePoints.get(i).getLatitude(), routePoints.get(i).getLongitude());
		    rectOptions.add(THIS_POINT);

		}
		
		// ATTACH THE POLYLINES TO THE MAP
		map.addPolyline(rectOptions);

   		//PUT A MARKER ON THE CURRENT LOCATION
		map.addMarker(new MarkerOptions()
			.position(CURRENT_LOCATION)
		   .title("Current Location")
	        .icon(BitmapDescriptorFactory
	        .fromResource(R.drawable.ic_launcher)));

	    // Move the camera instantly to THE CURRENT LOCATION with a zoom of 15.
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION, 15));

	    // Zoom in, animating the camera.
	    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

   }
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // TODO Auto-generated method stub
      switch (item.getItemId()) {
      case R.id.settings:
         Intent intent = new Intent(this, SettingsActivity.class);
         startActivity(intent);
         return true;
      case R.id.exit:
         finish();
         return true;
      default:
         return super.onOptionsItemSelected(item);
      }
   }
		   
   @Override
   public void onClick(View v) {
      _theButton.setEnabled(false);
      
      if (v == _theButton) {
         if(_ready) {
            final Context c = this;

            MapMyRunQuery mmrq = new MapMyRunQuery(this) {

               @Override
               public void onDone(Route route) {
            	   //TURN THE BUTTON BACK ON
                   _theButton.setEnabled(true);
                   
                  // Make call to maps API here
                  if(route == null) {
                     Toast.makeText(c, "Could not find any routes near you", Toast.LENGTH_LONG).show();
                     _theButton.setEnabled(true);
                  } else {
                     ArrayList<RoutePoint> routePoints = route.getRoutePoints();
                     Toast.makeText(c, route.getRouteOverview(), Toast.LENGTH_LONG).show();
                     _mainLayout.setVisibility(View.GONE);
                     final LatLng CURRENT_LOCATION = new LatLng(_latitude, _longitude);
                     drawTheDataOnTheMap(CURRENT_LOCATION, routePoints);
                  }
               }

            };

            mmrq.getRoute(Double.parseDouble(_preferences.getString("runningDistance", _distance)),
                  Double.parseDouble(_preferences.getString("searchRadius", _searchRadius)),
                  _latitude, _longitude);  

         } else {
            Toast.makeText(this, "Waiting for current location", Toast.LENGTH_LONG).show();
         }
      }
   }
   
   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      Log.d("FitHack", "onConfigurationChanged called");
   }
}
