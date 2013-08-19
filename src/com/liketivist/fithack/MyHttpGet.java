package com.liketivist.fithack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public abstract class MyHttpGet extends AsyncTask<Void, Void, String> {

   private String _url;

   public MyHttpGet(String url) {
      _url = url;
   }

   @Override
   protected String doInBackground(Void... params) {
      Log.d("FitHack", "Executing query: " + _url);
      String responseString = null;
      HttpClient httpclient = new DefaultHttpClient();

      // Prepare a request object
      HttpGet httpget = new HttpGet(_url);

      // Execute the request
      HttpResponse response;
      try {
         response = httpclient.execute(httpget);
         // Examine the response status
         Log.d("FitHack", response.getStatusLine().toString());

         // Get hold of the response entity
         HttpEntity entity = response.getEntity();
         // If the response does not enclose an entity, there is no need
         // to worry about connection release

         if (entity != null) {

            // A Simple JSON Response Read
            InputStream instream = entity.getContent();
            responseString = convertStreamToString(instream);
            // now you have the string representation of the HTML request
            instream.close();
         }

      } catch (Exception e) {
         Log.d("FitHack", "Exception: " + e.toString());
      }
      // TODO Auto-generated method stub
      return responseString;
   }

   @Override
   protected void onPostExecute(String result) {
//      Log.d("FitHack", result);
      onResponseReceived(result);
   }

   public abstract void onResponseReceived(String response);

   private static String convertStreamToString(InputStream is) {
      /*
       * To convert the InputStream to String we use the
       * BufferedReader.readLine() method. We iterate until the BufferedReader
       * return null which means there's no more data to read. Each line will
       * appended to a StringBuilder and returned as String.
       */
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();

      String line = null;
      try {
         while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            is.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return sb.toString();
   }
}
