package com.cordova.dualwebview;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
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
	protected final static String ACTION_LOAD_URL_In_SUBVIEW = "loadUrlInSubview";
	
	public static Context mContext;
	private static CordovaWebView gWebView;
	private static String POSITION = null;
	private static String TOPPOS = "top";
	private static int HEIGHT;
	private static String HREF = null;
	private static boolean OVERLAY;
	
	private static String subViewURL;
	
	private final static int TOP = 0;
	private final static int BOTTOM = 1;
	
	private static WebView subView = null;
	private static PluginResult pluginResult = null;
	private JSONObject mainObject;
	private static boolean isUrlLoad;
	
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
						mainObject = new JSONObject(args.optString(0));
						POSITION = mainObject.getString("position");
						HEIGHT = Integer.parseInt(mainObject.getString("height"));
						HREF = mainObject.getString("href");
						OVERLAY = mainObject.getBoolean("overlay");
						subViewURL = HREF;
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
		else if(action.equalsIgnoreCase(ACTION_LOAD_URL_In_SUBVIEW))
		{
			cordova.getThreadPool().execute(new Runnable() {
			public void run()
			{
				try 
				{
					mainObject = new JSONObject(args.optString(0));
					subViewURL = mainObject.getString("url");
					loadUrlInSubview(cordova,command,subViewURL);
				} catch (JSONException e) {
					
				}
			}
			});
		} 
		return true;
	}
	
	private static void sendResultBackToJavaScript(CallbackContext command)
	{
		if(pluginResult == null)
		{
			pluginResult = new PluginResult(PluginResult.Status.OK,subViewURL);
		}
		pluginResult.setKeepCallback(true);
		command.sendPluginResult(pluginResult);
		
	}
	public static void addSubview(final CallbackContext command,CordovaInterface cordova)//,final String position,final String href,final boolean overlay,final int height)
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
							subView = new WebView(mContext);
							WebSettings webSettings = subView.getSettings();
							webSettings.setJavaScriptEnabled(true);
							subView.setWebViewClient(new WebViewClient(){
								
								public boolean shouldOverrideUrlLoading(WebView view, String url) 
								{
									view.loadUrl(url);
									return true;
								};
								
								@Override
								public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
	//								Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
								}
								
								public void onPageFinished(WebView view, String url) 
								{
	//								Toast.makeText(mContext,R.string.page_load_msg,Toast.LENGTH_SHORT).show();
								};
								
							});
							
							subView.setWebChromeClient(new WebChromeClient()
							{
								public boolean shouldOverrideUrlLoading(WebView view, String url) 
								{
									view.loadUrl(url);
									return true;
								};
								public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
	//								Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
								}
								public void onPageFinished(WebView view, String url) 
								{
	//								Toast.makeText(mContext, R.string.page_load_msg,Toast.LENGTH_SHORT).show();
								};
							});
							 
							ViewGroup.LayoutParams lp = gWebView.getLayoutParams();    
							lp.height=HEIGHT;   
							subView.setLayoutParams(lp); 
							
							layout.addView(subView);
							
							subView.loadUrl(HREF);
							
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
							
							gWebView.setWebChromeClient(new WebChromeClient(){
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
	
	public static void loadUrlInSubview(CordovaInterface cordova,final CallbackContext command,final String url)
	{
		Log.i("InloadUrlInSubview" ,"loadUrlInSubview..........");
		
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
					if(layout.getVisibility() == View.VISIBLE)
					{
						isUrlLoad = false;
						subView.loadUrl(url);
						WebSettings webSettings = subView.getSettings();
						webSettings.setJavaScriptEnabled(true);
						subView.setWebViewClient(new WebViewClient(){
							
							
							public boolean shouldOverrideUrlLoading(WebView view, String url) 
							{
								view.loadUrl(url);
								return true;
							};
							
							@Override
							public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
//								Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
							}
							
							public void onPageFinished(WebView view, String url) 
							{
								if(!isUrlLoad)
								{
									sendResultBackToJavaScript(command);
									isUrlLoad = true;
								}
//								Toast.makeText(mContext,R.string.page_load_msg,Toast.LENGTH_SHORT).show();
							};
							
						});
						
						subView.setWebChromeClient(new WebChromeClient()
						{
							boolean show = false;
							public boolean shouldOverrideUrlLoading(WebView view, String url) 
							{
								view.loadUrl(url);
								return true;
							};
							public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
//								Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
							}
							public void onPageFinished(WebView view, String url) 
							{
								if(!isUrlLoad)
								{
									sendResultBackToJavaScript(command);
									isUrlLoad = true;
								}
//								Toast.makeText(mContext, R.string.page_load_msg,Toast.LENGTH_SHORT).show();
							};
							
						});
						
						
					}
				}
			 }
		 });
	}
	
	
