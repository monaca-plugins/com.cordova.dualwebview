//
//  DualCordovaWebviewPlugin.m
//  DualCordovaWebviewPlugin
//
//  Created by MountainLion on 14/05/14.
//
//

#import "DualCordovaWebviewPlugin.h"

@implementation DualCordovaWebviewPlugin


-(void)deviceready:(CDVInvokedUrlCommand*)command
{
    self.webView.delegate = self;
   
    
    NSString* url = @"http://www.yahoo.com";
    NSURL* nsUrl = [NSURL URLWithString:url];
    NSURLRequest* request = [NSURLRequest requestWithURL:nsUrl cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:30];
    
    [self.webView loadRequest:request];

}



-(void)addSubview:(CDVInvokedUrlCommand *)command
{
    
    NSMutableDictionary* properties = [command.arguments objectAtIndex:0];
    
     NSString* position = [properties objectForKey:@"position"];
    NSString* overlay = [properties objectForKey:@"overlay"];
    
    if ([overlay isEqualToString:@"true"])
    {
        if ([position isEqualToString:@"top"])
        {
            
            [self.webView setFrame:CGRectMake(0, 120, 320, 460)];
            UIWebView *webview = [[UIWebView alloc] initWithFrame:CGRectMake(0, 20, 320, 60)];
            
            [webview setBackgroundColor:[UIColor clearColor]];
            NSString* url1 = @"http://www.google.com";
            NSURL* nsUrl1 = [NSURL URLWithString:url1];
            NSURLRequest* request1 = [NSURLRequest requestWithURL:nsUrl1 cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:30];
            
            [webview loadRequest:request1];
            [self.viewController.view addSubview:webview];
            
            
        }
        else{
            
            [self.webView setFrame:CGRectMake(0, 20, 320, 460)];
            UIWebView *webview = [[UIWebView alloc] initWithFrame:CGRectMake(0, 480, 320, 60)];
            
            [webview setBackgroundColor:[UIColor clearColor]];
            NSString* url1 = @"http://www.google.com";
            NSURL* nsUrl1 = [NSURL URLWithString:url1];
            NSURLRequest* request1 = [NSURLRequest requestWithURL:nsUrl1 cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:30];
            
            [webview loadRequest:request1];
            [self.viewController.view addSubview:webview];
            
            
        }
        

    }
    else{
    
         [self.webView setFrame:CGRectMake(0, 0, 320, 520)];
    
    }
    
    
}

@end
