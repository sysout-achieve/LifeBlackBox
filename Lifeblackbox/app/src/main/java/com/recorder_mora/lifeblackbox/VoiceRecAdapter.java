package com.recorder_mora.lifeblackbox;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VoiceRecAdapter extends RecyclerView.Adapter<VoiceRecAdapter.ViewHolder> {
    ArrayList<VoiceRecItem> voiceRecItems;
    Context context;
    MediaPlayer mediaPlayer;
    Activity activity;

    public VoiceRecAdapter(Activity activity, Context context, ArrayList<VoiceRecItem> voiceRecItems, MediaPlayer mediaPlayer) {
        this.activity = activity;
        this.context = context;
        this.voiceRecItems = voiceRecItems;
        this.mediaPlayer = mediaPlayer;
    }

    @NonNull
    @Override
    public VoiceRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_voice_item, parent, false);
        VoiceRecAdapter.ViewHolder vh = new VoiceRecAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VoiceRecAdapter.ViewHolder holder, final int position) {
        holder.txt_title.setText(voiceRecItems.get(position).rec_title);
        holder.txt_runtime.setText(voiceRecItems.get(position).rec_runningtime);
        if (voiceRecItems.get(position).rec_selected) {
            holder.img_box.setImageResource(R.drawable.play);
        } else {
            holder.img_box.setImageResource(R.drawable.circumference);
        }

        holder.layout_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                if (voiceRecItems.get(position).isRec_selected()) {
                    voiceRecItems.get(position).setRec_selected(false);
                } else {
                    setSelectVoiceRec(position);
                }
                notifyDataSetChanged();
            }
        });

        holder.layout_play.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callLongClickDialog(R.layout.dialog_item_longclick, position);
//                removeRecord(position);
                return false;
            }
        });
    }

    public void callLongClickDialog(final int layout, final int position) {
        final Dialog dlg = new Dialog(activity);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(layout);
        dlg.show();
        final Button btn_refactor = dlg.findViewById(R.id.btn_refactor);
        final Button btn_send = dlg.findViewById(R.id.btn_send);
        final Button btn_delete = dlg.findViewById(R.id.btn_delete);
        final Button cancelButton = dlg.findViewById(R.id.btn_cancel);

        btn_refactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendFileSNS(position);
                dlg.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDeleteDialog(R.layout.dialog_item_delete, position);
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }

    public void callDeleteDialog(final int layout, final int position) {
        final Dialog dlg = new Dialog(activity);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(layout);
        dlg.show();
        final Button okButton = dlg.findViewById(R.id.btn_ok);
        final Button cancelButton = dlg.findViewById(R.id.btn_cancel);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRecord(position);
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }

    public void removeRecord(int position){
        mediaPlayer.reset();
        File file = new File(voiceRecItems.get(position).getFilepath());
        file.delete();
        voiceRecItems.remove(position);
        notifyDataSetChanged();
    }

    public void setSelectVoiceRec(int position) {
        for (int i = 0; i < voiceRecItems.size(); i++) {
            voiceRecItems.get(i).setRec_selected(false);
        }
//        sendImplicitBroadcast();
//        killMediaPlayer();
        voiceRecItems.get(position).setRec_selected(true);
        try {
            mediaPlayer.setDataSource(voiceRecItems.get(position).filepath);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    private void sendFileSNS(int position){     // TODO : 공유하기 기능 다시 해결해야함
        Intent intent = new Intent(Intent.ACTION_SEND);
        File file = new File(voiceRecItems.get(position).filepath);
        try {
            if(file.exists()){
//                intent.setAction(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//                    Uri contentUri = FileProvider.getUriForFile(context, "com.recorder_mora.lifeblackbox.fileProvider", file);
//            intent.putExtra(Intent.EXTRA_STREAM, );
//                    intent.putExtra(Intent.EXTRA_STREAM, file.getAbsolutePath());
                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                }

                activity.startActivity(Intent.createChooser(intent, voiceRecItems.get(position).rec_title));


            } else {
                Toast.makeText(context, "파일을 확인해주세요.", Toast.LENGTH_LONG ).show();
            }
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(context, "No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }
    }

    private void sendImplicitBroadcast(Context ctxt, Intent i)
    {
        PackageManager pm=ctxt.getPackageManager();
        List<ResolveInfo> matches=pm.queryBroadcastReceivers(i, 0);
        for (ResolveInfo resolveInfo : matches) {
            Intent explicit=new Intent(i);
            ComponentName cn= new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name);
            explicit.setComponent(cn);
            ctxt.sendBroadcast(explicit);
        }
    }


    private void killMediaPlayer() {
        if(mediaPlayer != null){
            try {
                mediaPlayer.release();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return voiceRecItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_box;
        TextView txt_title, txt_runtime;
        RelativeLayout layout_play;

        public ViewHolder(View itemView) {
            super(itemView);
            img_box = itemView.findViewById(R.id.img_box);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_runtime = itemView.findViewById(R.id.txt_runtime);
            layout_play = itemView.findViewById(R.id.layout_play);
        }
    }
}

