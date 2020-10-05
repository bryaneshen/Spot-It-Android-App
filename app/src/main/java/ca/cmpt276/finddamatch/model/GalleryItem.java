package ca.cmpt276.finddamatch.model;


import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Class to store image data from the JSON file that we obtain via FLickr
 */

public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;
    private int position;

    @Override
    public String toString() {
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
