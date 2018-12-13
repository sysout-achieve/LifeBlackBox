package com.recorder_mora.lifeblackbox;

import android.Manifest;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    MediaRecorder recorder = new MediaRecorder();
    MediaRecorder mRecorder = new MediaRecorder();
    Button start, stop, btn_list;
    public final static String mPath =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/record/lifeBB";
    boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.bt_start);
//        start.setOnClickListener(this);
        stop = findViewById(R.id.bt_stop);
        btn_list = findViewById(R.id.btn_list);
//        stop.setOnClickListener(this);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording == false) {
                    initAudioRecorder();
                    mRecorder.start();
                    isRecording = true;
                    start.setText("Stop Recording");
                } else {
                    mRecorder.stop();
                    isRecording = false;
                    start.setText("Start Recording");
                }
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordListActivity.class);
                startActivity(intent);
            }
        });

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

//    public void startRec() {
//        try {
//            File file = Environment.getExternalStorageDirectory();
//            //갤럭시 S4기준으로 /storage/emulated/0/의 경로를 갖고 시작한다.
//            String path = file.getAbsolutePath() + "파일 경로 ";
//
//            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            //첫번째로 어떤 것으로 녹음할것인가를 설정한다. 마이크로 녹음을 할것이기에 MIC로 설정한다.
//            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            //이것은 파일타입을 설정한다. 녹음파일의경우 3gp로해야 용량도 작고 효율적인 녹음기를 개발할 수있다.
//            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            //이것은 코덱을 설정하는 것이라고 생각하면된다.
//            recorder.setOutputFile(path);
//            //저장될 파일을 저장한뒤
//            recorder.prepare();
//            recorder.start();
//            //시작하면된다.
//            Toast.makeText(this, "start Record", Toast.LENGTH_LONG).show();
//        } catch (IllegalStateException e) {
//        // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//        // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public void initAudioRecorder() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        Log.d("TAG", "file path is " + mPath);
//        File dir = new File(context.getFilesDir(), "폴더명");
//        if(!mPath.exists()){
//            mPath.mkdirs();
//        }



        mRecorder.setOutputFile(mPath);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void stopRec() {
//        recorder.stop();
//        //멈추는 것이다.
//        recorder.release();
//        Toast.makeText(this, "stop Record", Toast.LENGTH_LONG).show();
//    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.bt_start) {
//            startRec();
//        } else if (v.getId() == R.id.bt_stop) {
//            stopRec();
//        }
//    }
}


