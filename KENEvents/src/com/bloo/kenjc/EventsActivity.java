package com.bloo.kenjc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import maxwin.maxwin.view.XListView;
import maxwin.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class EventsActivity extends Activity implements OnClickListener,
		OnItemSelectedListener, IXListViewListener{
	TextView searcheventbycriteria, searcheventbytype;
	ImageView eventselector;

	String spinnerString;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	TextView eventtext, eventdayntime, eventdate, eventtitle;
	ImageButton events, notifications, contacts, more;
	MyAdapter myadapter;
	ArrayList<HashMap<String, String>> activitylist = new ArrayList<HashMap<String, String>>();
	ArrayList<Date> datelist = new ArrayList<Date>();
	ArrayList<Date> dateendlist = new ArrayList<Date>();
	ArrayList<String> Upcomingdatelist = new ArrayList<String>();
	ArrayList<String> Completeddatelist = new ArrayList<String>();
	ListView list;
	String REG_flag, str;
	HashMap<String, String> map;
	HashMap<String, String> actnamemap;
	HashMap<String, String> upcomingmap;
	HashMap<String, String> Completedmap;
	ArrayList<HashMap<String, String>> acttypelist = new ArrayList<HashMap<String, String>>();
	ArrayList<String> bigsorteddates = new ArrayList<String>();
	ArrayList<String> lesssorteddates = new ArrayList<String>();
	ArrayList<String> equalsorteddates = new ArrayList<String>();
	ArrayList<HashMap<String, String>> newlist = new ArrayList<HashMap<String, String>>();
	Integer sortingtype;
	SharedPreferences prefrence;
	Editor editor;
	String stored_date;
	private int[] colors;
	private int[] images;
	Display display;
	int height, width;
	ArrayList<String> Sortlist = new ArrayList<String>();
	ArrayList<String> Actlist = new ArrayList<String>();
	
	
	private Handler mHandler;
	private int counter = 0;
	private XListView eventlist;
	
	
	ArrayList<String> afterlist = new ArrayList<String>();
	ArrayList<String> beforlist = new ArrayList<String>();
	ArrayList<String> alreadylisted = new ArrayList<String>();

	int itemsimg;
	String EventId;
	int actTypeId = 0;
	int actTypeId1;
	String sortlist_data;
	String actlist_data;
	/* boolean sortingtype=false; */
	private boolean sortbydata=false;
	/*private ListView eventlist;*/
	private String Flag;
	ListView title;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventsearchtab);
		prefrence = getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();
		display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();
		init();
		
		Flag="Is first";
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoader.getInstance().init(config);

		System.gc();
		Runtime.getRuntime().gc();
		userloginstatus();
		colors = new int[] { Color.parseColor("#3DBFD9"),
				Color.parseColor("#5EC7A1"), Color.parseColor("#5DBE71"),
				Color.parseColor("#B2DA38"), Color.parseColor("#E5E14B") };
		images = new int[] { R.drawable.date_bg, R.drawable.date_bg1,
				R.drawable.date_bg2, R.drawable.date_bg3, R.drawable.date_bg4 };
		ArrayList<String> itemsimg = new ArrayList();
		itemsimg.add("R.drawable.date_bg");
		itemsimg.add("R.drawable.date_bg1");
		itemsimg.add("R.drawable.date_bg2");
		itemsimg.add("R.drawable.date_bg3");
		itemsimg.add("R.drawable.date_bg4");

		stored_date = prefrence.getString("store_date", "");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		String date = sdf.format(d);
		if (stored_date.equalsIgnoreCase("") || stored_date.length() == 0) {
			editor.putString("store_date", date);
			editor.commit();
		}

		String ac = prefrence.getString("activity", "");

		if (ac.equalsIgnoreCase("incoming")) {

			Intent i = new Intent(EventsActivity.this,NotificationMsgDisplayActivity.class);

			editor.remove("activity");
			editor.commit();
			startActivity(i);
		}
		
		
	}

	private void init() {
		searcheventbycriteria = (TextView) findViewById(R.id.dd_1_criteria);
		searcheventbytype = (TextView) findViewById(R.id.dd_2_type);
		eventlist = (XListView) findViewById(R.id.lv_eventlist);
		eventlist.setPullLoadEnable(true);
		eventlist.setXListViewListener(this);
		searcheventbycriteria.setOnClickListener(this);
		searcheventbytype.setOnClickListener(this);
	   mHandler = new Handler();
		

	}

	private void userloginstatus() {
		if (IsNetworkConnection.checkNetworkConnection(EventsActivity.this)) {
			counter++;

			String jsn = "[{\"" + "memId" + "\":" + "\""
					+ prefrence.getString("MemID", "") + "\"" + "}]";
			String url = Constants.SERVER_URL + "action=GetEvents"+"&page="+counter;
			if (prefrence.getString("MemID", "").equalsIgnoreCase("")) {
				new post_async(EventsActivity.this, "GetEvents").execute(url);
			} else {
				new post_async(EventsActivity.this, "GetEvents").execute(url,
						jsn);
			}
		} else {
			Toast.makeText(EventsActivity.this, "No Network", 2000).show();
		}
	}

	protected void Regetevent() {
		if (IsNetworkConnection.checkNetworkConnection(EventsActivity.this)) {

			String jsn = "[{\"" + "actTypeId" + "\":" + "\"" + actTypeId + "\""
					+ "}]";

			String url = Constants.SERVER_URL + "action=GetEvents";
			new post_async(EventsActivity.this, "GetEvents").execute(url, jsn);
		} else {
			Toast.makeText(EventsActivity.this, "No Network", 2000).show();
		}

	}

	protected void regeteventbydur() {
		if (IsNetworkConnection.checkNetworkConnection(EventsActivity.this)) {
			counter=0;
			String jsn = "[{\"" + "actTypeId" + "\":" + "\"" + actTypeId1
					+ "\"" + ",\"" + "sortType" + "\":" + "\"" + sortingtype
					+ "\"" + "}]";

			String url = Constants.SERVER_URL + "action=GetEvents"+ "&page="+counter;
			new post_async(EventsActivity.this, "GetEvents").execute(url, jsn);
		} else {
			Toast.makeText(EventsActivity.this, "No Network", 2000).show();
		}

	}

	protected void updatedevicetoken() {

		String jsn = "[{\"" + "memId" + "\":" + "\""
				+ prefrence.getString("MemID", "") + "\"" + ",\""
				+ "memDeviceType" + "\":" + "\"" + "android" + "\"" + ",\""
				+ "memUDID" + "\":" + "\"" + Constants.DEVICE_TOKEN + "\""
				+ "}]";

		String url = Constants.SERVER_URL + "action=UpdateDeviceInfo";
		new post_async(EventsActivity.this, "UpdateDeviceInfo").execute(url,
				jsn);

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
			v = infaltor.inflate(R.layout.customeeventlistdata, null);
			final LinearLayout mainll = (LinearLayout) v
					.findViewById(R.id.eventtimedate_ll);
			final LinearLayout ll1 = (LinearLayout) v.findViewById(R.id.ll_1);
			final LinearLayout ll2 = (LinearLayout) v.findViewById(R.id.ll_2);
			final TextView eventtitle = (TextView) v
					.findViewById(R.id.tv_eventtitle);
			final TextView eventplacename = (TextView) v
					.findViewById(R.id.tv_eventlocname);
			final TextView eventdetail = (TextView) v
					.findViewById(R.id.tv_eventdetails);
			final TextView eventtime = (TextView) v
					.findViewById(R.id.tv_eventtime);
			final TextView eventdate = (TextView) v
					.findViewById(R.id.tv_eventdate);
			final TextView eventmonth = (TextView) v
					.findViewById(R.id.tv_eventmonth);
			final TextView eventday = (TextView) v
					.findViewById(R.id.tv_eventday);
			final ImageView eventimage = (ImageView) v
					.findViewById(R.id.iv_eventplace);
			eventimage.getLayoutParams().height = (height * 15) / 100;
			imageLoader.displayImage(Constants.IMAGEDATAURL
					+ arrayactivity.get(position).get("eventimg")
					+ "&h=100&w=100&zc=1", eventimage, options);

			eventtitle.setText(arrayactivity.get(position).get("Name"));

			eventplacename.setText(arrayactivity.get(position).get("place"));

			eventdetail.setText(Html.fromHtml(arrayactivity.get(position).get("detail")));

			SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
			String dayname = null;
			String month = null;
			String dd = null;

			try {
				String date1 = arrayactivity.get(position).get("eventdate");
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
			String timeevt = null;
			String gettime = null;
			timeevt = arrayactivity.get(position).get("eventstarttime");

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				Date dateObj = sdf.parse(timeevt);
				gettime = new SimpleDateFormat("hh:mm aa").format(dateObj);
			} catch (final ParseException e) {
				e.printStackTrace();
			}

			eventtime.setText(gettime);
			int colorPos = position % colors.length;
			int imagePos = position % images.length;
			ll2.setBackgroundColor(colors[colorPos]);
			mainll.setBackgroundColor(colors[colorPos]);
			ll1.setBackgroundResource(images[imagePos]);

			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent eventdetail = new Intent(EventsActivity.this,
							EventDetailActivity.class);
					eventdetail.putExtra("event", arrayactivity.get(position));
					startActivity(eventdetail);
				}
			});
			return v;
		}

	}

	@Override
	public void onClick(View v) {

		if (v == searcheventbycriteria) {
			

			dialogSearchByduration();
		}
		if (v == searcheventbytype) {

			sortbydata = true;
			SearchByactType();

		}

	}

	private void SearchByactType() {

		AlertDialog.Builder dialog1 = new AlertDialog.Builder(
				EventsActivity.this);
		LayoutInflater inflater = (LayoutInflater) EventsActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.listofacttype, null, false);
		 title = (ListView) layout
				.findViewById(R.id.dialoglist_acttype);

		for (int i = 0; i < acttypelist.size(); i++) {

			if ((Actlist.contains(acttypelist.get(i).get("Actname").toString()))) {
			} else {
				Actlist.add(acttypelist.get(i).get("Actname"));
				if (acttypelist.size() == Actlist.size()) {
				}
			}

		}
		


		final AlertDialog Dial = dialog1.create();
		title.setAdapter(new ArrayAdapter<String>(EventsActivity.this,
				android.R.layout.select_dialog_singlechoice, Actlist));
		title.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		Dial.setView(layout);
		
		for (int i = 0; i < Actlist.size(); i++) {
			if(Actlist.get(i).equalsIgnoreCase(actlist_data))
			{
			      title.setItemChecked(Actlist.indexOf(actlist_data), true);
			     }
		} 
				
		
		title.setOnItemSelectedListener(this);
		title.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				searcheventbytype.setText(Actlist.get(position).toString());
                actlist_data=Actlist.get(position).toString();
                if (!Actlist.get(position).toString().equals(""))
                {
					actTypeId = Integer.parseInt(acttypelist.get(position).get("ActId"));
					
					if (actTypeId != 0)
					{
						
						actTypeId = Integer.parseInt(acttypelist.get(position).get("ActId"));
						if (sortbydata == true&& prefrence.getString("Sortlistvalue", "").equalsIgnoreCase("Upcoming"))

						{
							editor.putString("Sortlistvalue", "Upcoming").commit();
							editor.putString("AllSortinng", "Upcoming").commit();
							sortingtype = 1;
							actTypeId1 = actTypeId;
							regeteventbydur();

						} else if (sortbydata == true&& prefrence.getString("Sortlistvalue", "").equalsIgnoreCase("Completed")) {
							editor.putString("Sortlistvalue", "Completed").commit();
							editor.putString("AllSortinng", "Completed").commit();
							sortingtype = 2;
							actTypeId1 = actTypeId;
							regeteventbydur();

						} else { 
						
							editor.putString("AllSortinng", "All").commit();
							sortingtype = 3;
							actTypeId1 = actTypeId;
							regeteventbydur();

						}

					
					}
                }
                else
                {
                	editor.putString("AllSortinng", "All").commit();
                	sortingtype = 3;
					actTypeId1 = actTypeId;
					regeteventbydur();
                }
		

				editor.putInt("ActId", actTypeId1).commit();
				

				Dial.dismiss();
			}

		});

		Dial.show();

	}

	public void upcomningsorting() {
		newlist.clear();
		beforlist.clear();
		if (activitylist.size() < 0)

		{

			Toast.makeText(this, "You can't filter, There is no event", 1000)
					.show();
		} else {

			for (int i = 0; i < activitylist.size(); i++) {

				String activity_date = activitylist.get(i).get("eventenddate");

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

				Date d1, d2;
				try {
					d1 = df.parse(activity_date);
					Date now = new Date(System.currentTimeMillis());
					String date = df.format(now);
					d2 = df.parse(date);
					if (d1.after(d2) || d1.equals(d2)) {

					} else if (d1.before(d2)) {
						beforlist.add(df.format(d1));
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
            
			
			alreadylisted.clear();
			for (int k = 0; k < beforlist.size(); k++) {
				for (int j = 0; j < activitylist.size(); j++) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date afterdate, activitlistdate;
					try {
						afterdate = df.parse(beforlist.get(k));
						activitlistdate = df.parse(activitylist.get(j).get(
								"eventenddate"));
						if (afterdate.equals(activitlistdate)) {
							if (alreadylisted.contains(activitylist.get(j).get(
									"eventid"))) {
							} else {

								alreadylisted.add(activitylist.get(j).get(
										"eventid"));
								upcomingmap = new HashMap<String, String>();
								upcomingmap.put("eventid", activitylist.get(j)
										.get("eventid"));
								upcomingmap.put("Name", activitylist.get(j)
										.get("Name"));
								upcomingmap.put("detail", activitylist.get(j)
										.get("detail"));
								upcomingmap.put("place", activitylist.get(j)
										.get("place"));
								upcomingmap.put("Title", activitylist.get(j)
										.get("Title"));	
								upcomingmap.put("evtLocatioName", activitylist.get(j)
												.get("evtLocatioName"));
								upcomingmap.put("eventdate", activitylist
										.get(j).get("eventdate"));
								upcomingmap.put("eventenddate", activitylist
										.get(j).get("eventenddate"));
								
						
								upcomingmap.put("eventstarttime", activitylist
										.get(j).get("eventstarttime"));
								upcomingmap.put("eventendtime", activitylist
										.get(j).get("eventendtime"));
								upcomingmap.put("eventimg", activitylist.get(j)
										.get("eventimg"));
								upcomingmap.put("alreadyreg", activitylist.get(j)
										.get("alreadyreg"));
								upcomingmap.put("eventLat", activitylist.get(j)
										.get("eventLat"));
								upcomingmap.put("eventLon", activitylist.get(j)
										.get("eventLon"));
								newlist.add(upcomingmap);

							}
						}

					} catch (ParseException e) {
						e.printStackTrace();
					}

				}
			}
			Collections.sort(newlist,new Comparator<HashMap<String, String>>() {

						@Override
						public int compare(HashMap<String, String> lhs,HashMap<String, String> rhs) {
							return lhs.get("eventenddate").compareTo(
									rhs.get("eventenddate"));
						}
					});

			Collections.reverse(newlist);
			
			myadapter = new MyAdapter(EventsActivity.this, newlist);
			eventlist.setAdapter(myadapter);
		}

	}

	public void aftersorting() {
		
		newlist.clear();
		afterlist.clear();
		if (activitylist.size() < 0) {

			Toast.makeText(this, "You can't filter, There is no event", 1000)
					.show();
		} else {
			for (int i = 0; i < activitylist.size(); i++) {
				String activity_date = activitylist.get(i).get("eventdate");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date d1, d2;
				try {
					d1 = df.parse(activity_date);
					Date now = new Date(System.currentTimeMillis());
					String date = df.format(now);
					d2 = df.parse(date);
					if (d1.after(d2) || d1.equals(d2)) {
						afterlist.add(df.format(d1));
					} else if (d1.before(d2)) {
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			alreadylisted.clear();
			for (int k = 0; k < afterlist.size(); k++) {
				for (int j = 0; j < activitylist.size(); j++) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date afterdate, activitlistdate;
					try {
						afterdate = df.parse(afterlist.get(k));
						activitlistdate = df.parse(activitylist.get(j).get(
								"eventdate"));
						if (afterdate.equals(activitlistdate)) {

							if (alreadylisted.contains(activitylist.get(j).get(
									"eventid"))) {
							} else {
								alreadylisted.add(activitylist.get(j).get(
										"eventid"));
								upcomingmap = new HashMap<String, String>();
								upcomingmap.put("eventid", activitylist.get(j)
										.get("eventid"));
								upcomingmap.put("Name", activitylist.get(j)
										.get("Name"));
								upcomingmap.put("detail", activitylist.get(j)
										.get("detail"));
								upcomingmap.put("place", activitylist.get(j)
										.get("place"));
								upcomingmap.put("Title", activitylist.get(j)
										.get("Title"));
								upcomingmap.put("evtLocatioName", activitylist.get(j)
										.get("evtLocatioName"));
								
								upcomingmap.put("eventdate", activitylist
										.get(j).get("eventdate"));
								upcomingmap.put("eventenddate", activitylist
										.get(j).get("eventenddate"));
								upcomingmap.put("eventstarttime", activitylist
										.get(j).get("eventstarttime"));
								upcomingmap.put("eventendtime", activitylist
										.get(j).get("eventendtime"));
								//	map.put("eventendtime", jobj.getString("evtEndTime"));
								upcomingmap.put("eventimg", activitylist.get(j)
										.get("eventimg"));
								upcomingmap.put("alreadyreg", activitylist.get(j)
										.get("alreadyreg"));
								upcomingmap.put("eventLat", activitylist.get(j)
										.get("eventLat"));
								upcomingmap.put("eventLon", activitylist.get(j)
										.get("eventLon"));
								newlist.add(upcomingmap);

							}

						}

					} catch (ParseException e) {
						e.printStackTrace();
					}

				}
			}
		/*	Collections.sort(newlist,
					new Comparator<HashMap<String, String>>() {

						@Override
						public int compare(HashMap<String, String> lhs,
								HashMap<String, String> rhs) {
							return lhs.get("eventdate").compareTo(
									rhs.get("eventdate"));
						}
					});

			Collections.reverse(newlist);*/
			
			myadapter = new MyAdapter(EventsActivity.this, newlist);
			eventlist.setAdapter(myadapter);

		}
	}

	private void dialogSearchByduration() {

		AlertDialog.Builder dialog1 = new AlertDialog.Builder(
				EventsActivity.this);
		LayoutInflater inflater = (LayoutInflater) EventsActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.listviewfile, null, false);

		final ListView title = (ListView) layout.findViewById(R.id.dialoglist);

		Sortlist.clear();

		Sortlist.add("Upcoming");
		Sortlist.add("Completed");
		final AlertDialog Dial = dialog1.create();
		title.setAdapter(new ArrayAdapter<String>(EventsActivity.this,
				android.R.layout.select_dialog_singlechoice, Sortlist));
		title.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		Dial.setView(layout);
		
		
		if (sortlist_data!=null) {
			if(sortlist_data.equalsIgnoreCase("Upcoming")){
			      title.setItemChecked(0, true);
			     }
			else if(sortlist_data.equalsIgnoreCase("Completed")){
			      title.setItemChecked(1, true);
			     }
		}
		else
		{
			sortlist_data="";
		}
		
		
		title.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				searcheventbycriteria.setText(Sortlist.get(position).toString());
				sortlist_data=Sortlist.get(position).toString();
				editor.commit();
				if (position == 0) {
					editor.putString("Sortlistvalue", "Upcoming").commit();
					if (sortbydata == true) {
						if (prefrence.getInt("ActId", actTypeId) != 0) {
							editor.putString("AllSortinng", "Upcoming").commit();
							sortingtype = 1;
							actTypeId1 = prefrence.getInt("ActId", actTypeId);
							regeteventbydur();
						} else {
							sortbydata = false;
							editor.putString("Eventsorting", "After");
							editor.putInt("ActId",0).commit();
							editor.commit();
							aftersorting();

						}

					} else {
						sortbydata = false;
						editor.putString("Eventsorting", "After");
						editor.putInt("ActId",0).commit();
						editor.commit();
						aftersorting();
					}
				}
				if (position == 1) {
					editor.putString("Sortlistvalue", "Completed").commit();
					if (sortbydata = true) {
					
						if (prefrence.getInt("ActId", actTypeId) != 0) {
							editor.putString("AllSortinng", "Completed").commit();
							sortingtype = 2;
							actTypeId1 = prefrence.getInt("ActId", actTypeId);
							regeteventbydur();
							
						} else {
							sortbydata = false;
							editor.putString("Eventsorting", "before");
							editor.putInt("ActId",0).commit();
							editor.commit();
							upcomningsorting();
							
						}
					} else {
						sortbydata = false;
						editor.putString("Eventsorting", "before");
						editor.putInt("ActId",0).commit();
						editor.commit();
						upcomningsorting();
					}
				}
				Dial.dismiss();
			}
		});
		Dial.show();

	}

	private void showactivitybytype() {

		if (IsNetworkConnection.checkNetworkConnection(EventsActivity.this)) {

			String url = Constants.SERVER_URL + "action=GetActivities";

			new post_async(EventsActivity.this, "GetActivities").execute(url);
		} else {
			Toast.makeText(EventsActivity.this, "No Network", 2000).show();
		}

	}


	public void responseofEvents(String resultString) {
		activitylist.clear();
		try {
			JSONArray jarray = new JSONArray(resultString);
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jobj = jarray.getJSONObject(i);
				if (!jobj.has("alreadyregistered")) {
					map = new HashMap<String, String>();
					map.put("eventid", jobj.getString("evtId"));
					map.put("eventacttypeid", jobj.getString("evtactTypeId"));
					map.put("Name", jobj.getString("evtName"));
					map.put("eventdate", jobj.getString("evtStartDate"));
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date eventdate = df.parse(jobj.getString("evtStartDate"));
					datelist.add(eventdate);
					map.put("eventenddate", jobj.getString("evtEndDate"));
					SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
					Date eventenddate = df.parse(jobj.getString("evtEndDate"));
					dateendlist.add(eventenddate);
					map.put("eventstarttime", jobj.getString("evtStartTime"));
					map.put("eventendtime", jobj.getString("evtEndTime"));
					map.put("detail", jobj.getString("evtInfo"));
					map.put("eventimg", jobj.getString("evtImage"));
					map.put("location", jobj.getString("evtLocatioName"));
					map.put("place", jobj.getString("evtAddress"));
					map.put("eventLat", jobj.getString("evtLatitude"));
					map.put("eventLon", jobj.getString("evtLongitude"));
					map.put("Title", jobj.getString("evtTags"));
					map.put("alreadyreg","");
					editor.putString("eventname", jobj.getString("evtName"));
					editor.putString("date", jobj.getString("evtStartDate"));
					editor.putString("alreadyreg","").commit();
					editor.commit();
					activitylist.add(map);
				}

				else if (!prefrence.getString("MemID", "").equalsIgnoreCase("")) {
					map.put("eventid", jobj.getString("evtId"));
					map.put("Name", jobj.getString("evtName"));
					map.put("detail", jobj.getString("evtInfo"));
					map.put("place", jobj.getString("evtAddress"));
					map.put("Title", jobj.getString("evtTags"));
					map.put("location", jobj.getString("evtLocatioName"));
					map.put("eventdate", jobj.getString("evtStartDate"));
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date eventdate = df.parse(jobj.getString("evtStartDate"));
					datelist.add(eventdate);
					map.put("eventenddate", jobj.getString("evtEndDate"));
					SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
					Date eventenddate = df.parse(jobj.getString("evtEndDate"));
					dateendlist.add(eventenddate);
					map.put("eventstarttime", jobj.getString("evtStartTime"));
					map.put("eventendtime", jobj.getString("evtEndTime"));
					map.put("eventimg", jobj.getString("evtImage"));
					map.put("alreadyreg", jobj.getString("alreadyregistered"));
					map.put("eventLat", jobj.getString("evtLatitude"));
					map.put("eventLon", jobj.getString("evtLongitude"));
					map.put("alreadyreg",jobj.getString("alreadyregistered"));
					editor.putString("eventname", jobj.getString("evtName"));
					editor.putString("date", jobj.getString("evtStartDate"));
					editor.putString("alreadyreg",jobj.getString("alreadyregistered")).commit();
					activitylist.add(map);
					editor.commit();

				} else {
					map = new HashMap<String, String>();
					map.put("eventid", jobj.getString("evtId"));
					map.put("Name", jobj.getString("evtName"));
					map.put("detail", jobj.getString("evtInfo"));
					map.put("place", jobj.getString("evtAddress"));
					map.put("location", jobj.getString("evtLocatioName"));
					map.put("Title", jobj.getString("evtTags"));
					map.put("eventdate", jobj.getString("evtStartDate"));
					map.put("date", jobj.getString("evtStartDate"));
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date eventdate = df.parse(jobj.getString("evtStartDate"));
					dateendlist.add(eventdate);
					map.put("eventenddate", jobj.getString("evtEndDate"));
					SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
					Date eventenddate = df.parse(jobj.getString("evtEndDate"));
					dateendlist.add(eventenddate);
					map.put("eventstarttime", jobj.getString("evtStartTime"));
					map.put("eventendtime", jobj.getString("evtEndTime"));
					map.put("eventimg", jobj.getString("evtImage"));
					map.put("eventLat", jobj.getString("evtLatitude"));
					map.put("eventLon", jobj.getString("evtLongitude"));
					map.put("alreadyreg","");
                    editor.putString("eventname", jobj.getString("evtName"));
					editor.putString("date", jobj.getString("evtStartDate"));
					editor.putString("alreadyreg","").commit();
					editor.commit();
					activitylist.add(map);
				}
			}
		} catch (Exception e) {

		}
		if (activitylist.size() <= 0) {

				activitylist.clear();
				Toast.makeText(this, "No event list found", 1000).show();
				myadapter = new MyAdapter(this, activitylist);
				eventlist.setAdapter(myadapter);
				eventlist.setPullLoadEnable(false);
		
		}
		
		else if (prefrence.getString("AllSortinng", "").equalsIgnoreCase("Upcoming")&&prefrence.getString("Sortlistvalue", "").equalsIgnoreCase("Upcoming")) {
			
			aftersorting();
		}
		else if(prefrence.getString("AllSortinng", "").equalsIgnoreCase("Completed")&& prefrence.getString("Sortlistvalue", "").equalsIgnoreCase("Completed"))
		{
			upcomningsorting();
		}
		else if (prefrence.getString("AllSortinng", "").equalsIgnoreCase("All"))
		{
			myadapter = new MyAdapter(this, activitylist);
			eventlist.setAdapter(myadapter);
		}
		else if  (prefrence.getString("AllSortinng", "").equalsIgnoreCase("afterrefresh")) {
			aftersorting();
		}
	
		else {
			aftersorting();
		}
	
		showactivitybytype();

	}

	public void responseofGetActivities(String resultString) {

		try {

			JSONArray jarray = new JSONArray(resultString);
			for (int i = 0; i < jarray.length(); i++)
			{
				actnamemap = new HashMap<String, String>();
				JSONObject jobj = jarray.getJSONObject(i);
				actnamemap.put("Actname", jobj.getString("actTypeName"));
				actnamemap.put("ActId", jobj.getString("actTypeId"));
				acttypelist.add(actnamemap);

			}
			updatedevicetoken();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void responseofupdatedeviceinfo(String resultString) {
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		searcheventbycriteria.setText(Sortlist.get(1).toString());
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				sortbydata = false;
				searcheventbytype.setText("");
				searcheventbycriteria.setText("");
				actlist_data="";
				sortlist_data="";
				editor.putString("AllSortinng", "afterrefresh").commit();
				editor.putString("Sortlistvalue", "").commit();
				userloginstatus();
				onLoad();
			}
		}, 2000);
		
	}
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
              sortbydata = false;
				searcheventbytype.setText("");
				searcheventbycriteria.setText("");
				actlist_data="";
				sortlist_data="";
				searcheventbycriteria.setText("");
				editor.putString("AllSortinng", "afterrefresh").commit();
				editor.putString("Sortlistvalue", "").commit();
				userloginstatus();
				onLoad();
			}
		}, 2000);
	
	}
	private void onLoad() {
		eventlist.stopRefresh();
		eventlist.stopLoadMore();
		eventlist.setRefreshTime("Loading");
	}
	
}
