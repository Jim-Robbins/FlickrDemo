package com.android.flickr.jrobbins.myfriendflickr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.flickr.jrobbins.myfriendflickr.adapters.ImageListAdapter;
import com.android.flickr.jrobbins.myfriendflickr.flickr.Flickr;
import com.android.flickr.jrobbins.myfriendflickr.flickr.FlickrImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jim.robbins on 9/12/16.
 */
public class MainFragment extends Fragment {

    public static String LOG_TAG = MainFragment.class.getSimpleName();
    public static String KEY_SEARCH_TAG = "query_search";
    private ArrayList<FlickrImage> flickrImgList = new ArrayList<>();
    private ImageListAdapter imgListAdapter;
    private Activity activity;

    //private static final String IMG_LIST_KEY = "flickrImgList";
    //SharedPreferences sharedpreferences;
    //private String mSearchBy;
    //public static String PREF_TAG = "com.android.flickr.jrobbins.myfriendflickr";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (savedInstanceState != null && savedInstanceState.containsKey(IMG_LIST_KEY)) {
//            // Retrieve previous movie list
//            flickrImgList = savedInstanceState.getParcelableArrayList(IMG_LIST_KEY);
//            Log.d(LOG_TAG,"onCreate - with savedInstance flickerImgList:" + (flickrImgList!=null));
//        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

         // Store previous search
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
//        mSearchBy = sharedPref.getString("searchBy", "");
//
//        Log.d(LOG_TAG,"onStart - mSearchBy:"+mSearchBy);
        String searchBy = getArguments().getString(KEY_SEARCH_TAG);

        if (!TextUtils.isEmpty(searchBy)) { // && !searchBy.equalsIgnoreCase(mSearchBy)) {
            Log.d(LOG_TAG,"onStart - loadImages:"+searchBy);
            // Our search has changed, so do a fresh look up from the api
            loadImages(searchBy);

            // Increment the counter
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString("searchBy", searchBy);
//            editor.commit(); // Very important
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(LOG_TAG,"onCreateView");
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Create instance of our custom ArrayAdapter
        imgListAdapter = new ImageListAdapter( getActivity(), flickrImgList);

        // Get reference to our grid list and apply our adapter
        ListView listviewFlickrImgs = (ListView) rootView.findViewById(R.id.listview_images);
        listviewFlickrImgs.setAdapter(imgListAdapter);

        listviewFlickrImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlickrImage flickrImage = imgListAdapter.getItem(position);
                String imgTitle = flickrImage.getTitle();
                if (imgTitle != null) {
                    Toast.makeText(activity, imgTitle, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        //Store our current movie list
//        outState.putParcelableArrayList(IMG_LIST_KEY, flickrImgList);
//        super.onSaveInstanceState(outState);
//    }

    /**
     *
     */
    private void loadImages(String searchBy)
    {
        Log.d(LOG_TAG,"loadImages:"+ searchBy);
        FlickerLoaderTask imageListTask = new FlickerLoaderTask();
        imageListTask.execute(searchBy);
    }

    public class FlickerLoaderTask extends AsyncTask<String, Integer, ArrayList<FlickrImage>> {

        ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgress = new ProgressDialog(activity);
            mProgress.setTitle(getString(R.string.loading));
            mProgress.setMessage(getString(R.string.loading_image_list));
            mProgress.show();
        }

        @Override
        protected ArrayList<FlickrImage> doInBackground(String... params) {

            ArrayList<FlickrImage> tmp = new ArrayList<FlickrImage>();
            Log.d(LOG_TAG,"doInBackground:"+ params[0]);

            try {
                JSONArray imageJSONArray = Flickr.getImgeJsonAryBySearchImagesByTag(params[0]);
                for (int i = 0; i < imageJSONArray.length(); i++) {
                    JSONObject item = imageJSONArray.getJSONObject(i);
                    FlickrImage imgCon = new FlickrImage(
                            item.getString(Flickr.KEY_ID),
                            item.getString(Flickr.KEY_OWNER),
                            item.getString(Flickr.KEY_SECRET),
                            item.getString(Flickr.KEY_SERVER),
                            item.getString(Flickr.KEY_FARM),
                            item.getString(Flickr.KEY_TITLE),
                            i);
                    tmp.add(imgCon);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tmp;
        }

        @Override
        protected void onPostExecute(ArrayList<FlickrImage> listData) {

            super.onPostExecute(listData);

            mProgress.dismiss();

            if (listData != null) {
                //Reset our adapter with the new movie data
                flickrImgList = new ArrayList<>(listData);
            }

            if(imgListAdapter == null)
            {
                imgListAdapter = new ImageListAdapter( getActivity(), flickrImgList);
            }

            imgListAdapter.clear();
            imgListAdapter.addAll(listData);

        }
    }

}
