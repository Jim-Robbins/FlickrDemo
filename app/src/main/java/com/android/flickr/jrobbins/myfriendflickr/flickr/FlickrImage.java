package com.android.flickr.jrobbins.myfriendflickr.flickr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jim.robbins on 9/12/16.
 */

public class FlickrImage implements Parcelable {
    private String _id;
    private String _owner;
    private String _secret;
    private String _server;
    private String _farm;
    private String _title;
    public int _position;

    public FlickrImage(String id, String owner, String secret, String server, String farm, String title, int position) {
        _id = id;
        _owner = owner;
        _secret = secret;
        _server = server;
        _farm = farm;
        _title = title;
        _position = position;
    }

    public String getTitle() {
        return _title;
    }

    public String getImgUrl()
    {
        //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
        return String.format(Flickr.FLICKR_BASE_IMG_URL, _farm, _server, _id, _secret);
    }

    protected FlickrImage(Parcel in) {
        _id = in.readString();
        _owner = in.readString();
        _secret = in.readString();
        _server = in.readString();
        _farm = in.readString();
        _title = in.readString();
        _position = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(_owner);
        dest.writeString(_secret);
        dest.writeString(_server);
        dest.writeString(_farm);
        dest.writeString(_title);
        dest.writeInt(_position);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FlickrImage> CREATOR = new Parcelable.Creator<FlickrImage>() {
        @Override
        public FlickrImage createFromParcel(Parcel in) {
            return new FlickrImage(in);
        }

        @Override
        public FlickrImage[] newArray(int size) {
            return new FlickrImage[size];
        }
    };

}
