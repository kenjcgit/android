package com.bloo.kenjc;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.fipl.kenjc.Utils.post_async;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class ContactsActivity extends FragmentActivity implements
		OnClickListener {

	ListView contactlist;
	ImageView contactselector;
	ImageButton More, Event, Notification;
	MyAdapter myadapter;
	ArrayList<HashMap<String, String>> activitylist = new ArrayList<HashMap<String, String>>();
	ListView list;
	HashMap<String, String> map;
	HashMap<String, String> data = new HashMap<String, String>();
	Editor editor;
	Display display;
	int height, width;
	android.app.Fragment mapview;
	private GoogleMap mGoogleMap;
	MarkerOptions mMarker = new MarkerOptions();
	SharedPreferences preferences;
	String Latitude, Longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactdetailtab);
		display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();
		preferences = getSharedPreferences(Constants.PREF, 0);
		editor = preferences.edit();
		init();
		getcontact();

	}

	private void init() {

		contactlist = (ListView) findViewById(R.id.lv_contactnameno);
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

			} else {
				Toast.makeText(getApplicationContext(),
						"Map cant be displayed. ", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class MyAdapter extends BaseAdapter {

		Activity activity;
		ArrayList<HashMap<String, String>> arrayactivity;

		public MyAdapter(Activity activity,
				ArrayList<HashMap<String, String>> activitylist1) {
			this.activity = activity;
			this.arrayactivity = activitylist1;

		}

		@Override
		public int getCount() {
			return arrayactivity.size();

		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = convertView;
			LayoutInflater infaltor = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = infaltor.inflate(R.layout.customelist_contact, null);
			LinearLayout mail_ll = (LinearLayout) v
					.findViewById(R.id.contactslist_mail);
			LinearLayout no_ll = (LinearLayout) v
					.findViewById(R.id.contactslist_no);
			final ImageView nodial = (ImageView) v.findViewById(R.id.iv_phone);
			final ImageView mailcomposs = (ImageView) v
					.findViewById(R.id.iv_mail);
			final TextView name = (TextView) v
					.findViewById(R.id.tv_contactname);
			final TextView mail = (TextView) v
					.findViewById(R.id.tv_contactmail);
			final TextView no = (TextView) v.findViewById(R.id.tv_contactno);
			name.setText(arrayactivity.get(position).get("Name"));
			no.setText(arrayactivity.get(position).get("No"));
			mail.setText(arrayactivity.get(position).get("Mail"));
			mail_ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent email = new Intent(Intent.ACTION_SEND);
					email.putExtra(Intent.EXTRA_EMAIL, ""
							+ mail.getText().toString());
					email.putExtra(Intent.EXTRA_SUBJECT, ""
							+ name.getText().toString());
					email.putExtra(Intent.EXTRA_TEXT, "");
					email.setType("message/rfc822");
					startActivity(Intent.createChooser(email,
							"Choose an Email client :"));
				}
			});
			no_ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						String uri = "tel:" + no.getText().toString();
						Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri
								.parse(uri));

						startActivity(dialIntent);

					} catch (Exception e) {

						Toast.makeText(getApplicationContext(),
								"Your call has failed...", Toast.LENGTH_LONG)
								.show();
						e.printStackTrace();

					}

				}

			});

			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			return v;
		}

	}

	@Override
	public void onClick(View v) {

	}

	public void getcontact() {
		if (IsNetworkConnection.checkNetworkConnection(ContactsActivity.this)) {

			String url = Constants.SERVER_URL + "action=GetContacts";
			new post_async(ContactsActivity.this, "GetContacts").execute(url);
		} else {
			Toast.makeText(ContactsActivity.this, "No Network", 2000).show();
		}
	}

	public void responseofgetcontact(String resultString) {

		try {

			JSONArray jarray = new JSONArray(resultString);
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jobj = jarray.getJSONObject(i);
				map = new HashMap<String, String>();
				map.put("No", jobj.getString("cnPhone"));
				map.put("Mail", jobj.getString("cnEmail"));
				map.put("Name", jobj.getString("cnName"));
				map.put("Lat", jobj.getString("cnLatitude"));
				editor.putString("ContctLat", jobj.getString("cnLatitude"));
				map.put("Long", jobj.getString("cnLongitude"));
				editor.putString("ContctLong", jobj.getString("cnLongitude"));
				activitylist.add(map);
			}

			myadapter = new MyAdapter(this, activitylist);
			contactlist.setAdapter(myadapter);

			if (activitylist != null) {

				for (int i = 0; i < activitylist.size(); i++) {

					mMarker.position(
							new LatLng(Double.parseDouble(activitylist.get(i)
									.get("Lat")), Double
									.parseDouble(activitylist.get(i)
											.get("Long"))))
							.title("Position " + 1)
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
					mGoogleMap.addMarker(mMarker);
					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(Double.parseDouble(activitylist.get(i)
									.get("Lat")), Double
									.parseDouble(activitylist.get(i)
											.get("Long"))), 11));

				}
			}

		} catch (Exception e) {
		}

	}
}
