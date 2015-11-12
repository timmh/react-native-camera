package com.lwansbrough.ReactCamera;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

public class ReactCameraPackage implements ReactPackage {

    private CameraInstanceManager cameraInstanceManager;
    private Activity mainActivity;

    public ReactCameraPackage(Activity mainActivity) {
        this.cameraInstanceManager = new CameraInstanceManager(mainActivity);
        this.mainActivity = mainActivity;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<NativeModule>();
        modules.add(new ReactCameraModule(reactContext, cameraInstanceManager));
        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        List<ViewManager> viewManagers = new ArrayList<ViewManager>();
        viewManagers.add(new ReactCameraManager(cameraInstanceManager));
        return viewManagers;
    }
}
