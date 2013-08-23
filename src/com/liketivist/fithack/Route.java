package com.liketivist.fithack;

import java.util.ArrayList;

public class Route {

   ArrayList<RoutePoint> _routePoints = new ArrayList<RoutePoint>();
   String _routeKey;
   double _routeDistance;
   double _startDistance = 0;
   
   public Route(double routeDistance, String routeKey) {
      _routeDistance = routeDistance;
      _routeKey = routeKey;
   }
   
   public String getRouteKey() {
      return _routeKey;
   }
   
   public void addRoutePoint(String latitude, String longitude) {
      _routePoints.add(new RoutePoint(latitude, longitude));
   }
   
   public String getRouteOverview(double lat, double lng) {
      return String.format("Found a %.2fmi route %.2fmi away", _routeDistance, getStartDistance(lat,lng));
   }
   
   public ArrayList<RoutePoint> getRoutePoints() {
      return _routePoints;
   }
   
   public double getStartDistance(double lat_phone, double lng_phone) {
      double R = 3961.3; // earth radius in miles
      double dLat = Math.toRadians(_routePoints.get(0).getLatitude()-lat_phone);
      double dLon = Math.toRadians(_routePoints.get(0).getLongitude()-lng_phone);
      double lat1 = Math.toRadians(lat_phone);
      double lat2 = Math.toRadians(_routePoints.get(0).getLatitude());

      double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
      double d = R * c;
      return d;
   }
}
