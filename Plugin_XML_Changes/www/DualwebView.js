		var exec = require("cordova/exec");

               var addDualWebView  = function(){
               };
               
               addDualWebView.prototype.deviceready = function(){
               
               cordova.exec(null, null, 'DualWebViewPlugin', 'deviceready', []);
               };
               
               addDualWebView.prototype.addSubview = function(callback,options) {
               
               cordova.exec(callback, null, 'DualWebViewPlugin', 'addSubview', [options]);
               };
               
               addDualWebView.prototype.showSubview = function(callback) {
               
               cordova.exec(callback, null, 'DualWebViewPlugin', 'showSubview', []);
               };
               
               addDualWebView.prototype.hideSubview = function(callback) {
               cordova.exec(callback, null, 'DualWebViewPlugin', 'hideSubview', []);
               };
               
               var dualWebview = new addDualWebView();
               module.exports = dualWebview;
               
               
               });
