
package com.cordova.dualwebview;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DualWebviewPlugin extends CordovaPlugin
{
	protected final static String ACTION_ADD = "addSubview";
	public static Context mContext;
	private static CordovaWebView gWebView;
	public final static String TOP = "top";
	
	public boolean isShow = true;
	
	
	
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
		gWebView.setWebViewClient(new WebViewClient(){
			
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			};
		});
		
		gWebView.setWebChromeClient(new WebChromeClient()
                                    {
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			};
			
		});
		
		final ViewGroup viewgroup = (ViewGroup)gWebView.getParent();
		
		if(viewgroup.getChildCount() == 1)
		{
            final LinearLayout layout = new LinearLayout(mContext);
			
            cordova.getActivity().runOnUiThread(new Runnable() {
                
                boolean isShow = true;
                
                @Override
                public void run() {
                    
                    if(isShow)
                    {
                        final Button button = new Button(mContext);
                        
                        if(isShow){
                            button.setText(R.string.btn_hide);
                        }
                        else{
                            button.setText(R.string.btn_show);
                        }
                        
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if(isShow)
                                {
                                    isShow = false;
                                    button.setText(R.string.btn_show);
                                    viewgroup.removeView(layout);
                                }
                                else
                                {
                                    isShow = true;
                                    button.setText(R.string.btn_hide);
                                    if(position.equals(TOP))
                                        viewgroup.addView(layout,1);
                                    else
                                        viewgroup.addView(layout,2);
                                }
                            }
                        });
                        
                        WebView web = new WebView(mContext);
                        WebSettings webSettings = web.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        web.setWebViewClient(new WebViewClient(){
                            // Overidden to avoid crashing while opening google.com
                            public boolean shouldOverrideUrlLoading(WebView view, String url)
                            {
                                view.loadUrl(url);
                                return true;
                            };
                            
                            @Override
                            public void onReceivedError(WebView view, int errorCode,
                                                        String description, String failingUrl) {
                                Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
                            }
                            
                            public void onPageFinished(WebView view, String url)
                            {
                                Toast.makeText(mContext,R.string.page_load_msg,Toast.LENGTH_SHORT).show();
                            };
                            
                        });
                        // To handle Chrome in Android
                        web.setWebChromeClient(new WebChromeClient()
                                               {
                            public boolean shouldOverrideUrlLoading(WebView view, String url) 
                            {
                                view.loadUrl(url);
                                return true;
                            };
                            public void onReceivedError(WebView view, int errorCode,
                                                        String description, String failingUrl) {
                                Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
                            }
                            public void onPageFinished(WebView view, String url) 
                            {
                                Toast.makeText(mContext, R.string.page_load_msg,Toast.LENGTH_SHORT).show();
                            };
                        });
                        
                        ViewGroup.LayoutParams lp = gWebView.getLayoutParams();    
                        lp.height=height;   
                        web.setLayoutParams(lp); 
                        
                        layout.addView(web);
                        
                        web.loadUrl(href);
                        
                        viewgroup.addView(button,0);
                        
                        if(position.equals(TOP))
                            viewgroup.addView(layout,1);
                        else
                            viewgroup.addView(layout,2);
                    }
                }
            });
            
            gWebView.loadUrl("https://in.yahoo.com/?p=us");
		}
    
	}
	
	
}