package com.example.camerahardware;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
//import android.hardware.camera2.*;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button B1,share1;
    EditText E1;
//    ViewGroup V1;
    private final static int START_DRAGGING = 0;
    private final static int STOP_DRAGGING = 1;
    private int status;
    int flag=0;
    float xAxis = 0f;
    float yAxis = 0f;
    float lastXAxis = 0f;
    float lastYAxis = 0f;
    int numberOfLines=0;
    int i=0;


    private Camera mCamera;
    private CameraPreview mCameraPreview;
    boolean cam;
    private String currentPhotoPath="default path";
//    SurfaceView preview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        B1 = (Button) findViewById(R.id.button);
        E1 = (EditText) findViewById(R.id.editTextTextPersonName);
//        E1.setVisibility(View.INVISIBLE);
        E1.setVisibility(View.GONE);
//        V1 = (RelativeLayout) findViewById(R.id.Relative);
        mCamera = getCameraInstance();
        cam = checkCameraHardware(this);
        mCameraPreview = new CameraPreview(this, mCamera);


        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_Preview);

        preview.addView(mCameraPreview);
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                i++;
//                Add_Line(i);
                if(E1.getVisibility()==View.GONE){
                    E1.setVisibility(View.VISIBLE);
                }
//                else if(E1.getVisibility()==View.VISIBLE){
//                    E1.setVisibility(View.GONE);
//                }
            }
        });
        share1 = (Button) findViewById(R.id.share);
        share1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                try {
                    saveBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });




        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                Log.d("MyCameraApp", "mcamera access"+"   "+mCamera);
                Log.d("MyCameraApp", "camera open"+"   "+cam);
//                Log.d("MyCameraApp", "path"+"   "+currentPhotoPath)

                //mCameraPreview.surfaceCreated(mCameraPreview.mSurfaceHolder);

              // mCamera.startPreview();
               mCamera.takePicture(null, null, mPicture);

                //                mCameraPreview = new CameraPreview(this ,mCamera);
                Log.d("MyCameraApp", "path"+"   "+currentPhotoPath);
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////Do something after 1000ms
//                        mCamera.startPreview();
//                    }
//                }, 1000);
//                preview.removeView(mCameraPreview);
//                preview.addView(mCameraPreview);
            }
        });
        E1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if(me.getAction()==MotionEvent.ACTION_DOWN){
                    status = START_DRAGGING;
                    final float x = me.getX();
                    final float y = me.getY();
                    lastXAxis = x;
                    lastYAxis = y;
                    v.setVisibility(View.INVISIBLE);
                }else if(me.getAction()==MotionEvent.ACTION_UP){
                    status = STOP_DRAGGING;
                    flag=0;
                    v.setVisibility(View.VISIBLE);
                }else if(me.getAction()==MotionEvent.ACTION_MOVE){
                    if (status == START_DRAGGING){
                        flag=1;
                        v.setVisibility(View.VISIBLE);
                        final float x = me.getX();
                        final float y = me.getY();
                        final float dx = x - lastXAxis;
                        final float dy = y - lastYAxis;
                        xAxis += dx;
                        yAxis += dy;
                        v.setX((int)xAxis);
                        v.setY((int)yAxis);
                        v.invalidate();
                    }
                }
                return false;
            }
        });

    }

    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     *
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Unable to aceess cameara.", Toast.LENGTH_SHORT).show();
	        setResult(RESULT_CANCELED);
			finish();
        }
        return camera;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = null;
            try {
                pictureFile = getOutputMediaFile();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Input file problem.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                e.printStackTrace();
            }
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
//                finish();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Input file problem.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "input exception0.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    };
    private File getOutputMediaFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
//    private static File getOutputMediaFile() {
//        File mediaStorageDir = new File(
//                Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                "MyCameraApp");
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d("MyCameraApp", "failed to create directory");
//                return null;
//            }
//        }
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                .format(new Date());
//        File mediaFile;
//        mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                + "IMG_" + timeStamp + ".jpg");
//        Log.d("MyCameraApp", "Path"+"   "+mediaFile);
//
//
//        return mediaFile;
//    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
    public void saveBitmap(Bitmap bitmap) throws IOException {

        File imagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageFileName = "JPEG_" + "_";
        File kiran = File.createTempFile(imageFileName,".png",imagePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(kiran);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage()+"kiafdksdnfsfksznvks", e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage()+"file not found", e);
        }
    }
}