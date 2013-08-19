package com.liketivist.fithack;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

public abstract class MapMyRunQuery {
   
   private String _jsonResponse;
   private String _result;
   
   public MapMyRunQuery() {
   }
   
   public void getRoute(float distance, float latitude, float longitude) {
//      new MyHttpGet("http://api.mapmyfitness.com/3.1/geocode/get_location?&o=json&location=97015") {
//         @Override
//         public void onResponseReceived(String response) {
//            // TODO Auto-generated method stub
//            Log.d("FitHack", response);
//            _jsonResponse = response;
//            JSONObject json=null;
//            try {
//               json=new JSONObject(_jsonResponse);
//               double la = json.getJSONObject("result").getJSONObject("output").getJSONObject("geocoded_address").getDouble("Latitude");
//               double lo = json.getJSONObject("result").getJSONObject("output").getJSONObject("geocoded_address").getDouble("Longitude");
//               onDone(String.format("%.2f,%.2f", la, lo));
//               //Log.d("FitHack", String.format("%.2f,%.2f", la, lo));
//            } catch (JSONException e) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//            }
//         }
//      }.execute();
//      
      final double distanceFinal = (double)distance;
      new MyHttpGet("http://api.mapmyfitness.com/3.1/routes/search_routes?o=json&center_longitude="+longitude+"&center_latitude="+latitude+"&radius=1&route_type_id=1&start_record=0&limit=100&sort_by=total_distance") {

         @Override
         public void onResponseReceived(String response) {
            // TODO Auto-generated method stub
            //result.output.routes[index].total_distance
            try {
               JSONObject json = new JSONObject(response);
               JSONArray routes = json.getJSONObject("result").getJSONObject("output").getJSONArray("routes");
               for (int i = 0; i < routes.length(); i++) {
                  double d = routes.getJSONObject(i).getDouble("total_distance");
                  String route_key = routes.getJSONObject(i).getString("route_key");
                  if (d > distanceFinal) {
                     getRouteByKey(route_key);
                     break;
                  }
                  Log.d("FitHack", String.format("distance: %.2f", d));
               }
            } catch (JSONException e) {
               e.printStackTrace();
            }
         }
         
      }.execute();
   }
   
   private void getRouteByKey(String route_key) {
      new MyHttpGet("http://www.mapmyrun.com/kml?r="+route_key) {

         @Override
         public void onResponseReceived(String response) {
            // TODO Auto-generated method stub
            Log.d("FitHack",response);
            try {
               XmlPullParser parser = Xml.newPullParser();
               parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
               parser.setInput(new ByteArrayInputStream(response.getBytes()), null);
               while (parser.next() != XmlPullParser.END_TAG) {
                  Log.d("FitHack", "parser: " + parser.getName());
               }
            } catch (Exception e) {
               
            }
         }
         
      }.execute();
   }
   
   public abstract void onDone(String response);
   
}
