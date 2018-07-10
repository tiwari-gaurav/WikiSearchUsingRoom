package wiki.com.wikisearch.network;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wiki.com.wikisearch.room.PageEntity;

public interface APiInterface {
    @GET("/w/api.php")
    Call<List<PageEntity>> getPages(@Query("action") String action,
                                    @Query("format") String format,
                                    @Query("prop") String prop,
                                    @Query("generator") String generator,
                                    @Query("formatversion") String formatversion,
                                    @Query("piprop") String piprop,
                                    @Query("wbptterms") String wbptterms,
                                    @Query("gpssearch") String gpssearch);
}
