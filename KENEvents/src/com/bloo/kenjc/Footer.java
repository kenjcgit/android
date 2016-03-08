package com.bloo.kenjc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;

public class Footer extends LinearLayout {

	TextView footereventtxt, footernotitxt, footercontacttxt, footermoretxt;
	ImageView footereventicon, footernotiicon, footercontacticon,
			footermoreicon, eventselctor, notiselctor, contactselctor,
			moreselctor;
	LinearLayout footerLinearLayout;
	FrameLayout footereventtab, footernotificationtab, footermorertab,
			footercontacttab;
	public static final int EVENT_TEXT = 100;
	public static final int NOTIFICATION_TEXT = 102;
	public static final int NOTIFICATION_BADGETEXT = 101;
	public static final int CONTACT_TEXT = 103;
	public static final int MORE_TEXT = 104;

	public static final int EVENT_SELECTOR = 5;
	public static final int NOTIFICATION_SELECTOR = 6;
	public static final int CONTACT_SELECTOR = 7;
	public static final int MORE_SELECTOR = 8;

	public static final int HOME_BG = 10;
	public static final int EVENT_BG = 11;
	public static final int NOTIFICATION_BG = 12;
	public static final int CONTACT_BG = 13;
	public static final int MORE_BG = 14;
	SharedPreferences prefrence;
	private TextView footernoticounttxt;
	private Editor editor;

	public Footer(final Context context, AttributeSet attrs) {
		super(context, attrs);
		prefrence = context.getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.footertab, null);

		footerLinearLayout = (LinearLayout) v.findViewById(R.id.mainfooter_ll);
		footereventtab = (FrameLayout) v.findViewById(R.id.eventfooter_fl);
		footernotificationtab = (FrameLayout) v
				.findViewById(R.id.notifooter_fl);
		footermorertab = (FrameLayout) v.findViewById(R.id.morefooter_fl);
		footercontacttab = (FrameLayout) v.findViewById(R.id.contactfooter_fl);

		footereventicon = (ImageView) v.findViewById(R.id.iv_events_img);
		footernotiicon = (ImageView) v.findViewById(R.id.iv_noti_img);
		footercontacticon = (ImageView) v.findViewById(R.id.iv_contact_img);
		footermoreicon = (ImageView) v.findViewById(R.id.iv_more_img);

		footereventtxt = (TextView) v.findViewById(R.id.tv_eventfooter);
		footernotitxt = (TextView) v.findViewById(R.id.tv_notifooter);
		footernoticounttxt = (TextView) v.findViewById(R.id.noticount);
		footercontacttxt = (TextView) v.findViewById(R.id.tv_contactfooter);
		footermoretxt = (TextView) v.findViewById(R.id.tv_morefooter);

		eventselctor = (ImageView) v.findViewById(R.id.event_selector);
		notiselctor = (ImageView) v.findViewById(R.id.noti_selector);
		contactselctor = (ImageView) v.findViewById(R.id.contact_selector);
		moreselctor = (ImageView) v.findViewById(R.id.more_selector);

		footereventicon.setId(EVENT_BG);
		footereventtxt.setId(EVENT_TEXT);
		footernotiicon.setId(NOTIFICATION_BG);
		footernoticounttxt.setId(NOTIFICATION_BADGETEXT);
		footernotitxt.setId(NOTIFICATION_TEXT);

		footercontacticon.setId(CONTACT_BG);
		footercontacttxt.setId(CONTACT_TEXT);
		footermoreicon.setId(MORE_BG);
		footermoretxt.setId(MORE_TEXT);
		footereventicon.setImageResource(R.drawable.footer_icon2_selected);

		footereventtxt.setTextColor(Color.parseColor("#00BFFF"));
		eventselctor.setVisibility(View.VISIBLE);

		footernoticounttxt.setText(prefrence.getString("memBadgeCount", "0"));

		if (com.fipl.kenjc.Utils.Constants.intDrawerSelection == 1) {

			footereventicon.setImageResource(R.drawable.footer_icon2_selected);

			footereventtxt.setTextColor(Color.parseColor("#00BFFF"));
			eventselctor.setVisibility(View.VISIBLE);

		}
		if (com.fipl.kenjc.Utils.Constants.intDrawerSelection == 2) {
			notiselctor.setVisibility(View.VISIBLE);

			footernotitxt.setTextColor(Color.parseColor("#00BFFF"));
			footernotiicon.setImageResource(R.drawable.footer_icon1_selected);
			footereventicon
					.setImageResource(R.drawable.footer_icon2_unselected);

			footereventtxt.setTextColor(Color.parseColor("#000000"));
			eventselctor.setVisibility(View.INVISIBLE);

		} else if (com.fipl.kenjc.Utils.Constants.intDrawerSelection == 3) {
			contactselctor.setVisibility(View.VISIBLE);

			footercontacttxt.setTextColor(Color.parseColor("#00BFFF"));
			footercontacticon.setImageResource(R.drawable.footer_icon_selected);
			footereventicon
					.setImageResource(R.drawable.footer_icon2_unselected);

			footereventtxt.setTextColor(Color.parseColor("#000000"));
			eventselctor.setVisibility(View.INVISIBLE);

		} else if (com.fipl.kenjc.Utils.Constants.intDrawerSelection == 4) {
			moreselctor.setVisibility(View.VISIBLE);

			footermoretxt.setTextColor(Color.parseColor("#00BFFF"));
			footermoreicon.setImageResource(R.drawable.footer_icon3_selected);
			footereventicon
					.setImageResource(R.drawable.footer_icon2_unselected);

			footereventtxt.setTextColor(Color.parseColor("#000000"));
			eventselctor.setVisibility(View.INVISIBLE);
		}

		footereventtab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, EventsActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				i.putExtra("Flag", "Is first");
				Constants.intDrawerSelection = 1;

				context.startActivity(i);

			}
		});
		footernotificationtab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, NotificationsActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Constants.intDrawerSelection = 2;
				context.startActivity(i);
			}
		});
		footercontacttab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(context, ContactsActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Constants.intDrawerSelection = 3;
				context.startActivity(i);

			}
		});
		footermorertab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(context, MoreActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Constants.intDrawerSelection = 4;
				context.startActivity(i);
			}
		});

		this.addView(footerLinearLayout);
	}

}
