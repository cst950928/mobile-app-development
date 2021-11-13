package edu.neu.madcourse.MyMusicPlayer;

import java.io.File;
import java.io.Serializable;

public interface MusicItem extends Serializable {
    public File getFile();

    public void setFile(File file);

    public String getName();

    public void setFileName(String fileName);

    public String getUrl();

    public void setUrl(String url);
}
