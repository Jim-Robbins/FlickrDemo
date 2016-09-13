package com.android.flickr.jrobbins.myfriendflickr.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.flickr.jrobbins.myfriendflickr.R;
import com.android.flickr.jrobbins.myfriendflickr.flickr.FlickrImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jim.robbins on 9/12/16.
 */

public class ImageListAdapter extends ArrayAdapter<FlickrImage> {

    private static final String LOG_TAG = ImageListAdapter.class.getSimpleName();

    private Activity mContext;

    /**
     * Custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param imagesList     A List of FlickrImage objects to display in a list
     */
    public ImageListAdapter(Activity context, List<FlickrImage> imagesList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // Because this is a custom adapter for an ImageView, the adapter is not
        // going to use the second argument, so it can be any value.
        super(context, 0, imagesList);
        mContext = context;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MovieVO object from the ArrayAdapter at the appropriate position
        FlickrImage flickrImage = getItem(position);

        // Adapters recycle views to AdapterViews.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_image, parent, false);
        }

        ImageView imgView = (ImageView) convertView.findViewById(R.id.list_item_flickr_image);
        String imgPath = flickrImage.getImgUrl();

        Log.d(LOG_TAG, imgPath);

        //Use Picasso to load in the movie poster into the imageView
//        if (BuildConfig.DEBUG) {
//            Picasso.with(mContext).setIndicatorsEnabled(true);
//            Picasso.with(mContext).setLoggingEnabled(true);
//        }
        Picasso.with(mContext)
                .load(imgPath)
                .placeholder( R.drawable.progress_animation )
                .error(R.drawable.no_image)
                .into(imgView);

        return convertView;
    }

}
