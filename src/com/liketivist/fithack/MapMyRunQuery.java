package com.liketivist.fithack;

import java.io.StringReader;
import java.util.ArrayList;

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
//            Log.d("FitHack",response);
            boolean inCoordinates = false;
            boolean inRoute = false;
            String coordinatesString = null;
            final ArrayList<RoutePoint> routePoints = new ArrayList<RoutePoint>();
            
            try {
               XmlPullParser parser = Xml.newPullParser();
               parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//               parser.setInput(new ByteArrayInputStream(response.getBytes()), null);
               parser.setInput(new StringReader(response));
               
               int eventType = parser.getEventType();
               while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
//                    Log.d("FitHack","Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                   if(parser.getName().equals("coordinates")) inCoordinates = true;
                   if(parser.getName().equals("name")) {
                      parser.next();
                      if(parser.getText().equals("Route")) inRoute = true;
                   }
//                    Log.d("FitHack","Start tag: "+parser.getName());
                } else if(eventType == XmlPullParser.END_TAG) {
                   if(parser.getName().equals("coordinates")) {
                      inCoordinates = false;
                      inRoute = false;
                   }
//                    Log.d("FitHack","End tag: "+parser.getName());
                } else if(eventType == XmlPullParser.TEXT) {
                   if(inCoordinates && inRoute) {
                      coordinatesString = parser.getText();
//                      Log.d("FitHack","Text: "+coordinatesString);
                   }
                }
                eventType = parser.next();
               }
//               Log.d("FitHack","End document");
            } catch (Exception e) {
               
            }

            coordinatesString = coordinatesString.replaceAll("\n", "|").replaceAll("\\s+", "");
            String[] cArray = coordinatesString.split("\\|");
            for(String s : cArray) {
               String[] llArray = s.split(",");
//               Log.d("FitHack",s);
               if(llArray.length > 1) {
                  Log.d("FitHack", String.format("%s,%s", llArray[1], llArray[0]));
                  routePoints.add(new RoutePoint(llArray[1], llArray[0]));
               }
            }
            onDone(routePoints);
         }
         
      }.execute();
   }
   
   public abstract void onDone(ArrayList<RoutePoint> routePoints);
   
}
