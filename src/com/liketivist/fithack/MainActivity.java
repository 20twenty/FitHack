package com.liketivist.fithack;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

   private Button _theButton;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      _theButton = (Button)this.findViewById(R.id.theButton);
      _theButton.setOnClickListener(this);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

   @Override
   public void onClick(View v) {
      if ( v == _theButton ) {
         Toast.makeText(this, "You pressed THE BUTTON", Toast.LENGTH_LONG).show();
         MapMyRunQuery mmrq = new MapMyRunQuery();
         mmrq.getRoute(0.1f, 45.06f, -122.32f);
      }
   }

}
