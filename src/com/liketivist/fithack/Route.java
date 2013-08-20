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
   
   public String getRouteOverview() {
      return String.format("Found a %.2fmi route %.2fmi away", _routeDistance, _startDistance);
   }
   
   public ArrayList<RoutePoint> getRoutePoints() {
      return _routePoints;
   }
}
