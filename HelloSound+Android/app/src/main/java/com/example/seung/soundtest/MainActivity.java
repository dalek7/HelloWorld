package com.example.seung.soundtest;

// based on https://developer.android.com/guide/topics/media/mediarecorder.html

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private static String mDirSave = null;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;

    private TextView mTextView1 = null;
    private TextView mTextView2 = null;
    private TextView mTextViewIdx = null;
    private String   mAndroidDeviceId = null;
    int mFileCnt = 0;


    // Permission stuffs
    // Changes from Android M ??
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}; // List of permissions required


    public void AskPermission()
    {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS, PERMISSION_ALL);
                return;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Do your work.
                } else {
                    Toast.makeText(this, "Until you grant the permission, we cannot proceed further", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        AskPermission();

        // Record to the external cache directory for visibility
        //String dirsave = getExternalCacheDir().getAbsolutePath();
        String dirname = "logger2017";
        mDirSave = Environment.getExternalStorageDirectory() + File.separator + "data"+ File.separator + dirname;

        File tDir = new File(mDirSave);
        try {
            tDir.mkdirs();
        }catch (Exception e) {
            e.printStackTrace();
            Dbg.out("Directory not created...");
        }

        if(tDir.exists())
        {
            System.out.println("DIR : "+tDir);
        }


        LinearLayout ll = new LinearLayout(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //params.height = 100;

        ll.setLayoutParams(params);
        ll.setOrientation(LinearLayout.VERTICAL);


        mRecordButton = new RecordButton(this);

        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        450, //ViewGroup.LayoutParams.WRAP_CONTENT
                        250,//ViewGroup.LayoutParams.WRAP_CONTENT
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        450,
                        250,
                        0));


        setContentView(ll);

        mAndroidDeviceId = GetUniqueID();
        Dbg.out(mAndroidDeviceId);
        mAndroidDeviceId = mAndroidDeviceId.substring(mAndroidDeviceId.length()-5, mAndroidDeviceId.length());
        Dbg.out(mAndroidDeviceId);

        mTextViewIdx = new TextView(this);
        mTextViewIdx.setText(mAndroidDeviceId);

        ll.addView(mTextViewIdx,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        mTextView1 = new TextView(this);
        mTextView1.setText(mDirSave);


        ll.addView(mTextView1,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        mTextView2 = new TextView(this);
        mTextView2.setText("-");
        ll.addView(mTextView2,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    void SetupPlay()
    {
        try {
            Dbg.out("Player Setting up..."+mFileName);
            mPlayer.setDataSource(mFileName);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    Log.i(LOG_TAG, "your code comes here");
                    mPlayButton.EnablePlay();

                }
            });

            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    // http://stackoverflow.com/a/28368570
    public String GetUniqueID(){
        String  myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return  myAndroidDeviceId;

    }


    // AUDIO-related
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        SetupPlay();
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void UpdateFileName() // default option
    {
        UpdateFileName("3gp");
    }

    private void UpdateFileName(String ext)
    {

        String str1 = mAndroidDeviceId+ "_" + ProcFile.MakeStringWithDateTimeShort()+String.format("_%d", mFileCnt) + "."+ ext ;
        mFileName = mDirSave + File.separator + str1;// "/audiorecordtest.3gp";

        mFileCnt++;
    }



    private void startRecording() {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        if(false) // TODO : use GUI elements !
        {
            UpdateFileName("3gp");
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        }
        else
        {
            UpdateFileName("mp3");
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }

        mTextView2.setText(mFileName);
        mRecorder.setOutputFile(mFileName);


        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;


    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording)
                {
                    mPlayButton.setEnabled(false);
                    setText("Stop recording");
                }
                else
                {
                    mPlayButton.setEnabled(true);
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }

        public void EnablePlay()
        {
            mStartPlaying = true;
            setText("Start playing");

        }
    }

}