package com.bloo.kenjc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.post_async;
import com.fipl.kenjc.fb.DialogError;
import com.fipl.kenjc.fb.Facebook;
import com.fipl.kenjc.fb.Facebook.DialogListener;
import com.fipl.kenjc.fb.FacebookError;
import com.fipl.kenjc.fb.FbLoginCommon;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

@SuppressLint("NewApi")
public class EventDetailActivity extends Activity implements OnClickListener {
	ImageView backbtn, sharebtn;
	Display display;
	int height, width;
	Button Attendyes, Attendmaybe;
	SharedPreferences prefrence;
	Editor editor;
	EventDetailActivity context;
	private Button twitter;
	private Button fbIcon;
	private ImageView closebtn;
	private Facebook facebook = new Facebook("1434012063564344");
	LinearLayout attendevent;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	ImageView eventimage;
	private TextView eventday, eventdatentime, eventlocation, eventtitle,
			eventdetaildata, eventtagtitle;
	static HashMap<String, String> data = new HashMap<String, String>();
	private ImageView mapdetail;
	String Latitude, Longitude;
	String name, startdate, enddate, eventplace;
	String eventdetail, attentAStatus = "";
	ImageView calender;
	/* private int[] calendarId; */
	private Button Whatsapp;
	private Button Mail;
	private Button sms;
	private LinearLayout ll_attend;
	String urlTwitter = "https://twitter.com/share?text=";
	private String title;
	String eventtime;
	String str;
	private LinearLayout maplayout;
	private String dateofevent;
	private String dayofevent;
	private String monthofevent;
	private static String eventUriStr;
	private static String reminderUriStr;
	long eventID;
	String calid, calname, calplace;
	String dataadd = "";
	private Handler mHandler;
	Uri eventsUri, remainderUri;
	MyObserver observe;
	private ScrollView sub_scroll;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Constants.executeLogcat(EventDetailActivity.this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventdetailscreen);
		prefrence = getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();
		display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();
		init();
		mHandler = new Handler();
		observe = new MyObserver(mHandler);

