package com.bloo.kenjc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.google.android.gcm.GCMRegistrar;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if(IsNetworkConnection.checkNetworkConnection(SplashActivity.this))
		{
		
		RegisterWithGCM();
		}
		else
		{
			Toast.makeText(SplashActivity.this, "No Network", 2000).show();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Intent intent = new Intent(SplashActivity.this,EventsActivity.class);
							startActivity(intent);
							finish();
						}
					});
				} catch (Exception e) {
				}
			}
		}).start();

	}
	
	
	
	@SuppressWarnings("deprecation")
	 private void RegisterWithGCM() {

	  GCMRegistrar.checkDevice(this);
	  GCMRegistrar.checkManifest(this);
	  final String regId = GCMRegistrar.getRegistrationId(this);
	  if (regId.equals("")) {
	   GCMRegistrar.register(this, Constants.SENDER_ID); 
	   Constants.DEVICE_TOKEN = regId;  

	  } else {
	   Constants.DEVICE_TOKEN = regId;
	  }
	 }
	
}



