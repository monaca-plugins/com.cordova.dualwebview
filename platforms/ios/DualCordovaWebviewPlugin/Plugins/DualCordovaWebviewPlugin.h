//
//  DualCordovaWebviewPlugin.h
//  DualCordovaWebviewPlugin
//
//  Created by MountainLion on 14/05/14.
//
//

#import <Cordova/CDVPlugin.h>

@interface DualCordovaWebviewPlugin : CDVPlugin<UIWebViewDelegate>

@property(nonatomic,strong)UIButton *btnHideShowOverlay;
@property(nonatomic,strong)UIWebView *webview;
@property(nonatomic)CGFloat webviewHeight;
@property(nonatomic)NSString *position;
@property(nonatomic)BOOL hideWebviewFlag;

-(void)addSubview:(CDVInvokedUrlCommand *)command;

-(void)deviceready:(CDVInvokedUrlCommand *)command;

@end
