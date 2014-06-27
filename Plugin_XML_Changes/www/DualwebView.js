cordova.define("org.apache.cordova.DualWebView.DualwebView", function(require, exports, module) {
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
               
               addDualWebView.prototype.loadUrlInSubview = function(callback,options) {
               cordova.exec(callback, null, 'DualWebViewPlugin', 'loadUrlInSubview', [options]);
               };

               addDualWebView.prototype.onNavigation = function(message) {};
               
               var dualWebview = new addDualWebView();
               module.exports = dualWebview;
               
               
               });
