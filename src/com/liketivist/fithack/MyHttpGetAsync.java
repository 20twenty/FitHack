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

public abstract class MyHttpGetAsync extends AsyncTask<Void, Void, String> {

   private String _url;

   public MyHttpGetAsync(String url) {
      _url = url;
   }

   @Override
   protected String doInBackground(Void... params) {
      return MyHttpGet.get(_url);
   }

   @Override
   protected void onPostExecute(String result) {
      onResponseReceived(result);
   }

   public abstract void onResponseReceived(String response);

}
