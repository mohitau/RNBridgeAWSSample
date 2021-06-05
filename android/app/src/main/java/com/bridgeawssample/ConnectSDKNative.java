package com.bridgeawssample;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManagerListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ConnectSDKNative extends ReactContextBaseJavaModule implements DiscoveryManagerListener {

    private final ReactApplicationContext applicationContext;
    private DiscoveryManager mDiscoveryManager;
    private ConnectableDevice mDevice;

    public ConnectSDKNative(ReactApplicationContext applicationContext) {
        super(applicationContext);
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "ConnectSDKNative";
    }

    @ReactMethod
    public void callDeviceDiscovery(Callback successCallback, Callback errorCallback){
        try{
            mDiscoveryManager = DiscoveryManager.getInstance();
            mDiscoveryManager.registerDefaultDeviceTypes();
            mDiscoveryManager.addListener(this);
            mDiscoveryManager.start();
            successCallback.invoke("Called Device Discovery ");
        }catch (Exception e){
            errorCallback.invoke(e.getLocalizedMessage());
        }
    }

//    @ReactMethod
//    public void callDeviceDiscovery(String fromJSWorld, Promise promise){
//        try{
//            Log.d("ConnectSDKNative", fromJSWorld);
//            mDiscoveryManager = DiscoveryManager.getInstance();
//            mDiscoveryManager.registerDefaultDeviceTypes();
//            mDiscoveryManager.start();
//            promise.resolve("Device Discovery Completed");
//        }catch (Exception e){
//            promise.reject(e.getLocalizedMessage(),e.getMessage());
//        }
//    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(RCTNativeAppEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void onDeviceAdded(DiscoveryManager manager, ConnectableDevice device) {
        Log.i("Device",device.getFriendlyName());
        try{
            sendEvent(applicationContext,"DeviceFound", Util.convertJsonToMap(device.toJSONObject()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDeviceUpdated(DiscoveryManager manager, ConnectableDevice device) {

    }

    @Override
    public void onDeviceRemoved(DiscoveryManager manager, ConnectableDevice device) {

    }

    @Override
    public void onDiscoveryFailed(DiscoveryManager manager, ServiceCommandError error) {

    }


}
