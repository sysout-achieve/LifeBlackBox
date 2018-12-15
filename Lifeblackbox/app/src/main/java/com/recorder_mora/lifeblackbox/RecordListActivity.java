package com.recorder_mora.lifeblackbox;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
//    TextView fileList;
    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    RecyclerView recy_recordlist;
    private ArrayList<VoiceRecItem> voiceRecItems;
    VoiceRecAdapter voiceRecAdapter;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record/lifebb/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        mBtPlay = (Button) findViewById(R.id.bt_play);
//        fileList = findViewById(R.id.fileList);
        File directory = new File(path);
        File[] files = directory.listFiles();
        List<String> filesNameList = new ArrayList<>();
        mPlayer = new MediaPlayer();
        recy_recordlist = (RecyclerView) findViewById(R.id.recy_recordlist);
        recy_recordlist.setLayoutManager(new LinearLayoutManager(this));
        voiceRecItems = new ArrayList<>();
        voiceRecAdapter = new VoiceRecAdapter(getApplicationContext(), voiceRecItems, mPlayer);
        recy_recordlist.setAdapter(voiceRecAdapter);

        for (int i=0; i < files.length; i++) {
            filesNameList.add(files[i].getName());
            voiceRecItems.add(new VoiceRecItem(i, files[i].getName(),getPlayTime(files[i].getPath()),false, files[i].getPath()));
        }

//        fileList.setText(filesNameList.toString());



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

    private String getPlayTime(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );
        long duration = timeInmillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);
        return hours + "시 " + minutes + "분 " + seconds + "초 ";
    }
}
