package ziad.bookstoresystem.Controllers;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class APIManager {
    public static String apiKey = "AIzaSyBCS5bBrh1bxYF73R7kWIeGpgj0xxZtxj8";

    public static OkHttpClient client = new OkHttpClient();

    private Request sendRequest(String url) {
        System.out.println("Sending request to " + url);
        return new Request.Builder()
                .url(url)
                .build();
    }

    public JSONArray getItems(Request request){
        try(Response response = client.newCall(request).execute()) {
            System.out.println("getting the Response");
            // Getting the JSON body
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            // Getting the Items Array
            JSONArray items = jsonObject.getJSONArray("items");
            System.out.println("Got the Items");
            // Looping on the Itams array
            return items;
        }catch (Exception e ){
            e.printStackTrace();
        }

        return null;
    }

    public JSONArray getData(String url){
        Request request = sendRequest(url);
        System.out.println("Sending the Final response ");
        return getItems(request);

    }

    public JSONObject srchID(String id){
        String fir = "https://www.googleapis.com/books/v1/volumes/";

        String url = fir + id +"?key="+ apiKey;
        System.out.println(url);

        Request request = sendRequest(url);
        try(Response response = client.newCall(request).execute()) {
            System.out.println("getting the Response");
            // Getting the JSON body
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            // Getting the Items Array
            JSONArray items = new JSONArray();
            System.out.println("Got the Items");
//            JSONObject volume = jsonObject.getJSONObject("volumeInfo");
            items.put(jsonObject);
//            System.out.println("got the book name" + volume.getString("title"));
            return jsonObject;
            // Looping on the Itams array
        }catch (Exception e ){
            e.printStackTrace();
        }
        System.out.println("Got the volume info");

        return null;
    }
    public JSONObject getRes(Request request) throws IOException {
        Call call = client.newCall(request);
        Response response = call.execute();
        String json = response.body().string();
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject;

    }






}
