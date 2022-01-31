package edu.neu.madcourse.MyMusicPlayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

/**
 * Music files from online resources in Cloud Storage
 */
public class OnlineItem implements Serializable, MusicItem {
    private String fileName;
    private String url;

    /**
     * Constructor
     * @param fileName the file name of online mp3 file
     * @param url the url of online mp3 file
     */
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

    /**
     * Getters and setters
     */
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
