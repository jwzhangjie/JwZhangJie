package com.jwzhangjie.videoplayer.videofile;

import java.io.Serializable;

import com.jwzhangjie.videoplayer.component.LoadedImage;

public class Video implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7920222595800367956L;
	private int id;
    private String title;
    private String album;
    private String artist;
    private String displayName;
    private String mimeType;
    private String path;
    private long size;
    private long duration;
    private LoadedImage image;
    /** 视频标题拼音 */
	private String title_key;

    /**
     * 
     */
    public Video() {
        super();
    }

    /**
     * @param id
     * @param title
     * @param album
     * @param artist
     * @param displayName
     * @param mimeType
     * @param data
     * @param size
     * @param duration
     * @param title_key
     */
    public Video(int id, String title, String album, String artist,
            String displayName, String mimeType, String path, long size,
            long duration, String title_key) {
        super();
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.path = path;
        this.size = size;
        this.duration = duration;
        this.title_key = title_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public LoadedImage getImage(){
    	return image;
    }
    
    public void setImage(LoadedImage image){
    	this.image = image;
    }
    
    public final String getTitle_key(){
    	return title_key;
    }
    
    public final void setTitle_key(String title_key){
    	this.title_key = title_key;
    }
}