package com.bloo.kenjc;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class EventMapDisplay extends FragmentActivity {

	private GoogleMap mGoogleMap;;
	MarkerOptions mMarker = new MarkerOptions();
	android.app.Fragment eventmapview;
	SharedPreferences preferences;
	String Latitude, Longitude;
	String str = "";
	private MarkerOptions markeropt;

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		EventMapDisplay.this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventmap);

		String str = (getIntent().getExtras().getString("EvtName") + "\n"
				+ getIntent().getExtras().getString("EvtDate") + "\n" + getIntent()
				.getExtras().getString("EvtTime"));

		try {
			SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			mGoogleMap = supportMapFragment.getMap();

			if (mGoogleMap != null) {

				mGoogleMap.setMyLocationEnabled(true);
				mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
				mGoogleMap.getUiSettings().setCompassEnabled(true);
				mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
				mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
				mGoogleMap.setTrafficEnabled(true);
				mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(Double.parseDouble(getIntent().getExtras()
								.getString("eventLat")), Double
								.parseDouble(getIntent().getExtras().getString(
										"eventLong"))), 8));

				markeropt = new MarkerOptions().position(new LatLng(Double
						.parseDouble(getIntent().getExtras().getString(
								"eventLat")), Double.parseDouble(getIntent()
						.getExtras().getString("eventLong"))));
				markeropt.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
				markeropt.title(str);

				mGoogleMap.setMyLocationEnabled(true);
				mGoogleMap.addMarker(markeropt);
			} else {
				Toast.makeText(getApplicationContext(),
						"Map cant be displayed. ", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
