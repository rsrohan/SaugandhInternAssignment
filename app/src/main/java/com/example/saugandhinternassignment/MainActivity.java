package com.example.saugandhinternassignment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ImageButton videobtn, audiobtn, picturebtn;
    Button btn;
    AlertDialog.Builder ad;
    String pathsave="";
    MediaRecorder mediaRecorder;
    private static SQLite sqLite;
    //boolean image=false, video=false, audio=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videobtn=findViewById(R.id.videoimage);
        audiobtn=findViewById(R.id.audioimage);
        picturebtn=findViewById(R.id.pictureimage);
        btn=findViewById(R.id.buttonfinal);
        sqLite= new SQLite(getApplicationContext());

        if(!checkpermissionfromdevice())
        {
            requestPermission();
        }

        videobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // video=true;
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
            }
        });
        picturebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //image=true;
                startActivity(new Intent(MainActivity.this, CaptureActivity.class));
            }
        });
        audiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // audio=true;
                ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("Recording...");
                ad.setMessage("Audio is being recorded. Tap the button to stop.");
                pathsave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ UUID.randomUUID().toString()+"_audio_record.3gp";
                sqLite.insertClass("Audio",1, pathsave);
                setupmediarecorder();
                try{
                    Toast.makeText(MainActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                }catch (IOException e){
                    e.printStackTrace();
                }
                ad.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaRecorder.stop();

                    }
                });
                ad.show();


            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String s= sqLite.getpath("Video");
                    String s2= sqLite.getpath("Image");
                    String s3= sqLite.getpath("Audio");
                    startActivity(new Intent(MainActivity.this, FinalActivity.class));
                }
                catch (IndexOutOfBoundsException e)
                {
                    Toast.makeText(MainActivity.this, "PLEASE DO ALL THE ABOVE PRACTICES ATLEAST ONCE BEFORE GOING TO SECOND SCREEN.", Toast.LENGTH_SHORT).show();

                }


                // Toast.makeText(MainActivity.this, "PLEASE DO ALL THE ABOVE PRACTICES ATLEAST ONCE BEFORE GOING TO SECOND SCREEN.", Toast.LENGTH_SHORT).show();


            }
        });



    }
    private void setupmediarecorder(){
        mediaRecorder= new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathsave);
    }

    private boolean checkpermissionfromdevice()
    {
        int writeexstorageresult= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recaudiores=ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int recaudiores2=ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int recaudiores3=ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return writeexstorageresult == PackageManager.PERMISSION_GRANTED && recaudiores == PackageManager.PERMISSION_GRANTED && recaudiores2 == PackageManager.PERMISSION_GRANTED && recaudiores3 == PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION
        },  1000);
    }
}
