package com.android.flickr.jrobbins.myfriendflickr.flickr;

import com.android.flickr.jrobbins.myfriendflickr.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jim.robbins on 9/12/16.
 */

public class Flickr {
    private static final String LOG_TAG = Flickr.class.getSimpleName();

    // String to create Flickr API urls
    private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/?method=";
    public static final String FLICKR_BASE_IMG_URL = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";
    private static final String FLICKR_PHOTOS_SEARCH_STRING = "flickr.photos.search";
    private static final String FLICKR_GET_SIZES_STRING = "flickr.photos.getSizes";
    private static final int FLICKR_PHOTOS_SEARCH_ID = 1;
    private static final int FLICKR_GET_SIZES_ID = 2;
    public static final int NUMBER_OF_PHOTOS = 100;

    private static final String APIKEY_SEARCH_STRING = "&api_key=" + BuildConfig.FLICKR_API_KEY;
    private static final String TAGS_STRING = "&tags=";
    private static final String PHOTO_ID_STRING = "&photo_id=";
    private static final String FORMAT_STRING = "&format=json";
    public static final int PHOTO_THUMB = 111;
    public static final int PHOTO_LARGE = 222;

    public static String KEY_PHOTOS = "photos";
    public static String KEY_PHOTO = "photo";
    public static String KEY_ID = "id";
    public static String KEY_OWNER = "owner";
    public static String KEY_SECRET = "secret";
    public static String KEY_SERVER = "server";
    public static String KEY_FARM = "farm";
    public static String KEY_TITLE = "title";
//    public static String KEY_SIZES = "sizes";
//    public static String KEY_SIZE = "size";
//    public static String KEY_LABEL = "label";
//    public static String KEY_SQUARE = "Square";
//    public static String KEY_SOURCE = "source";
//    public static String KEY_MEDIUM = "Medium";

    private static String createURL(int methodId, String parameter) {
        String method_type = "";
        String url = null;
        switch (methodId) {
            case FLICKR_PHOTOS_SEARCH_ID:
                method_type = FLICKR_PHOTOS_SEARCH_STRING;
                url = FLICKR_BASE_URL + method_type + APIKEY_SEARCH_STRING + TAGS_STRING + parameter + FORMAT_STRING + "&per_page=" + NUMBER_OF_PHOTOS + "&media=photos";
                break;
            case FLICKR_GET_SIZES_ID:
                method_type = FLICKR_GET_SIZES_STRING;
                url = FLICKR_BASE_URL + method_type + PHOTO_ID_STRING + parameter + APIKEY_SEARCH_STRING + FORMAT_STRING;
                break;
        }
        return url;
    }

    public static JSONArray getImgeJsonAryBySearchImagesByTag(String tag) {

        JSONArray imageJSONArray = null;

        try {
            String url = createURL(FLICKR_PHOTOS_SEARCH_ID, tag);
            //Log.d("Flickr","getImgeJsonAryBySearchImagesByTag:"+ url);
            JSONObject root = getFlickrJsonResponseObject(url);
            JSONObject photos = root.getJSONObject(KEY_PHOTOS);
            imageJSONArray = photos.getJSONArray(KEY_PHOTO);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imageJSONArray;
    }

    private static JSONObject getFlickrJsonResponseObject(String url)
    {
        String jsonString = null;
        JSONObject jsonObject = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response responses = null;

        try {
            responses = client.newCall(request).execute();
            jsonString = responses.body().string();
            jsonObject = new JSONObject(jsonString.replace("jsonFlickrApi(", "").replace(")", ""));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}