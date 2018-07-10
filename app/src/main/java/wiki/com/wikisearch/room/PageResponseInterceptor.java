package wiki.com.wikisearch.room;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PageResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        /*Request.Builder requestBuilder = original.newBuilder()
                .header("x-product-name", MobiefitSDKContract.instance().appShortcode);
        if (Session.singleton().getAccessToken() != null && Session.singleton().getAccessToken() != "") {
            requestBuilder.header("x-access-token", Session.singleton().getAccessToken());
        }
        Request request = requestBuilder.build();*/

        Request request = original;

        Log.i("Retrofit URL :: ", request.url().toString());
        if (request.headers() != null) {
            Log.i("Retrofit Headers :: ", request.headers().toMultimap().toString());
        }
        if (request.body() != null) {
            Log.i("Retrofit Body :: ", request.toString());
        }
        Response response = null;
        try {


            //Hit the Server and get the response
            response = chain.proceed(request);
            String body = response.body().string();
            MediaType contentType = response.body().contentType();
            Log.i("Retrofit Response :: ", body);
            JsonObject apiResponse = new JsonParser().parse(body).getAsJsonObject();
            String statusCode = getStringSafe(apiResponse, "batchcomplete");

            //Parse the response for
            //    1. Checking non 200 status
            //    2. Checking 400 - should be logged out then


            if (statusCode.equals("true")|| statusCode.equals("")) {
                //return response.newBuilder().body(ResponseBody.create(contentType, body)).build();

                JsonArray jsonArray =  new JsonParser().parse(body).getAsJsonObject().getAsJsonObject("query").getAsJsonArray("pages");

                MediaType contentTypee = response.body().contentType();
                ResponseBody bodye = ResponseBody.create(contentTypee, jsonArray.toString());
                return response.newBuilder().body(bodye).build();
            } else {
            }

        } catch (SocketTimeoutException ex) {
            ex.printStackTrace();
        } catch (JsonParseException e ) {
            e.printStackTrace();
        }catch (NullPointerException e ) {
            e.printStackTrace();
        }
        return  response;
    }

    public static String getStringSafe(JsonObject json, String key) {
        return json.get(key) != null && !json.get(key).isJsonNull() ?  json.get(key).getAsString() : null;
    }
}
