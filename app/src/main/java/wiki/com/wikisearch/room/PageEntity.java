package wiki.com.wikisearch.room;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "WikiPages")
public class PageEntity {

    @PrimaryKey(autoGenerate = true)
    private int page_id;


    private String title;


    private int index;

    @Embedded
    private Thumbnail thumbnail;
    @Embedded
    private Terms terms;

    public PageEntity(){

    }

    public PageEntity(int page_id, String title, Thumbnail thumbnail,Terms terms) {

        this.page_id = page_id;
        this.title = title;
        this.terms=terms;
        this.thumbnail = thumbnail;

    }


    public Terms getTerms() {
        return terms;
    }

    public void setTerms(Terms terms) {
        this.terms = terms;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
