package com.liketivist.fithack;

import android.util.Log;

public class MapMyRunQuery {
   
   private String _jsonResponse;
   
   public void getRoute(float distance, float latitude, float longitude) {
      new MyHttpGet("http://api.mapmyfitness.com/3.1/geocode/get_location?&o=json&location=97015") {
         @Override
         public void onResponseReceived(String response) {
            // TODO Auto-generated method stub
            Log.d("FitHack", response);
            _jsonResponse = response;
         }
      }.execute();
   }
   
   
}
