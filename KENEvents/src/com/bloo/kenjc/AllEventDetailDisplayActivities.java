package com.bloo.kenjc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
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

import com.fipl.kenjc.Utils.Constants;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class AllEventDetailDisplayActivities extends Activity implements
		OnClickListener {

	ListView eventlist;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private int[] colors;
	private int[] images;
	MyAdapter myadapter;
	private ImageView backbtn;
	String eventname = "";
	private TextView tv_nweventtitle;
	ArrayList<HashMap<String, String>> eventdata = new ArrayList<HashMap<String, String>>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alleventdetaillayout);
		init();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.app_icon_android)
				.showImageOnFail(R.drawable.app_icon_android)
				.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoader.getInstance().init(config);

		System.gc();
		Runtime.getRuntime().gc();

		colors = new int[] { Color.parseColor("#3DBFD9"),
				Color.parseColor("#5EC7A1"), Color.parseColor("#5DBE71"),
				Color.parseColor("#B2DA38"), Color.parseColor("#E5E14B") };
		images = new int[] { R.drawable.date_bg, R.drawable.date_bg1,
				R.drawable.date_bg2, R.drawable.date_bg3, R.drawable.date_bg4 };
		ArrayList<String> itemsimg = new ArrayList<String>();
		itemsimg.add("R.drawable.date_bg");
		itemsimg.add("R.drawable.date_bg1");
		itemsimg.add("R.drawable.date_bg2");
		itemsimg.add("R.drawable.date_bg3");
		itemsimg.add("R.drawable.date_bg4");

		eventdata = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra("nwevent");

		eventname = getIntent().getExtras().getString("eventname");
		if (eventdata.size() > 0) {
			tv_nweventtitle.setText(eventname);
			myadapter = new MyAdapter(AllEventDetailDisplayActivities.this,
					eventdata);
			eventlist.setAdapter(myadapter);
		} else {
			Toast.makeText(AllEventDetailDisplayActivities.this,
					"No Records Found.", Toast.LENGTH_SHORT).show();

		}

	}

	private void init() {
		tv_nweventtitle = (TextView) findViewById(R.id.tv_nweventtitle);
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		backbtn.setVisibility(View.VISIBLE);
		eventlist = (ListView) findViewById(R.id.lv_alleventlist);
		backbtn.setOnClickListener(this);
	}

	public class MyAdapter extends BaseAdapter {

		Activity activity;
		ArrayList<HashMap<String, String>> arrayactivity;

		public MyAdapter(Activity activity,
				ArrayList<HashMap<String, String>> eventdata) {
			this.activity = activity;
			this.arrayactivity = eventdata;
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

		@SuppressWarnings("unchecked")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = convertView;
			LayoutInflater infaltor = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = infaltor.inflate(R.layout.reminderlisting, null);

			final TextView tv_name = (TextView) v.findViewById(R.id.tv_name);

			if (((HashMap<String, String>) arrayactivity.get(position))
					.get("NwEvtName") != null
					&& ((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtName").length() > 0
					&& !((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtName").equalsIgnoreCase("null")) {
				tv_name.setText(""
						+ arrayactivity.get(position).get("NwEvtName"));
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
