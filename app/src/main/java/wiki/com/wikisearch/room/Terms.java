package wiki.com.wikisearch.room;

import android.arch.persistence.room.Ignore;

import java.util.ArrayList;

public class Terms {
    public ArrayList<String>description = new ArrayList<String>();

    @Ignore
    public Terms(){

    }

    public Terms(ArrayList<String> description){
        this.description.addAll(description);

    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }
}
