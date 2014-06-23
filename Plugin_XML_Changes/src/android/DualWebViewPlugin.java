package com.cordova.dualwebview;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DualWebViewPlugin extends CordovaPlugin
{
	protected final static String ACTION_ADD = "addSubview";
	protected final static String ACTION_SHOW= "showSubview";
	protected final static String ACTION_HIDE = "hideSubview";
	
	public static Context mContext;
	private static CordovaWebView gWebView;
	public static String POSITION = null;
	public static String TOPPOS = "top";
	public static int HEIGHT;
	public static String HREF = null;
	public static boolean OVERLAY;
	
	public final static int TOP = 0;
	public final static int BOTTOM = 1;
	
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
					System.out.println("In ACTION_ADD");
					try
					{
						JSONObject mainObject = new JSONObject(args.optString(0));
						POSITION = mainObject.getString("position");
						HEIGHT = Integer.parseInt(mainObject.getString("height"));
						HREF = mainObject.getString("href");
						OVERLAY = mainObject.getBoolean("overlay");
						
						addSubview(command,cordova);
						
						
					} catch (JSONException e) {
						
					}
				}
			});
		}
		
		else if(action.equalsIgnoreCase(ACTION_SHOW))
		{
			cordova.getThreadPool().execute(new Runnable() {
				public void run()
				{
					System.out.println("In ACTION_SHOW");
					
					showSubview(cordova);
					
				}
			});
		}
		else if(action.equalsIgnoreCase(ACTION_HIDE))
		{
			cordova.getThreadPool().execute(new Runnable() {
				public void run()
				{
					System.out.println("In ACTION_HIDE");
					
					hideSubview(cordova);
				}
			});
		}
		
		return true;
	}
	
	
	public static void addSubview(CallbackContext command,CordovaInterface cordova)//,final String position,final String href,final boolean overlay,final int height)
	{
		final ViewGroup viewgroup = (ViewGroup)gWebView.getParent();
		
		if(viewgroup.getChildCount() == 1)
		{
            final LinearLayout layout = new LinearLayout(mContext);
			
            cordova.getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    
                    if(OVERLAY)
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
                        lp.height=HEIGHT;
                        web.setLayoutParams(lp);
                        
                        layout.addView(web);
                        
                        web.loadUrl(HREF);
                        
                        if(POSITION.equals(TOPPOS))
                            viewgroup.addView(layout,0);
                        else
                            viewgroup.addView(layout,1);
                    }
                    else
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
                        gWebView.loadUrl("https://in.yahoo.com/?p=us");
                    }
                }
            });
            
		}
	}
	
	public static void showSubview(CordovaInterface cordova)
	{
		final ViewGroup viewgroup = (ViewGroup)gWebView.getParent();
		
        cordova.getActivity().runOnUiThread(new Runnable() 
                                            {
            View layout = null;
            @Override
            public void run() {
                
				if(viewgroup.getChildCount() == 2)
				{
					if(POSITION.equals(TOPPOS))
					{
						layout = (View)viewgroup.getChildAt(TOP);
					}
					else
					{
						layout = (View)viewgroup.getChildAt(BOTTOM);
					}
					
					layout.setVisibility(View.VISIBLE);
				}
            }
        });
	}
	
    
	public static void hideSubview(CordovaInterface cordova)
	{
		
		final ViewGroup viewgroup = (ViewGroup)gWebView.getParent();
		
        cordova.getActivity().runOnUiThread(new Runnable() 
                                            {
            View layout = null;
            @Override
            public void run() {
                
				if(viewgroup.getChildCount() == 2)
				{
					if(POSITION.equals(TOPPOS))
					{
						layout = (View)viewgroup.getChildAt(TOP);
					}
					else
					{
						layout = (View)viewgroup.getChildAt(BOTTOM);
					}
					
					layout.setVisibility(View.GONE);
				}
            }
        });
	}
}