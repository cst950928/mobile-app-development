package edu.neu.madcourse.MyMusicPlayer;

import java.io.File;

public class LocalItem implements MusicItem{
    private File file;

    public LocalItem(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public void setFileName(String fileName) {

    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public void setUrl(String url) {

    }
}
