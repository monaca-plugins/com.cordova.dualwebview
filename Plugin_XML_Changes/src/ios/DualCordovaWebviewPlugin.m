//
//  DualCordovaWebviewPlugin.m
//  DualCordovaWebviewPlugin
//
//  Created by MountainLion on 14/05/14.
//
//

#import "DualCordovaWebviewPlugin.h"

@interface DualCordovaWebviewPlugin()

@property (readwrite, assign) BOOL deviceready;
@property (readwrite, assign) BOOL loadUrl;



@property (readonly, nonatomic, retain) NSMutableArray* eventQueue;

@end

@implementation DualCordovaWebviewPlugin
@synthesize webview,webviewHeight,deviceready,eventQueue,loadUrl;


-(void)deviceready:(CDVInvokedUrlCommand*)command
{
    self.webview = [[UIWebView alloc]init];
    self.webview .delegate = self;
    self.webview.tag = 100;
    
    deviceready = YES;
    for (NSString* js in eventQueue) {
        [self.commandDelegate evalJs:js];
    }
    
    [eventQueue removeAllObjects];
}

//***** add subview ****//
-(void)addSubview:(CDVInvokedUrlCommand *)command
{
    NSMutableDictionary* properties = [command.arguments objectAtIndex:0];
    self.position = [properties objectForKey:@"position"];
    BOOL overlay = [[properties objectForKey:@"overlay"] boolValue];
    NSString *url = [properties objectForKey:@"href"];
    webviewHeight = [[properties objectForKey:@"height"]floatValue];
    
    if (overlay == true)
    {
        if ([self.position isEqualToString:@"top"])
        {
            [self.webView setFrame:CGRectMake(0, webviewHeight+20, 320, 548-webviewHeight)];
            [self.webview setFrame:CGRectMake(0, 20, 320, webviewHeight)];
            [webview setBackgroundColor:[UIColor clearColor]];
            NSURL* nsUrl = [NSURL URLWithString:url];
            NSURLRequest* request = [NSURLRequest requestWithURL:nsUrl cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:30];
            [self.webview  loadRequest:request];
            [self.viewController.view addSubview:self.webview];
        }
        else
        {
            [self.webView setFrame:CGRectMake(0, 20, 320, 548 - webviewHeight)];
            [self.webview setFrame:CGRectMake(0, 548-webviewHeight, 320, webviewHeight)];
            [self.webview  setBackgroundColor:[UIColor clearColor]];
            NSURL* nsUrl1 = [NSURL URLWithString:url];
            NSURLRequest* request1 = [NSURLRequest requestWithURL:nsUrl1 cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:30];
            [self.webview  loadRequest:request1];
            [self.viewController.view addSubview:self.webview];
        }
    }
    else{
        [self.webView setFrame:CGRectMake(0, 20, 320, 548)];
        self.webView.delegate = self;
        
        NSURL* nsUrl = [NSURL URLWithString:url];
        NSURLRequest* request = [NSURLRequest requestWithURL:nsUrl cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:30];
        [self.webView loadRequest:request];
        
    }
    
}

//***** show subview ****//
-(void)showSubview:(CDVInvokedUrlCommand *)command{
    
    if ([self.position isEqualToString:@"top"])
    {
        [self.webview  setHidden:NO];
        [self.webView setFrame:CGRectMake(0, webviewHeight+20, 320, 548-webviewHeight)];
        self.webview = [[UIWebView alloc] initWithFrame:CGRectMake(0, 20, 320, webviewHeight)];
    }
    else{
        
        [self.webview  setHidden:NO];
        [self.webView setFrame:CGRectMake(0, 20, 320, 488 - webviewHeight)];
        [self.webview setFrame:CGRectMake(0, 548-webviewHeight, 320, webviewHeight)];
    }
}

//***** hide subview ****//
-(void)hideSubview:(CDVInvokedUrlCommand *)command{
    
    if ([self.position isEqualToString:@"top"])
    {
        [self.webview  setHidden:YES];
        [self.webView setFrame:CGRectMake(0, 20, 320, 548)];
        [self.viewController.view bringSubviewToFront:self.webView];
    }
    else{
        
        [self.webview  setHidden:YES];
        [self.webView setFrame:CGRectMake(0, 20, 320, 548)];
        [self.viewController.view bringSubviewToFront:self.webView];
        
    }
}

//***** load url in subview ****//
-(void)loadUrlInSubview:(CDVInvokedUrlCommand *)command{
    
    NSMutableDictionary* properties = [command.arguments objectAtIndex:0];
    
    NSString *url = [properties objectForKey:@"url"];
    
    NSURL* nsUrl = [NSURL URLWithString:url];
    NSURLRequest* request = [NSURLRequest requestWithURL:nsUrl cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:30];
    [self.webview loadRequest:request];
    
    loadUrl = YES;
    alertDisplayed = NO;
}


//***** event gets fired when page in subview is loaded ****//
- (void) fireEvent:(NSString*)event  message:(NSString *)message
{
    NSString* params = [NSString stringWithFormat:
                        @"\'%@\'",
                        message];
    
    NSString* js = [NSString stringWithFormat:
                    @"window.DualwebView.on%@(%@)",
                    event, params];
    
    if (deviceready) {
        [self.commandDelegate evalJs:js];
    } else {
        [self.eventQueue addObject:js];
    }
}

#pragma mark UIWebview Delegate

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    
    if (webView.tag == 100)
    {
        [self fireEvent:@"Navigation" message:webview.request.URL.absoluteString];
        
    }
    
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:[NSString stringWithFormat:@"%@",error.description] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alert show];
    
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if (navigationType == UIWebViewNavigationTypeLinkClicked)
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"" message:@"Link Clicked" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    }
    return true;
}

#pragma mark -


@end
