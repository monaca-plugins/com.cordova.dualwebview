
var exec = require("cordova/exec");
	var addDualWebView  = function(){
	};
	
    addDualWebView.prototype.deviceready = function(){
        
        cordova.exec(null, null, 'DualWebViewPlugin', 'deviceready', []);
    };
    
    addDualWebView.prototype.addSubview = function(callback,options) {
        
        cordova.exec(callback, null, 'DualWebViewPlugin', 'addSubview', [options]);
    };
    
    var dualWebview = new addDualWebView();
	module.exports = dualWebview;
    