		if (!data.get("alreadyreg").equals("")) {
			if (data.get("alreadyreg").equalsIgnoreCase("Yes")) {
				editor.putString(data.get("eventid"), "yes");
				Attendyes.setBackgroundResource(R.drawable.btn);
				Attendmaybe.setBackgroundResource(R.drawable.btn1);
			} else if (data.get("alreadyreg").equalsIgnoreCase("May Be")) {
				editor.putString(data.get("eventid"), "maybe");
				Attendyes.setBackgroundResource(R.drawable.btn1);
				Attendmaybe.setBackgroundResource(R.drawable.btn);
			} else {

				if (prefrence.getString("Status", "no").equalsIgnoreCase("Yes")) {
					editor.putString(data.get("eventid"), "yes");
					Attendyes.setBackgroundResource(R.drawable.btn);
					Attendmaybe.setBackgroundResource(R.drawable.btn1);
				} else if (prefrence.getString("Status", "no")
						.equalsIgnoreCase("May Be"))

				{
					editor.putString(data.get("eventid"), "maybe");
					Attendyes.setBackgroundResource(R.drawable.btn1);
					Attendmaybe.setBackgroundResource(R.drawable.btn);
				} else {

					if (prefrence.getString("attentAStatus", "no")
							.equalsIgnoreCase("Yes")) {
						editor.putString(data.get("eventid"), "yes");
						Attendyes.setBackgroundResource(R.drawable.btn);
						Attendmaybe.setBackgroundResource(R.drawable.btn1);
					} else if (prefrence.getString("attentAStatus", "no")
							.equalsIgnoreCase("May Be"))

					{
						editor.putString(data.get("eventid"), "maybe");
						Attendyes.setBackgroundResource(R.drawable.btn1);
						Attendmaybe.setBackgroundResource(R.drawable.btn);
					} else {
						editor.putString(data.get("eventid"), "");
						Attendyes.setBackgroundResource(R.drawable.btn1);
						Attendmaybe.setBackgroundResource(R.drawable.btn1);
					}
				}
			}
		}

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
		String value = data.get("alreadyreg");

	}

	public boolean isAppRunning(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> services = activityManager
				.getRunningTasks(Integer.MAX_VALUE);
		if (services.get(0).topActivity.getPackageName().toString()
				.equalsIgnoreCase(context.getPackageName().toString())) {
			return true;
		}
		return false;
	}

	private void init() {
		ll_attend = (LinearLayout) findViewById(R.id.ll_attend);
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		sharebtn = (ImageView) findViewById(R.id.headershare);
		backbtn.setVisibility(View.VISIBLE);
		sharebtn.setVisibility(View.VISIBLE);
		Attendyes = (Button) findViewById(R.id.bt_attendyes);
		Attendmaybe = (Button) findViewById(R.id.bt_attendmaybe);
		eventimage = (ImageView) findViewById(R.id.iv_eventdetailgallery);
		eventtitle = (TextView) findViewById(R.id.tv_eventdetailtitle);
		eventlocation = (TextView) findViewById(R.id.tv_eventlocation);
		eventdatentime = (TextView) findViewById(R.id.tv_eventdetaildatentime);
		eventdetaildata = (TextView) findViewById(R.id.tv_eventdetaildata);
		sub_scroll = (ScrollView) findViewById(R.id.sub_scroll);
		sub_scroll.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		eventtagtitle = (TextView) findViewById(R.id.tv_eventdetailtagdata);
		attendevent = (LinearLayout) findViewById(R.id.ll_attend);
		maplayout = (LinearLayout) findViewById(R.id.evtdetail_map_rl);
		calender = (ImageView) findViewById(R.id.iv_eventdetailcal);
		data = (HashMap<String, String>) getIntent().getSerializableExtra(
				"event");

		imageLoader.displayImage(Constants.IMAGEURL + data.get("eventimg"),
				eventimage, options);

		eventlocation.setText(data.get("place"));
		title = data.get("Title");
		if (title.contains(",")) {
			String whatyouaresearching = title.substring(0,
					title.lastIndexOf(","));
			eventtagtitle.setText(whatyouaresearching);
		} else {

			eventtagtitle.setText(data.get("Title"));
		}
		eventtitle.setText(data.get("Name"));
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String dayname = null;
		String month = null;
		String dd = null;

		try {
			String date1 = data.get("eventdate");
			;
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

		dateofevent = dd;
		dayofevent = dayname;
		monthofevent = month;
		String date = data.get("eventdate");
		String inputPattern = "yyyy-MM-dd";
		String outputPattern = "EEEE, dd MMMM yyyy ";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		Date ddate = null;
		str = null;
		try {
			ddate = inputFormat.parse(date);
			str = outputFormat.format(ddate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		eventdatentime.setText(str);
		String time = null;
		eventtime = null;
		time = data.get("eventstarttime");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			Date dateObj = sdf.parse(time);
			eventtime = new SimpleDateFormat("hh:mm aa").format(dateObj);
			System.out.println(dateObj);
			System.out
					.println(new SimpleDateFormat("hh:mm aa").format(dateObj));
		} catch (final ParseException e) {
			e.printStackTrace();
		}

		String enddate = data.get("eventenddate");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Date d1, d2;
		try {
			d1 = df.parse(enddate);
			Date now = new Date(System.currentTimeMillis());
			String date1 = df.format(now);
			d2 = df.parse(date1);
			if (d1.before(d2)) {
				ll_attend.setVisibility(View.GONE);

			} else if (d1.before(d2)) {
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		eventdatentime.append(eventtime);
		eventdetaildata.setText(Html.fromHtml(data.get("detail")));

		backbtn.setOnClickListener(this);
		sharebtn.setOnClickListener(this);
		Attendyes.setOnClickListener(this);
		Attendmaybe.setOnClickListener(this);
		calender.setOnClickListener(this);
		maplayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == backbtn) {
			Intent i = new Intent(EventDetailActivity.this,
					EventsActivity.class);
			startActivity(i);

		}
		if (v == maplayout) {
			prefrence.edit().putString("fromMore", "eventdetail").commit();
			Intent mapdetail = new Intent(EventDetailActivity.this,
					EventMapDisplay.class);
			mapdetail.putExtra("eventLong", data.get("eventLon"));
			mapdetail.putExtra("eventLat", data.get("eventLat"));
			mapdetail.putExtra("EvtName", data.get("Name"));
			mapdetail.putExtra("EvtDate", str);
			mapdetail.putExtra("EvtTime", eventtime);
			startActivity(mapdetail);
		}
		if (v == Attendyes) {
			if (prefrence.getString("MemID", "").equalsIgnoreCase("")) {
				prefrence.edit().putString("fromMore", "eventdetail").commit();
				Intent logintoattend = new Intent(EventDetailActivity.this,
						LoginActivity.class);
				startActivity(logintoattend);
			} else {

				eventattendyes();
			}
		}
		if (v == Attendmaybe)

		{
			if (prefrence.getString("MemID", "").equalsIgnoreCase("")) {
				prefrence.edit().putString("fromMore", "eventdetail").commit();
				Intent logintoattend = new Intent(EventDetailActivity.this,
						LoginActivity.class);
				startActivity(logintoattend);
			} else {

				eventattendmaybe();
			}
		}
		if (v == calender) {

			String enddate = data.get("eventenddate");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			Date d1, d2;
			try {
				d1 = df.parse(enddate);
				Date now = new Date(System.currentTimeMillis());
				String date1 = df.format(now);
				d2 = df.parse(date1);
				if (d1.before(d2)) {

					Toast.makeText(this, "Cannot Add Past Event to calender",
							1000).show();
					calender.setEnabled(false);

				} else {
					if (!readCalendarEvent()) {
						addToCalender();

						Toast.makeText(this,
								"Event has  been added to calender", 1000)
								.show();
					} else {
						Toast.makeText(this,
								"Event has already been added to calender",
								1000).show();
					}
				}

			} catch (ParseException e) {
				e.printStackTrace();
				Constants.executeLogcat(EventDetailActivity.this);
			}

		}

		if (v == sharebtn) {
			ShareDialog();
		}

	}

	public void addToCalender() throws ParseException {

		Cursor cursor;
		if (android.os.Build.VERSION.SDK_INT <= 7) {

			eventsUri = Uri.parse("content://calendar/events");
			remainderUri = Uri.parse("content://calendar/reminders");
			cursor = EventDetailActivity.this.getContentResolver().query(
					Uri.parse("content://calendar/calendars"),
					new String[] { "_id", "displayName" }, null, null, null);

		}

		else if (android.os.Build.VERSION.SDK_INT <= 14) {
			eventsUri = Uri.parse("content://com.android.calendar/events");
			remainderUri = Uri
					.parse("content://com.android.calendar/reminders");
			cursor = EventDetailActivity.this.getContentResolver().query(
					Uri.parse("content://com.android.calendar/calendars"),
					new String[] { "_id", "displayName" }, null, null, null);

		}

		else {
			eventsUri = Uri.parse("content://com.android.calendar/events");
			remainderUri = Uri
					.parse("content://com.android.calendar/reminders");
			cursor = EventDetailActivity.this.getContentResolver().query(
					Uri.parse("content://com.android.calendar/calendars"),
					new String[] { "_id", "calendar_displayName" }, null, null,
					null);

		}

		Log.e("count", "" + cursor.getColumnName(0));
		String[] calendarNames = new String[cursor.getCount()];
		String[] calendarId = new String[cursor.getCount()];
		cursor.moveToFirst();
		for (int i = 0; i < calendarNames.length; i++) {
			calendarId[i] = cursor.getString(0);
			calendarNames[i] = cursor.getString(1);
			cursor.moveToNext();

		}

		long startCalTime;
		long endCalTime;
		Date eventDate = null;

		TimeZone timeZone = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance();

		eventDate = new SimpleDateFormat("MM/dd/yyyy").parse("09/23/2014");
		cal.setTime(eventDate);

		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 43);
		startCalTime = cal.getTimeInMillis();

		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 45);
		endCalTime = cal.getTimeInMillis();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-mm-dd");
		Date d = f.parse(data.get("eventdate"));
		long startmilliseconds = d.getTime();
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-mm-dd");
		Date d1 = f1.parse(data.get("eventenddate"));
		long endmilliseconds = d1.getTime();
		long timeInMilliseconds = 0;
		long timeInMilliseconds1 = 0;
		String givenDateString = data.get("eventdate") + " "
				+ data.get("eventstarttime");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		try {
			Date mDate = sdf.parse(givenDateString);
			timeInMilliseconds = mDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String givenDateString1 = data.get("eventenddate") + " "
				+ data.get("eventendtime");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		try {
			Date mDate1 = sdf1.parse(givenDateString1);
			timeInMilliseconds1 = mDate1.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ContentValues event = new ContentValues();
		event.put(CalendarContract.Events.CALENDAR_ID, 1);
		event.put(CalendarContract.Events.TITLE, data.get("Name"));
		event.put(CalendarContract.Events.DESCRIPTION, data.get("detail"));
		event.put(CalendarContract.Events.EVENT_LOCATION, data.get("place"));
		event.put(CalendarContract.Events.DTSTART,
				timeInMillis(data.get("eventdate"), data.get("eventstarttime")));
		event.put(
				CalendarContract.Events.DTEND,
				timeInMillis(data.get("eventenddate"), data.get("eventendtime")));
		event.put(CalendarContract.Events.HAS_ALARM, 1);
		event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		Uri insertEventUri = EventDetailActivity.this.getContentResolver()
				.insert(CalendarContract.Events.CONTENT_URI, event);

	}

	private int[] calendarId;

	boolean readCalendarEvent() {
		int counter = 0;

		boolean flag = false;

		try {

			Cursor cursor = EventDetailActivity.this.getContentResolver()
					.query(CalendarContract.Events.CONTENT_URI,
							new String[] { CalendarContract.Events.CALENDAR_ID,
									CalendarContract.Events.TITLE,
									CalendarContract.Events.DESCRIPTION,
									CalendarContract.Events.EVENT_LOCATION,
									CalendarContract.Events.DTSTART,
									CalendarContract.Events.DTEND }, null,
							null, null);
			cursor.setNotificationUri(
					EventDetailActivity.this.getContentResolver(),
					CalendarContract.Events.CONTENT_URI);

			mHandler = new Handler();
			MyObserver observe = new MyObserver(mHandler);
			EventDetailActivity.this.getContentResolver()
					.registerContentObserver(
							CalendarContract.Events.CONTENT_URI, true, observe);

			if (cursor != null && cursor.getCount() > 0) {

				cursor.moveToFirst();
				while (cursor.moveToNext()) {

					if (!(cursor.getString(0) == null)) {
						calid = cursor.getString(0);
					} else {
						if (calid.isEmpty() || calid.equalsIgnoreCase("null")
								|| calid.length() == 0) {
							calid = "noid";

						}
					}
					if (!(cursor.getString(1) == null)) {
						calname = cursor.getString(1);
					} else {
						if (calname.isEmpty()
								|| calname.equalsIgnoreCase("null")
								|| calname.length() == 0) {
							calname = "noname";

						}
					}

					if (!(cursor.getString(3) == null)) {
						calplace = cursor.getString(3);
					} else {
						if (calplace.isEmpty()
								|| calplace.equalsIgnoreCase("null")
								|| calplace.length() == 0) {
							calplace = "noplace";

						}
					}
					
					if (calid.equals("1")
							&& calname.equalsIgnoreCase(data.get("Name"))
							&& calplace.equalsIgnoreCase(data.get("place"))) {
						flag = true;
						break;
					}

				}
				cursor.close();

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return flag;

	}

	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd/MM/yyyy hh:mm:ss a");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	private void eventattendmaybe() {
		if (attentAStatus.equalsIgnoreCase("May Be")) {
			Toast.makeText(EventDetailActivity.this,
					"You Have already Registered for this event", 1000).show();
		} else {

			if (prefrence.getString("Attendmaybe", "").isEmpty()) {
				Attendyes.setBackgroundResource(R.drawable.btn1);
				Attendmaybe.setBackgroundResource(R.drawable.btn);
				attentAStatus = "May Be";
				attendeventloginapi();
				editor.putString("attend", "firsttym");
				editor.putString(data.get("eventid"), "maybe");
				editor.commit();

				Toast.makeText(EventDetailActivity.this,
						"You will attend event", 1000).show();

			} else {
				Attendyes.setBackgroundResource(R.drawable.btn1);
				Attendmaybe.setBackgroundResource(R.drawable.btn);
				attentAStatus = "May Be";
				attendeventloginapi();
				editor.putString("attend", "");
				editor.putString(data.get("eventid"), "maybe");
				editor.commit();

			}

		}

	}

	private void eventattendyes() {

		if (attentAStatus.equalsIgnoreCase("Yes")) {
			Toast.makeText(EventDetailActivity.this,
					"You Have already Registered for this event", 1000).show();
		} else {

			if (prefrence.getString(data.get("eventid"), "").isEmpty()) {
				Attendyes.setBackgroundResource(R.drawable.btn);
				Attendmaybe.setBackgroundResource(R.drawable.btn1);

				attentAStatus = "Yes";
				attendeventloginapi();
				editor.putString("attend", "firsttym");
				editor.putString(data.get("eventid"), "yes");
				editor.commit();

				Toast.makeText(EventDetailActivity.this,
						"You are attending event", 1000).show();

			} else {
				Attendyes.setBackgroundResource(R.drawable.btn);
				Attendmaybe.setBackgroundResource(R.drawable.btn1);
				attentAStatus = "Yes";
				attendeventloginapi();
				editor.putString("attend", "");
				editor.putString(data.get("eventid"), "yes");
				editor.commit();

			}

		}

	}

	void ShareDialog() {
		final Dialog dialog = new Dialog(EventDetailActivity.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.sharing);
		twitter = (Button) dialog.findViewById(R.id.twitter);
		fbIcon = (Button) dialog.findViewById(R.id.fbIcon);
		Mail = (Button) dialog.findViewById(R.id.mail);
		Whatsapp = (Button) dialog.findViewById(R.id.whatsapp);
		sms = (Button) dialog.findViewById(R.id.sms);
		closebtn = (ImageView) dialog.findViewById(R.id.closebtn);

		closebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		fbIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postOnWall("Event : " + data.get("Name") + "Event Detail is:"
						+ Html.fromHtml(data.get("detail"))
						+ ",Event type is : " + data.get("Title")
						+ ",Start Date is : " + data.get("eventdate")
						+ ",End Date is : " + data.get("eventenddate")
						+ ",Start Time : " + data.get("eventstarttime")
						+ ",End Time is : " + data.get("eventendtime"));
			}
		});
		sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendsms();
			}
		});
		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(EventDetailActivity.this,
						TwitterDialog.class);
				intent1.putExtra(
						"twturl",
						"" + urlTwitter + "Event Name is:" + data.get("Name")
								+ ",Event Detail is:"
								+ Html.fromHtml(data.get("detail"))
								+ ",Event type is:" + data.get("Title")
								+ ",Start Date is:" + data.get("eventdate")
								+ ",End Date is:" + data.get("eventenddate")
								+ ",Start Time:" + data.get("eventstarttime")
								+ ",End Time is:" + data.get("eventendtime"));
				intent1.putExtra("title", "Twitter");
				startActivity(intent1);

			}
		});
		Mail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareEmail();
			}
		});
		Whatsapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareWhatsApp(EventDetailActivity.this, data.get("Name"));
			}
		});

		dialog.show();
	}

	public void sendsms() {
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_TEXT,
				"Event Name is:" + data.get("Name") + "\n Event Detail is:"
						+ Html.fromHtml(data.get("detail"))
						+ "\n Event type is:" + data.get("Title")
						+ "\n Start Date is:" + data.get("eventdate")
						+ "\n End Date is:" + data.get("eventenddate")
						+ "\n Start Time:" + data.get("eventstarttime")
						+ "\n End Time is:" + data.get("eventendtime"));
		i.setType("text/plain");
		startActivity(i);

	}

	public void shareEmail() {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, data.get("Name"));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, data.get("Title"));
		emailIntent.putExtra(
				Intent.EXTRA_TEXT,
				"Event Name is:" + data.get("Name") + " Event Detail is:"
						+ Html.fromHtml(data.get("detail")) + " Event type is:"
						+ data.get("Title") + " Start Date is:"
						+ data.get("eventdate") + " End Date is:"
						+ data.get("eventenddate") + " Start Time:"
						+ data.get("eventstarttime") + " End Time is:"
						+ data.get("eventendtime"));
		try {
			startActivity(emailIntent);
			Log.i("Finished sending email...", "");

		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(EventDetailActivity.this,
					"There is no email client installed.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public static void shareWhatsApp(Activity appActivity, String texto) {

		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");
		sendIntent.putExtra(
				Intent.EXTRA_TEXT,
				"Event Name is:" + data.get("Name") + ",Event Detail is:"
						+ Html.fromHtml(data.get("detail")) + ",Event type is:"
						+ data.get("Title") + ",Start Date is:"
						+ data.get("eventdate") + ",End Date is:"
						+ data.get("eventenddate") + ",Start Time:"
						+ data.get("eventstarttime") + ",End Time is:"
						+ data.get("eventendtime"));
		PackageManager pm = appActivity.getApplicationContext()
				.getPackageManager();
		final List<ResolveInfo> matches = pm.queryIntentActivities(sendIntent,
				0);
		boolean temWhatsApp = false;
		for (final ResolveInfo info : matches) {
			if (info.activityInfo.packageName.startsWith("com.whatsapp")) {
				final ComponentName name = new ComponentName(
						info.activityInfo.applicationInfo.packageName,
						info.activityInfo.name);
				sendIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				sendIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				sendIntent.setComponent(name);
				temWhatsApp = true;
				break;
			}
		}

		if (temWhatsApp) {
			appActivity.startActivity(sendIntent);
		} else {
			Toast.makeText(appActivity,
					"Sorry, not install WhatsApp in your device.",
					Toast.LENGTH_LONG).show();
		}
	}

	private void attendeventloginapi() {
		String jsn = "[{\"" + "eadEvtIdi" + "\":" + "\"" + data.get("eventid")
				+ "\"" + ",\"" + "eadMemId" + "\":" + "\""
				+ prefrence.getString("MemID", "") + "\"" + ",\""
				+ "eadAttendeeStatus" + "\":" + "\"" + attentAStatus + "\""
				+ "}]";

		String url = Constants.SERVER_URL + "action=AttendEvent";
		new post_async(EventDetailActivity.this, "AttendEvent").execute(url,
				jsn);

	}

	public void responseofEvents(String resultString) {
		try {
			JSONArray jarray = new JSONArray(resultString);
			JSONObject jobj = jarray.getJSONObject(0);

			String evtid = jobj.getString("eadId");
			String attendstatus = jobj.getString("eadAttendeeStatus");
			editor.putString("eadID", evtid);
			editor.putString("Status", attendstatus);
			editor.commit();
			if (jobj.getString("eadId").toString().equalsIgnoreCase("0")) {
				Toast.makeText(EventDetailActivity.this, "" + attendstatus,
						1000).show();

			}

		}

		catch (Exception e) {
		}

	}

	private long timeInMillis(String dateStr, String time) {
		Calendar calendar = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateInString = dateStr + " " + time;
			Date date = sdf.parse(dateInString);

			calendar.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();

	}

	public void postOnWall(String msg) {

		String access_token = prefrence.getString("access_token", null);
		long expires = prefrence.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (facebook.isSessionValid()) {
			try {
				Log.e("System out", "dfhskhf..." + msg);
				Bundle parameters = new Bundle();
				parameters.putString("name", data.get("Name"));
				parameters.putString("caption", getString(R.string.app_name));
				parameters.putString("description", msg);
				parameters.putString("link",
						"https://developers.facebook.com/android");
				parameters.putString("picture", "");

				facebook.dialog(EventDetailActivity.this, "stream.publish",
						parameters, new DialogListener() {
							public void onComplete(Bundle values) {
								if (values.isEmpty()) {
								} else {
									Toast.makeText(EventDetailActivity.this,
											"share successfully!", 3000).show();
								}
								setResult(RESULT_OK);
							}

							public void onFacebookError(FacebookError error) {
							}

							public void onError(DialogError e) {
							}

							public void onCancel() {
								EventDetailActivity.this.finish();
							}
						});
			} catch (Exception e) {
			}
		} else {
			new FbLoginCommon(EventDetailActivity.this, msg);

		}
	}

	class MyObserver extends ContentObserver {
		public MyObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			this.onChange(selfChange, null);

		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {

		}
	}
}
