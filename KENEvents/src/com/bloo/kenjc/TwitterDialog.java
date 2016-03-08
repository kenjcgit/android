package  com.bloo.kenjc;
/*
 *  This class used for display Twitter dialog which used for share aws link. 
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bloo.kenjc.R;


public class TwitterDialog extends Activity {

	private boolean loadingFinished = true;
	private boolean redirect = false;
	
    static final float[] DIMENSIONS_DIFF_LANDSCAPE = {10, 40};
    static final float[] DIMENSIONS_DIFF_PORTRAIT = {20, 30};
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;
    static final String DISPLAY_STRING = "touch";
    static final String FB_ICON = "icon.png";

    private String mUrl,title;  
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;
    private TextView mTitle;
    int w,h;
    int titleHeight,buttonHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mUrl = getIntent().getStringExtra("twturl");
        title=getIntent().getStringExtra("title");
        Display d = getWindowManager().getDefaultDisplay();
        w = d.getWidth();
        h = d.getHeight();
        
        titleHeight = 30;
        buttonHeight = 50;
        
        mSpinner = new ProgressDialog(this);
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        mContent = new LinearLayout(this);
        mContent.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        mContent.setOrientation(LinearLayout.VERTICAL);
        setUpTitle();
        setUpWebView();
       
        setContentView(mContent);
        
    }

    private void setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        mTitle = new TextView(this);
        mTitle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, titleHeight));
        mTitle.setText(title);
        mTitle.setTextColor(Color.parseColor("#38B0DE"));
        mTitle.setTypeface(null, Typeface.BOLD);
        mTitle.setBackgroundColor(Color.TRANSPARENT);        
        mContent.addView(mTitle);
    }

    private void setUpWebView() {
        mWebView = new WebView(this);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new TwitterDialog.MyWebViewClient());
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, h-titleHeight-buttonHeight-40));
        mContent.addView(mWebView);
        
        Button cancelButton = new Button(this);
        cancelButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, buttonHeight));
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setBackgroundResource(R.drawable.btn);
        	cancelButton.setText("Cancel");
        	cancelButton.setTextColor(Color.WHITE);
        	cancelButton.setOnClickListener(new View.OnClickListener()
        	{
				public void onClick(View arg0) 
				{
					setResult(100);
					TwitterDialog.this.finish();
				}			
			});
        mContent.addView(cancelButton);
    }

    private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {			
			if (!loadingFinished) {
				redirect = true;
			}
			loadingFinished = false;
			view.loadUrl(url);
			return true;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			loadingFinished = false;
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			if (!redirect) {
				loadingFinished = true;
			}
			if (loadingFinished && !redirect) {
				if (mSpinner!=null) {
					if (mSpinner.isShowing()) {
						mSpinner.dismiss();
					}
				}
				
			} else {
				redirect = false;
			}
		}
		
	}
} 