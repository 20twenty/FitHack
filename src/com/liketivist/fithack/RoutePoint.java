package com.liketivist.fithack;

public class RoutePoint {
   private float _latitude;
   private float _longitude;
   
   public RoutePoint(float latitude, float longitude) {
      _latitude = latitude;
      _longitude = longitude;
   }
   
   public RoutePoint(String latitude, String longitude) {
      _latitude = Float.parseFloat(latitude);
      _longitude = Float.parseFloat(longitude);
   }
   
   public float getLatitude() {
      return _latitude;
   }

   public float getLongitude() {
      return _longitude;
   }
   
}
