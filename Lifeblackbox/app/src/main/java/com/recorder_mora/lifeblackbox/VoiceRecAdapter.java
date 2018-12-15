package com.recorder_mora.lifeblackbox;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.recorder_mora.lifeblackbox.MainActivity.mPath;


public class VoiceRecAdapter extends RecyclerView.Adapter<VoiceRecAdapter.ViewHolder> {
    ArrayList<VoiceRecItem> voiceRecItems;
    Context context;
    MediaPlayer mediaPlayer;

    public VoiceRecAdapter(Context context, ArrayList<VoiceRecItem> voiceRecItems, MediaPlayer mediaPlayer){
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
        if(voiceRecItems.get(position).rec_selected){
            holder.img_box.setImageResource(R.drawable.play);
        } else {
            holder.img_box.setImageResource(R.drawable.circumference);
        }

        holder.layout_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voiceRecItems.get(position).isRec_selected()){
                    voiceRecItems.get(position).setRec_selected(false);
                } else {
                    setSelectVoiceRec(position);
                }
                notifyDataSetChanged();
            }
        });
    }

    public void setSelectVoiceRec(int position){
        for (int i = 0 ; i < voiceRecItems.size(); i++){
            voiceRecItems.get(i).setRec_selected(false);
        }
        voiceRecItems.get(position).setRec_selected(true);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(voiceRecItems.get(position).filepath);
            mediaPlayer.prepare();
        }catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
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
            img_box =  itemView.findViewById(R.id.img_box);
            txt_title =  itemView.findViewById(R.id.txt_title);
            txt_runtime =  itemView.findViewById(R.id.txt_runtime);
            layout_play =  itemView.findViewById(R.id.layout_play);
        }
    }
}

