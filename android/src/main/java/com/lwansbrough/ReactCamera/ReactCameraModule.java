package com.lwansbrough.ReactCamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Base64;
import android.widget.Toast;
import android.util.Log;

import android.os.Environment;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        camera.startPreview();
        camera.takePicture(null, null, null, new PictureTakenCallback(options, callback, reactContext));
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

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.startPreview();

            switch(options.getString("target")) {
                case "base64":
                    Bitmap bitmap = getBitmapFromData(data, camera, options);
                    byte[] byteArray;
                    ByteArrayOutputStream stream;
                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    callback.invoke(null, encoded);
                break;
                case "gallery":
                    Bitmap gBitmap = getBitmapFromData(data, camera, options);
                    Media.insertImage(reactContext.getContentResolver(), gBitmap, options.getString("title"), options.getString("description"));
                    callback.invoke();
                break;
                case "disk":
                    File pictureFile = getOutputMediaFile();
                    if (pictureFile == null){
                        callback.invoke("directory error");
                        return;
                    }
                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(data);
                        fos.close();
                        callback.invoke(null, pictureFile.getAbsolutePath());
                    } catch (Exception e) {
                        callback.invoke(e.getMessage());
                        e.printStackTrace();
                    }
                break;
            }
        }
    }

    private File getOutputMediaFile(){
        try {
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/rncamera");
            myDir.mkdirs();
            if(myDir.exists())
                Log.v("camera", "directory created");
            else
                Log.v("camera", "directory still not created");
            File mediaFile;
            mediaFile = new File(myDir, "IMG_"+ timeStamp + ".jpg");
            mediaFile.createNewFile( );
            if(mediaFile.exists())
                Log.v("camera", "file created now");
            else
                Log.v("camera", "file still not created");

            if(mediaFile.isDirectory())
                Log.v("camera", "is directory");
            else
                Log.v("camera", "is file");
            Log.v("camera", mediaFile.getAbsolutePath());
            return mediaFile;
        }
        catch(SecurityException e) {
            Log.v("camera", e.getMessage());
            return null;
        }
        catch(IOException e) {
            Log.v("camera", e.getMessage());
            return null;
        }
    }

    private Bitmap getBitmapFromData(byte[] data, Camera camera, ReadableMap options){
        int cameraOrientation = cameraInstanceManager.getCameraOrientation(camera);
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = options.getInt("sampleSize");
        Bitmap bitmap = RotateBitmap(BitmapFactory.decodeByteArray(data, 0, data.length, bitmapOptions), 90);
        return bitmap;
    }

    private Bitmap RotateBitmap(Bitmap original, int deg){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)deg);
        return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
    }
}
