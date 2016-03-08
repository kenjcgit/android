package com.bloo.kenjc;

import java.util.ArrayList;
import java.util.HashMap;

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

public class ReminderDisplayActivity extends Activity implements
		OnClickListener {
	private ImageView backbtn;
	private ListView reminderlist;
	MyAdapter myadapter;
	ArrayList<HashMap<String, String>> reminderdata = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminderlist);
		init();

	}

	private void init() {
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		backbtn.setVisibility(View.VISIBLE);
		reminderlist = (ListView) findViewById(R.id.lv_eventlist);

		reminderdata = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra("nwevent");
		if (reminderdata.size() > 0) {
			myadapter = new MyAdapter(ReminderDisplayActivity.this,
					reminderdata);
			reminderlist.setAdapter(myadapter);
		} else {
			Toast.makeText(ReminderDisplayActivity.this, "No Records Found.",
					Toast.LENGTH_SHORT).show();

		}

		backbtn.setOnClickListener(this);
	}

	public class MyAdapter extends BaseAdapter {

		Activity activity;
		ArrayList<HashMap<String, String>> arrayactivity;

		public MyAdapter(ReminderDisplayActivity activity,
				ArrayList<HashMap<String, String>> reminderdata) {
			this.activity = activity;
			this.arrayactivity = reminderdata;
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
							.get("NwEvtName").length() > 0
					&& !((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtName").equalsIgnoreCase("null")) {
				data.setVisibility(View.VISIBLE);
				data.setText(((HashMap<String, String>) arrayactivity
						.get(position)).get("NwEvtName"));
			} else {
				data.setVisibility(View.GONE);
				data.setText("");
			}

			if (((HashMap<String, String>) arrayactivity.get(position))
					.get("NwEvtSenddate") != null
					&& ((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtSenddate").length() > 0
					&& !((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtSenddate").equalsIgnoreCase("null")) {
				data1.setVisibility(View.VISIBLE);
				data1.setText(((HashMap<String, String>) arrayactivity
						.get(position)).get("NwEvtSenddate"));

			} else {
				data1.setVisibility(View.GONE);
				data1.setText("");
			}
			return v;
		}

	}

	@Override
	public void onClick(View v) {
		if (v == backbtn) {
			finish();

		}
	}

}
