package com.lwansbrough.ReactCamera;

import android.content.Context;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;
import com.facebook.react.uimanager.ReactProp;
import com.facebook.react.uimanager.ViewProps;

import android.hardware.Camera;
import android.widget.Toast;
import android.util.Log;
import javax.annotation.Nullable;

public class ReactCameraManager extends SimpleViewManager<ReactCameraView> {

    public static final String REACT_CLASS = "ReactCameraView";
    private Camera camera = null;
    private ThemedReactContext context;
    private ReactApplicationContext reactApplicationContext;

    private CameraInstanceManager cameraInstanceManager;

    public ReactCameraManager(CameraInstanceManager cameraInstanceManager) {
        this.cameraInstanceManager = cameraInstanceManager;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = "type")
    public void setType(ReactCameraView cameraView, @Nullable String type) {
        Log.v("ReactCameraManager", "setType");
        cameraView.updateCamera(cameraInstanceManager.getCamera(type));
    }

    @Override
    public ReactCameraView createViewInstance(ThemedReactContext context) {
        ReactCameraView view = new ReactCameraView(context, cameraInstanceManager);
        this.context = context;
        return view;
    }

    @Override
    public void updateView(final ReactCameraView view, final CatalystStylesDiffMap props) {
        super.updateView(view, props);
        view.maybeUpdateView();
    }
}
