package com.lwansbrough.ReactCamera;

import android.hardware.Camera;
import android.view.Surface;
import android.app.Activity;

public class CameraInstanceManager {

    private int cameraCount = Camera.getNumberOfCameras();
    private Camera[] cameraInstanceList = new Camera[cameraCount];
    private Activity activity;

    public CameraInstanceManager(Activity activity) {
        this.activity = activity;
    }

    public Camera getCamera(int id) {
        Camera camera = null;
        if (id < cameraCount) {
            if (!(cameraInstanceList[id] instanceof Camera)) {
                cameraInstanceList[id] = Camera.open(id);
                camera = cameraInstanceList[id];
            } else {
                camera = cameraInstanceList[id];
            }
        }
        return camera;
    }

    public Camera getCamera(String name) {
        name = name.toLowerCase();
        int cameraId = 0;
        switch (name) {
            case "back": cameraId = 0; break;
            case "front": cameraId = 1; break;
        }
        for (int i = 0; i < cameraInstanceList.length; i++) {
            if (i != cameraId) {
                releaseCamera(cameraInstanceList[i]);
            }
        }
        return getCamera(cameraId);
    }

    public void releaseCamera(Camera camera) {
        if (camera == null) return;
        camera.release();
        cameraInstanceList[getCameraId(camera)] = null;
    }

    public int getCameraId(Camera camera) {
        for (int i = 0; i < cameraInstanceList.length; i++) {
            if (cameraInstanceList[i] == camera) {
                return i;
            }
        }
        return -1;
    }

    public int getCameraOrientation(Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(getCameraId(camera), info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int deviceRotation = 0;
        switch (rotation) {
            case Surface.ROTATION_90: deviceRotation = 90; break;
            case Surface.ROTATION_180: deviceRotation = 180; break;
            case Surface.ROTATION_270: deviceRotation = 270; break;
        }

        int cameraRotation;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraRotation = (info.orientation + deviceRotation) % 360;
            cameraRotation = (360 - cameraRotation) % 360;
        } else {
            cameraRotation = (info.orientation - deviceRotation + 360) % 360;
        }
        return cameraRotation;
    }

    public void updateCameraOrientation(Camera camera) {
        int cameraRotation = getCameraOrientation(camera);

        Camera.Parameters cameraParameters = camera.getParameters();
        camera.setDisplayOrientation(cameraRotation);
        cameraParameters.setRotation(cameraRotation);
        camera.setParameters(cameraParameters);
    }
}
