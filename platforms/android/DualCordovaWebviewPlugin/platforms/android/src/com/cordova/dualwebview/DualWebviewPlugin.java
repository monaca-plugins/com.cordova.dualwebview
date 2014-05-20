package com.cordova.dualwebview;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class DualWebviewPlugin extends CordovaPlugin
{
	protected final static String ACTION_ADD = "addSubview";
	public static Context mContext;
	private static CordovaWebView gWebView;
	public final static String TOP = "top";
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView)
	{
		super.initialize(cordova, webView);
		mContext = super.cordova.getActivity().getApplicationContext();
		gWebView = webView;
	}

	
	@Override
	public boolean execute(String action, final JSONArray args,
			final CallbackContext command) throws JSONException
	{
		
		
		if (action.equalsIgnoreCase(ACTION_ADD))
		{
			cordova.getThreadPool().execute(new Runnable() {
				public void run()
				{
					
					try 
					{
						JSONObject mainObject = new JSONObject(args.optString(0));
						
						
						addSubview(command,cordova,
								   mainObject.getString("position"),
								   mainObject.getString("href"),
								   mainObject.getBoolean("overlay"),
								   Integer.parseInt(mainObject.getString("height"))
								   );
						
					} catch (JSONException e) {
						
					}
				}
			});
		} 
		return true;
	}

	
	public static void addSubview(CallbackContext command,CordovaInterface cordova,final String position,final String href,final boolean overlay,final int height)
	{
		
		final ViewGroup viewgroup = (ViewGroup)gWebView.getParent();
		
		if(viewgroup.getChildCount() == 1)
		{
			 final LinearLayout layout = new LinearLayout(mContext);
			
			 cordova.getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						if(overlay)
						{
							WebView web = new WebView(mContext);
							WebSettings webSettings = web.getSettings();
							webSettings.setJavaScriptEnabled(true);
							web.setWebViewClient(new WebViewClient(){
								
								public boolean shouldOverrideUrlLoading(WebView view, String url) 
								{
									view.loadUrl(url);
									
									return true;
								};
							});
							
							web.setWebChromeClient(new WebChromeClient()
							{
								public boolean shouldOverrideUrlLoading(WebView view, String url) 
								{
									view.loadUrl(url);
									return true;
								};
							});
							 
							ViewGroup.LayoutParams lp = gWebView.getLayoutParams();    
							lp.height=height;   
							web.setLayoutParams(lp); 
							
							layout.addView(web);
							
							web.loadUrl(href);
							
							if(position.equals(TOP))
								viewgroup.addView(layout,0);
							else
								viewgroup.addView(layout,1);
						}
					}
				});
			 
			 gWebView.loadUrl("https://in.yahoo.com/?p=us");
		}
		
		Log.i("addSubview","11111111");
	}
	
	
}