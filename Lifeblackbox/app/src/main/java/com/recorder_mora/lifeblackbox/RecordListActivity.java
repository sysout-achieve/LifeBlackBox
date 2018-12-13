package com.recorder_mora.lifeblackbox;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.recorder_mora.lifeblackbox.MainActivity.mPath;

public class RecordListActivity extends AppCompatActivity {
    MediaPlayer mPlayer = null;
    boolean isPlaying = false;
    Button mBtPlay = null;
    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record/lifeBB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        mBtPlay = (Button) findViewById(R.id.bt_play);
        File directory = new File(path);
        File[] files = directory.listFiles();
        List<String> filesNameList = new ArrayList<>();

        for (int i=0; i< files.length; i++) {
            filesNameList.add(files[i].getName());
        }

        mBtPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying == false) {
                    try {
                        mPlayer.setDataSource(mPath+getTime());
                        mPlayer.prepare();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    mPlayer.start();

                    isPlaying = true;
                    mBtPlay.setText("Stop Playing");
                }
                else {
                    mPlayer.stop();

                    isPlaying = false;
                    mBtPlay.setText("Start Playing");
                }
            }
        });

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                mBtPlay.setText("Start Playing");
            }
        });
    }
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


}
