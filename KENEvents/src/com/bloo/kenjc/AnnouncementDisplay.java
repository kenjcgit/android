package com.bloo.kenjc;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloo.kenjc.R;

@SuppressLint("ViewHolder")
public class AnnouncementDisplay extends Activity implements OnClickListener {
	private ImageView backbtn;
	private ListView announcementlist;
	MyAdapter myadapter;
	ArrayList<HashMap<String, String>> announcementdata = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcementlist);
		init();

	}

	@SuppressWarnings("unchecked")
	private void init() {
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		backbtn.setVisibility(View.VISIBLE);
		announcementlist = (ListView) findViewById(R.id.lv_eventlist);

		announcementdata = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra("nwevent");

		if (announcementdata.size() > 0) {
			myadapter = new MyAdapter(AnnouncementDisplay.this,
					announcementdata);
			announcementlist.setAdapter(myadapter);
		} else {
			Toast.makeText(AnnouncementDisplay.this, "No Records Found.",
					Toast.LENGTH_SHORT).show();

		}

		backbtn.setOnClickListener(this);
	}

	public class MyAdapter extends BaseAdapter {

		Activity activity;
		ArrayList<HashMap<String, String>> arrayactivity;

		public MyAdapter(AnnouncementDisplay activity,
				ArrayList<HashMap<String, String>> announcementdata) {
			this.activity = activity;
			this.arrayactivity = announcementdata;
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
			v = infaltor.inflate(R.layout.reminderlisting, null);
			final LinearLayout mainll = (LinearLayout) v
					.findViewById(R.id.main_ll);
			TextView data = (TextView) v.findViewById(R.id.tv_name);
			TextView data1 = (TextView) v.findViewById(R.id.tv_date);

			if (((HashMap<String, String>) arrayactivity.get(position))
					.get("NwEvtName") != null
					&& ((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtName").length() > 0) {
				data.setText(((HashMap<String, String>) arrayactivity
						.get(position)).get("NwEvtName"));

			} else {
				data.setText("");

			}

			if (((HashMap<String, String>) arrayactivity.get(position))
					.get("NwEvtSenddate") != null
					&& ((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtSenddate").length() > 0) {
				data1.setText(((HashMap<String, String>) arrayactivity
						.get(position)).get("NwEvtSenddate"));
			

			} else {
				data1.setText("");
			}

			return v;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backbtn) {
			finish();

		}
	}

}
