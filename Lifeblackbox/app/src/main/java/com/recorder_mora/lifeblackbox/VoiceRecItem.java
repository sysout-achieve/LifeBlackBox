package com.recorder_mora.lifeblackbox;

public class VoiceRecItem {

    int rec_id;
    String rec_title;
    String rec_runningtime;
    boolean rec_selected;
    String filepath;

    VoiceRecItem (int rec_id , String rec_title, String rec_runningtime, boolean rec_selected, String filepath){
        this.rec_id = rec_id;
        this.rec_title = rec_title;
        this.rec_runningtime = rec_runningtime;
        this.rec_selected = rec_selected;
        this.filepath = filepath;
    }

    public int getRec_id() {
        return rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public String getRec_title() {
        return rec_title;
    }

    public void setRec_title(String rec_title) {
        this.rec_title = rec_title;
    }

    public String getRec_runningtime() {
        return rec_runningtime;
    }

    public void setRec_runningtime(String rec_runningtime) {
        this.rec_runningtime = rec_runningtime;
    }

    public boolean isRec_selected() {
        return rec_selected;
    }

    public void setRec_selected(boolean rec_selected) {
        this.rec_selected = rec_selected;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
