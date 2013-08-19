package com.liketivist.fithack;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class MapMyRunQuery {
   
   private String _jsonResponse;
   private String _result;
   
   public MapMyRunQuery() {
   }
   
   public void getRoute(float distance, float latitude, float longitude) {
      new MyHttpGet("http://api.mapmyfitness.com/3.1/geocode/get_location?&o=json&location=97015") {
         @Override
         public void onResponseReceived(String response) {
            // TODO Auto-generated method stub
            Log.d("FitHack", response);
            _jsonResponse = response;
            JSONObject json=null;
            try {
               json=new JSONObject(_jsonResponse);
               double la = json.getJSONObject("result").getJSONObject("output").getJSONObject("geocoded_address").getDouble("Latitude");
               double lo = json.getJSONObject("result").getJSONObject("output").getJSONObject("geocoded_address").getDouble("Longitude");
               onDone(String.format("%.2f,%.2f", la, lo));
               //Log.d("FitHack", String.format("%.2f,%.2f", la, lo));
            } catch (JSONException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }.execute();
   }
   
   public abstract void onDone(String response);
   
}
