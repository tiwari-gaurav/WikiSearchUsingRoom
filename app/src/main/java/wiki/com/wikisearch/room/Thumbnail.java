package wiki.com.wikisearch.room;

import android.arch.persistence.room.Ignore;

public class Thumbnail {

    private String source;
    private int width,height;

@Ignore
    public Thumbnail(){

    }
   public Thumbnail(String source, int width, int height){
        this.height=height;
        this.source=source;
        this.width=width;

    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
