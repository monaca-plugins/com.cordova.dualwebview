var addDualWebView  = {
    
    deviceready:function(){
        
        cordova.exec(null, null, 'DualWebviewPlugin', 'deviceready', []);
    },
    
    addSubview:function(callback,options) {
        
        cordova.exec(callback, null, 'DualWebviewPlugin', 'addSubview', [options]);
    }
    
};