package com.example.saugandhinternassignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class FinalActivity extends AppCompatActivity {

    SQLite sqLite;
    VideoView view;
    ImageView pauseplay;
    ImageView photo;
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        photo=findViewById(R.id.imageView);
        pauseplay=findViewById(R.id.pauseplay);
        sqLite= new SQLite(getApplicationContext());
       // Toast.makeText(this, sqLite.getpath("Video") , Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, sqLite.getpath("Image"), Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, sqLite.getpath("Image2") , Toast.LENGTH_SHORT).show();


        File imgFile = new  File(sqLite.getpath("Image"));

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //RotateBitmap(myBitmap, 90f);

            photo.setRotation(90f);
            photo.setImageBitmap(myBitmap);

        }
        view = findViewById(R.id.videoView);
        String path = sqLite.getpath("Video");
        view.setVideoURI(Uri.parse(path));
        view.setRotation(90f);
        view.requestFocus();
        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
            }
        });
        String path2 =sqLite.getpath("Audio");
         player = new MediaPlayer();

        try {
            player.setDataSource(path2);
            player.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception of type : " + e.toString());
            e.printStackTrace();
        }
        final boolean[] pauseplaybtn = {false};
        pauseplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pauseplaybtn[0])
                {
                    view.start();
                    player.start();
                    pauseplay.setImageResource(R.drawable.pse);
                    pauseplaybtn[0] =true;
                }
                else
                {
                    view.pause();
                    player.pause();
                    pauseplay.setImageResource(R.drawable.play);
                    pauseplaybtn[0]=false;
                }

            }
        });




    }
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FinalActivity.this, MainActivity.class));
        player.pause();
    }
}
