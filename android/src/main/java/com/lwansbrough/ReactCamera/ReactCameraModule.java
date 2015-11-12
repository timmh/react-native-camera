package com.lwansbrough.ReactCamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Base64;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import android.provider.MediaStore.Images.Media;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.HashMap;

public class ReactCameraModule extends ReactContextBaseJavaModule {
    ReactApplicationContext reactContext;
    private CameraInstanceManager cameraInstanceManager;

    public ReactCameraModule(ReactApplicationContext reactContext, CameraInstanceManager cameraInstanceManager) {
        super(reactContext);
        this.reactContext = reactContext;
        this.cameraInstanceManager = cameraInstanceManager;
    }

    @Override
    public String getName() {
        return "ReactCameraModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
            final Map<String, Object> constantsAspect = new HashMap<>();
            constantsAspect.put("stretch", "stretch");
            constantsAspect.put("fit", "fit");
        constants.put("Aspect", constantsAspect);
        return constants;
    }

    @ReactMethod
    public void capture(ReadableMap options, final Callback callback) {
        Camera camera = cameraInstanceManager.getCamera(options.getString("type"));
        camera.stopPreview();
        camera.takePicture(null, null, new PictureTakenCallback(options, callback, reactContext));
    }

    private class PictureTakenCallback implements Camera.PictureCallback {
        ReadableMap options;
        Callback callback;
        ReactApplicationContext reactContext;

        PictureTakenCallback(ReadableMap options, Callback callback, ReactApplicationContext reactContext) {
            this.options = options;
            this.callback = callback;
            this.reactContext = reactContext;
        }

        private Bitmap RotateBitmap(Bitmap original, int deg)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate((float)deg);
            return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
        }

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.startPreview();

            int cameraOrientation = cameraInstanceManager.getCameraOrientation(camera);

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = options.getInt("sampleSize");
            Bitmap bitmap = RotateBitmap(BitmapFactory.decodeByteArray(data, 0, data.length, bitmapOptions), -90);

            switch(options.getString("target")) {
                case "base64":
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    callback.invoke(encoded);
                break;
                case "gallery":
                    Media.insertImage(reactContext.getContentResolver(), bitmap, options.getString("title"), options.getString("description"));
                    callback.invoke();
                break;
                case "file":
                    callback.invoke();
                break;
            }
        }
    }
}
