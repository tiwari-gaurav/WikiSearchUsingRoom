package wiki.com.wikisearch.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;


@Database(entities = {PageEntity.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class})

public abstract class RoomDatabase extends android.arch.persistence.room.RoomDatabase{

    public abstract PageDao pageDao();

     private static RoomDatabase INSTANCE;

     public static RoomDatabase getdatabase(final Context context){
         if(INSTANCE==null){
             synchronized (RoomDatabase.class){
                 if(INSTANCE==null){
                     INSTANCE= Room.databaseBuilder(context.getApplicationContext(),RoomDatabase.class,"wiki_database")
                             .fallbackToDestructiveMigration()
                             .build();
                 }
             }
         }
         return INSTANCE;
     }
}
