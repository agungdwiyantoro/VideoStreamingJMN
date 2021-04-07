package com.example.mycomputer.videostreamingjmn;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by My Computer on 6/19/2016.
 */

//---- Activity untuk streaming Video
public class Video extends ActionBarActivity {

    private ProgressDialog progressDialog;
    private VideoView videoView;
    private AlertDialog.Builder alertDialog;
    private Toast exit,paused;



    String link;
    int iClicks;

    private boolean pressBACKOnce = false;

    private int stopPosition;

    private Animation pause_ani, pause_ani_out;

    ImageView mImageViewFilling;

    @Override
    public void onBackPressed() {

        if(pressBACKOnce){
            super.onBackPressed();
            videoView.pause();
            progressDialog.dismiss();
            finish();

        }

        this.pressBACKOnce = true;


        exit = Toast.makeText(Video.this, "Tap BACK twice to exit",Toast.LENGTH_SHORT);
        exit.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pressBACKOnce = false;
            }
        },1500);
    }

    public void errorDialog(){
        alertDialog = new AlertDialog.Builder(Video.this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("An error has occured");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        //  Log.d(TAG, "onPause called");
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.d(TAG, "onResume called");

        videoView.seekTo(stopPosition);
        videoView.start(); //Or use resume() if it doesn't work. I'm not sure
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        videoView = (VideoView) findViewById(R.id.Video_View);

        progressDialog = new ProgressDialog(Video.this);
        progressDialog.setTitle("Video Buffering");
        progressDialog.setMessage("Video is loading");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        exit = new Toast(Video.this);
        paused = new Toast(Video.this);

        link = getIntent().getStringExtra("link");

        pause_ani = AnimationUtils.loadAnimation(Video.this,R.anim.fadein);
        pause_ani_out = AnimationUtils.loadAnimation(Video.this,R.anim.fadeout);

        mImageViewFilling = (ImageView) findViewById(R.id.animasi);


        /*
        final Toast tooLong = Toast.makeText(Video.this, "The server has a slow response press BACK to return",Toast.LENGTH_SHORT);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        iClicks = iClicks + 1;

                        if (iClicks > 20 && iClicks < 25) {

                        }

                    }
                });

            }
        }, 0, 1000);

*/
        if (savedInstanceState != null)
        {
            stopPosition = savedInstanceState.getInt("position");
        }


        try {
            Uri video = Uri.parse(link);
            videoView.setVideoURI(video);

        }
        catch(Exception e){
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
        videoView.requestFocus();


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                //timer.cancel();
                // tooLong.cancel();
                if(LoginClasses.SaveSharedPreference.getAnimasi(Video.this)) {
                    mImageViewFilling.setVisibility(VISIBLE);
                    ((AnimationDrawable) mImageViewFilling.getBackground()).start();
                }
                videoView.start();


            }

        });



        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressDialog.dismiss();
                // tooLong.cancel();
                videoView.stopPlayback();
                errorDialog();

                return true;
            }
        });



        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.stopPlayback();
                finish();
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(videoView.isPlaying()) {



                    videoView.pause();

                    paused = Toast.makeText(Video.this,"Paused",Toast.LENGTH_SHORT);
                    paused.show();




                    return false;
                }
                else{

                    videoView.start();
                    return false;
                }

            }
        });


        mImageViewFilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginClasses.SaveSharedPreference.setAnimasi(Video.this,false);
                mImageViewFilling.setVisibility(INVISIBLE);
                ((AnimationDrawable) mImageViewFilling.getBackground()).stop();
            }
        });

    }

}






