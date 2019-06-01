package com.example.saugandhinternassignment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class VideoActivity extends AppCompatActivity {

    Camera c;
    CameraPreview cp;
    TextView captureButton;
    MediaRecorder mr;
    private boolean isRecording = false;
    private static SQLite sqLite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        sqLite=new SQLite(getApplicationContext());
        c = getCameraInstance();
        c.setDisplayOrientation(90);
        cp = new CameraPreview(this, c);
        FrameLayout preview = findViewById(R.id.camera_preview2);
        preview.addView(cp);





// Add a listener to the Capture button
        captureButton = findViewById(R.id.button_capture2);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isRecording) {

                            mr.stop();
                            releaseMediaRecorder();
                            c.lock();


                            captureButton.setText("Start");

                            isRecording = false;
                            startActivity(new Intent(VideoActivity.this, MainActivity.class));
                            finish();
                        } else {

                            if (prepareVideoRecorder()) {

                                mr.start();


                                captureButton.setText("Stop");
                                isRecording = true;
                            } else {

                                releaseMediaRecorder();

                            }
                        }
                    }
                }
        );


    }


        @Override
        protected void onPause() {
            super.onPause();
            releaseMediaRecorder();
            releaseCamera();
        }

        private void releaseMediaRecorder(){
            if (mr != null) {
                mr.reset();
                mr.release();
                mr = null;
                c.lock();
            }
        }

        private void releaseCamera(){
            if (c != null){
                c.release();
                c = null;
            }
        }

    private boolean prepareVideoRecorder(){

        c = getCameraInstance();
        c.setDisplayOrientation(90);
        mr = new MediaRecorder();


        c.unlock();
        mr.setCamera(c);


        mr.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mr.setVideoSource(MediaRecorder.VideoSource.CAMERA);


        mr.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));


        mr.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());


        mr.setPreviewDisplay(cp.getHolder().getSurface());


        try {
            mr.prepare();
        } catch (IllegalStateException e) {
            Log.d("TAG", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("TAG", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){

        }
        return c;
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
            sqLite.insertClass("Video",1, mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