//	public static void showSubview()
//	{
//		final ViewGroup viewgroup = (ViewGroup)gWebView.getParent();
//		if(viewgroup.getChildCount() == 1)
//		{
//			
//			 final LinearLayout layout = new LinearLayout(mContext);
//				
//			 cordova.getActivity().runOnUiThread(new Runnable() {
//					
////				    boolean isShow = true;
//				 
//					@Override
//					public void run() {
//						
////						if(isShow)
//						{
////							final Button button = new Button(mContext);
////							
////							if(isShow){
////								button.setText(R.string.btn_hide);
////							}
////							else{
////								button.setText(R.string.btn_show);
////							}
//							
////							 button.setOnClickListener(new View.OnClickListener() {
////					             public void onClick(View v) {
////					            	 if(isShow)
////					            	 {
////					            		 isShow = false;
////					            		 button.setText(R.string.btn_show);
////					            		 viewgroup.removeView(layout);
////					            	 }
////					            	 else
////					            	 {
////					            		 isShow = true;
////					            		 button.setText(R.string.btn_hide);
////					            		 if(position.equals(TOP))
////												viewgroup.addView(layout,1);
////											else
////												viewgroup.addView(layout,2);
////					            	 }
////					             }
////					         });
//							
//							WebView web = new WebView(mContext);
//							WebSettings webSettings = web.getSettings();
//							webSettings.setJavaScriptEnabled(true);
//							web.setWebViewClient(new WebViewClient(){
//								
//								public boolean shouldOverrideUrlLoading(WebView view, String url) 
//								{
//									view.loadUrl(url);
//									return true;
//								};
//								
//								@Override
//								public void onReceivedError(WebView view, int errorCode,
//										String description, String failingUrl) {
//									Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
//								}
//								
//								public void onPageFinished(WebView view, String url) 
//								{
//									Toast.makeText(mContext,R.string.page_load_msg,Toast.LENGTH_SHORT).show();
//								};
//								
//							});
//							
//							web.setWebChromeClient(new WebChromeClient()
//							{
//								public boolean shouldOverrideUrlLoading(WebView view, String url) 
//								{
//									view.loadUrl(url);
//									return true;
//								};
//								public void onReceivedError(WebView view, int errorCode,
//										String description, String failingUrl) {
//									Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
//								}
//								public void onPageFinished(WebView view, String url) 
//								{
//									Toast.makeText(mContext, R.string.page_load_msg,Toast.LENGTH_SHORT).show();
//								};
//							});
//							 
//							ViewGroup.LayoutParams lp = gWebView.getLayoutParams();    
//							lp.height=height;   
//							web.setLayoutParams(lp); 
//							
//							layout.addView(web);
//							
//							web.loadUrl(href);
//							
////							viewgroup.addView(button,0);
//							
//							if(position.equals(TOP))
//								viewgroup.addView(layout,0);
//							else
//								viewgroup.addView(layout,1);
//						}
//			
//			
//			
//		}
//	}
//		}
//	}
//	public static void hideSubview()
//	{
//		final ViewGroup viewgroup = (ViewGroup)gWebView.getParent();
//		if(viewgroup.getChildCount() == 2)
//		{
//			 final LinearLayout layout = (LinearLayout)viewgroup.getChildAt(0);
//			 viewgroup.removeView(layout);
//		}
//	}
//	
//	public static void addSubview(CallbackContext command,CordovaInterface cordova,final String position,final String href,final boolean overlay,final int height)
//	{
////		 cordova.getActivity().runOnUiThread(new Runnable() {
////				
////			    boolean isShow = true;
////			 
////				@Override
////				public void run() {
////					gWebView.setWebViewClient(new WebViewClient(){
////						
////						public boolean shouldOverrideUrlLoading(WebView view, String url) 
////						{
////							view.loadUrl(url);
////							return true;
////						};
////					});
////					
////					gWebView.setWebChromeClient(new WebChromeClient()
////					{
////						public boolean shouldOverrideUrlLoading(WebView view, String url) 
////						{
////							view.loadUrl(url);
////							return true;
////						};
////						
////					});
////				}
////		 });
//		 
//		final ViewGroup viewgroup = (ViewGroup)gWebView.getParent();
//		
//		if(viewgroup.getChildCount() == 1)
//		{
////			 final LinearLayout layout = new LinearLayout(mContext);
////			
////			 cordova.getActivity().runOnUiThread(new Runnable() {
////					
//////				    boolean isShow = true;
////				 
////					@Override
////					public void run() {
////						
//////						if(isShow)
////						{
//////							final Button button = new Button(mContext);
//////							
//////							if(isShow){
//////								button.setText(R.string.btn_hide);
//////							}
//////							else{
//////								button.setText(R.string.btn_show);
//////							}
////							
//////							 button.setOnClickListener(new View.OnClickListener() {
//////					             public void onClick(View v) {
//////					            	 if(isShow)
//////					            	 {
//////					            		 isShow = false;
//////					            		 button.setText(R.string.btn_show);
//////					            		 viewgroup.removeView(layout);
//////					            	 }
//////					            	 else
//////					            	 {
//////					            		 isShow = true;
//////					            		 button.setText(R.string.btn_hide);
//////					            		 if(position.equals(TOP))
//////												viewgroup.addView(layout,1);
//////											else
//////												viewgroup.addView(layout,2);
//////					            	 }
//////					             }
//////					         });
////							
////							WebView web = new WebView(mContext);
////							WebSettings webSettings = web.getSettings();
////							webSettings.setJavaScriptEnabled(true);
////							web.setWebViewClient(new WebViewClient(){
////								
////								public boolean shouldOverrideUrlLoading(WebView view, String url) 
////								{
////									view.loadUrl(url);
////									return true;
////								};
////								
////								@Override
////								public void onReceivedError(WebView view, int errorCode,
////										String description, String failingUrl) {
////									Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
////								}
////								
////								public void onPageFinished(WebView view, String url) 
////								{
////									Toast.makeText(mContext,R.string.page_load_msg,Toast.LENGTH_SHORT).show();
////								};
////								
////							});
////							
////							web.setWebChromeClient(new WebChromeClient()
////							{
////								public boolean shouldOverrideUrlLoading(WebView view, String url) 
////								{
////									view.loadUrl(url);
////									return true;
////								};
////								public void onReceivedError(WebView view, int errorCode,
////										String description, String failingUrl) {
////									Toast.makeText(mContext,R.string.page_load_error_msg,Toast.LENGTH_SHORT).show();
////								}
////								public void onPageFinished(WebView view, String url) 
////								{
////									Toast.makeText(mContext, R.string.page_load_msg,Toast.LENGTH_SHORT).show();
////								};
////							});
////							 
////							ViewGroup.LayoutParams lp = gWebView.getLayoutParams();    
////							lp.height=height;   
////							web.setLayoutParams(lp); 
////							
////							layout.addView(web);
////							
////							web.loadUrl(href);
////							
//////							viewgroup.addView(button,0);
////							
////							if(position.equals(TOP))
////								viewgroup.addView(layout,0);
////							else
////								viewgroup.addView(layout,1);
////						}
//					}
//				});
//			 
////			 gWebView.loadUrl("https://in.yahoo.com/?p=us");
//		}
//	}
//	
	
}