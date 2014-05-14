var addDualWebView  = {
    
    deviceready:function(){
        
        cordova.exec(null, null, 'DualWebViewPlugin', 'deviceready', []);
    },
    
    addSubview:function(callback,options) {
        
        cordova.exec(callback, null, 'DualWebViewPlugin', 'addSubview', [options]);
    }
    
};