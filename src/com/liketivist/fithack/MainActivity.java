package com.liketivist.fithack;

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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

   private Button _theButton;

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

   @Override
   public void onClick(View v) {
      if (v == _theButton) {
         final Context c = this;
         // Toast.makeText(this, "You pressed THE BUTTON",
         // Toast.LENGTH_LONG).show();
         MapMyRunQuery mmrq = new MapMyRunQuery() {

            @Override
            public void onDone(String response) {
               Toast.makeText(c, response, Toast.LENGTH_LONG).show();
            }

         };
         mmrq.getRoute(0.1f, 45.60f, -122.60f);
      }
   }

}
