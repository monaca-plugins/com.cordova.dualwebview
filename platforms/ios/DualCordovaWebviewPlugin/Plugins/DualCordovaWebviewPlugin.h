//
//  DualCordovaWebviewPlugin.h
//  DualCordovaWebviewPlugin
//
//  Created by MountainLion on 14/05/14.
//
//

#import <Cordova/CDVPlugin.h>

@interface DualCordovaWebviewPlugin : CDVPlugin<UIWebViewDelegate>

-(void)addSubview:(CDVInvokedUrlCommand *)command;

-(void)deviceready:(CDVInvokedUrlCommand *)command;

@end
