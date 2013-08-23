package com.liketivist.fithack;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings);
   }
   
   @Override
   public void onStart() {
     super.onStart();
     EasyTracker.getInstance(this).activityStart(this);
   }

   @Override
   public void onStop() {
     super.onStop();
     EasyTracker.getInstance(this).activityStop(this);
   }
}
