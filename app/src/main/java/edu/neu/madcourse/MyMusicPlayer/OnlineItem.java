package edu.neu.madcourse.MyMusicPlayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

public class OnlineItem implements Serializable, MusicItem {
    private String fileName;
    private String url;

    public OnlineItem(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public void setFile(File file) {

    }

    public String getName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
