package com.recorder_mora.lifeblackbox;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    MediaRecorder recorder = new MediaRecorder();
    MediaRecorder mRecorder = new MediaRecorder();
    Button start, stop, btn_list;
    public final static String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record/lifebb";
    boolean isRecording = false;
    NotificationManager manager;

    String recordName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.bt_start);
//        start.setOnClickListener(this);
        stop = findViewById(R.id.bt_stop);
        btn_list = findViewById(R.id.btn_list);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationChannel.setDescription("channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.GREEN);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
//            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }

//        stop.setOnClickListener(this);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording == false) {
                    initAudioRecorder();
                    mRecorder.start();
                    isRecording = true;
                    setNotiIcon();
                    start.setText("Stop Recording");
                } else {
                    mRecorder.stop();
                    isRecording = false;
                    manager.cancel(0);
                    start.setText("Start Recording");
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotiIcon();
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording){
                    mRecorder.reset();
                    isRecording = false;
                }
                start.setText("Start Recording");
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

    public static void createNewDirectory(String name) {
        // create a directory before creating a new file inside it.
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), name);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void setNotiIcon() {
        //알림(Notification)을 관리하는 NotificationManager 얻어오기
        PendingIntent pendingIntent = PendingIntent.getActivity(
                MainActivity.this
                , 0
                , new Intent(getApplicationContext(), MainActivity.class)
                ,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_radio_button_checked)
                .setContentTitle("Listening")
                .setOngoing(true)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        manager.notify(0, mBuilder.build());
    }

    public void initAudioRecorder() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmm");

        recordName = sdf.format(date);
//        recordName = "test";

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        Log.d("TAG", "file path is " + recordName);
//        File dir = new File(context.getFilesDir(), "폴더명");
//        if(!mPath.exists()){
//            mPath.mkdirs();
//        }
        createNewDirectory("/record/lifebb");
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record/lifebb/";

        mRecorder.setOutputFile(dir + recordName + ".mp4");

        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override

    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    protected void onDestroy() {
        if(isRecording){
            mRecorder.stop();
            manager.cancel(0);
            isRecording = false;
        }
        super.onDestroy();
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


