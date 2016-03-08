package com.bloo.kenjc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
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

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

@SuppressLint({ "ViewHolder", "SimpleDateFormat" })
public class NewEventDisplayActivity extends Activity implements
		OnClickListener {
	ListView eventlist;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private int[] colors;
	private int[] images;
	MyAdapter myadapter;
	private ImageView backbtn;
	ArrayList<HashMap<String, String>> eventdata = new ArrayList<HashMap<String, String>>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neweventdisplay);
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
		if (eventdata.size() > 0) {
			myadapter = new MyAdapter(NewEventDisplayActivity.this, eventdata);
			eventlist.setAdapter(myadapter);
		} else {
			Toast.makeText(NewEventDisplayActivity.this, "No Records Found.",
					Toast.LENGTH_SHORT).show();

		}

	}

	private void init() {

		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		backbtn.setVisibility(View.VISIBLE);
		eventlist = (ListView) findViewById(R.id.lv_nweventlist);

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
			v = infaltor.inflate(R.layout.customeeventlistdata, null);
			final LinearLayout mainll = (LinearLayout) v
					.findViewById(R.id.eventtimedate_ll);
			final LinearLayout ll1 = (LinearLayout) v.findViewById(R.id.ll_1);
			final LinearLayout ll2 = (LinearLayout) v.findViewById(R.id.ll_2);
			final TextView eventtitle = (TextView) v
					.findViewById(R.id.tv_eventtitle);
			final LinearLayout location_ll = (LinearLayout) v
					.findViewById(R.id.location_ll);
			final TextView eventplacename = (TextView) v
					.findViewById(R.id.tv_eventlocname);
			final TextView eventdetail = (TextView) v
					.findViewById(R.id.tv_eventdetails);
			final TextView eventdate = (TextView) v
					.findViewById(R.id.tv_eventdate);
			final TextView eventmonth = (TextView) v
					.findViewById(R.id.tv_eventmonth);
			final TextView eventday = (TextView) v
					.findViewById(R.id.tv_eventday);
			final ImageView eventimage = (ImageView) v
					.findViewById(R.id.iv_eventplace);

			if (((HashMap<String, String>) arrayactivity.get(position))
					.get("NwEvtimage") != null
					&& ((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtimage").length() > 0
					&& !((HashMap<String, String>) arrayactivity.get(position))
							.get("NwEvtimage").equalsIgnoreCase("null")) {
				eventimage.setVisibility(View.VISIBLE);
				imageLoader.displayImage(
						Constants.IMAGEDATAURL
								+ ((HashMap<String, String>) arrayactivity
										.get(position)).get("NwEvtimage")
								+ "&h=100&w=100&zc=1", eventimage, options);
			} else {
				eventimage.setVisibility(View.GONE);
			}

			if (((HashMap<String, String>) arrayactivity.get(position))
					.get("Name") != null
					&& ((HashMap<String, String>) arrayactivity.get(position))
							.get("Name").length() > 0
					&& !((HashMap<String, String>) arrayactivity.get(position))
							.get("Name").equalsIgnoreCase("null")) {
				eventtitle.setVisibility(View.VISIBLE);
				eventtitle.setText(((HashMap<String, String>) arrayactivity
						.get(position)).get("Name"));
			} else {
				eventtitle.setVisibility(View.GONE);
				eventtitle.setText("");
			}

			if (((HashMap<String, String>) arrayactivity.get(position))
					.get("Nwevtadd") != null
					&& ((HashMap<String, String>) arrayactivity.get(position))
							.get("Nwevtadd").length() > 0
					&& !((HashMap<String, String>) arrayactivity.get(position))
							.get("Nwevtadd").equalsIgnoreCase("null")) {
				location_ll.setVisibility(View.VISIBLE);
				eventplacename.setText(((HashMap<String, String>) arrayactivity
						.get(position)).get("Nwevtadd"));
			} else {
				location_ll.setVisibility(View.GONE);
				eventplacename.setText("");
			}
			if (((HashMap<String, String>) arrayactivity.get(position))
					.get("Nwevtinfo") != null
					&& ((HashMap<String, String>) arrayactivity.get(position))
							.get("Nwevtinfo").length() > 0
					&& !((HashMap<String, String>) arrayactivity.get(position))
							.get("Nwevtinfo").equalsIgnoreCase("null")) {
				eventdetail.setVisibility(View.VISIBLE);
				eventdetail.setText(Html
						.fromHtml(((HashMap<String, String>) arrayactivity
								.get(position)).get("Nwevtinfo")));
			} else {
				eventdetail.setVisibility(View.GONE);
				eventdetail.setText("");
			}
			SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
			String dayname = null;
			String month = null;
			String dd = null;

			try {
				String date1 = ((HashMap<String, String>) arrayactivity
						.get(position)).get("Nwevtstartdate");

				SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date d = inFormat.parse(date1);
				SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
				dayname = outFormat.format(d);
				SimpleDateFormat outFormat1 = new SimpleDateFormat("MMM");
				month = outFormat1.format(d);
				SimpleDateFormat outFormat2 = new SimpleDateFormat("dd");
				dd = outFormat2.format(d);
			} catch (ParseException e) {

				e.printStackTrace();
			}
			eventdate.setText(dd);
			eventday.setText(dayname);
			eventmonth.setText(month);

			if (((HashMap<String, String>) arrayactivity.get(position)).get(
					"Nwevtstartdate").length() > 0
					&& !((HashMap<String, String>) arrayactivity.get(position))
							.get("Nwevtstartdate").equalsIgnoreCase("null")) {
				ll2.setVisibility(View.VISIBLE);
				ll1.setVisibility(View.VISIBLE);
				mainll.setVisibility(View.VISIBLE);

				int colorPos = position % colors.length;
				int imagePos = position % images.length;
				ll2.setBackgroundColor(colors[colorPos]);
				mainll.setBackgroundColor(colors[colorPos]);
				ll1.setBackgroundResource(images[imagePos]);
			} else {
				ll2.setVisibility(View.GONE);
				ll1.setVisibility(View.GONE);
				mainll.setVisibility(View.GONE);
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
