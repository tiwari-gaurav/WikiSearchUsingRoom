package wiki.com.wikisearch.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wiki.com.wikisearch.room.PageResponseInterceptor;

public class ApiClient {
    public static final String Base_Url = "https://en.wikipedia.org/";
    public static Retrofit retrofit = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
static {
    httpClient.connectTimeout(30_000, TimeUnit.MILLISECONDS);
    httpClient.readTimeout(30_000, TimeUnit.MILLISECONDS);
    httpClient.writeTimeout(30_000, TimeUnit.MILLISECONDS);
    httpClient.addInterceptor(new PageResponseInterceptor());
}
    public static Retrofit getClient(){
        if(retrofit==null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }
}
