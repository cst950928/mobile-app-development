package edu.neu.madcourse.MyMusicPlayer;

import java.io.File;

/**
 * Music files from local devices
 */
public class LocalItem implements MusicItem{
    private File file;

    /**
     * Constructor
     * @param file the file path of local mp3 file
     */
    public LocalItem(File file) {
        this.file = file;
    }

    /**
     * Getters and setters
     * @return file object
     */
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
