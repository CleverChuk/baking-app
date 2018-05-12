package com.cleverchuk.bakingfun.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * a subclass of {@link AsyncTask}
 * Created by chuk on 5/8/18,at 01:11.
 */

public class ImageDownloader extends AsyncTask<Void,Void,Void> {
    public interface OndoInbackGroundCompleteListener{
        void deliverImage(Bitmap bitmap);
        void error(String error);
    }
    private OndoInbackGroundCompleteListener mListener;
    private String mUrl;
    public ImageDownloader(String url, OndoInbackGroundCompleteListener listener){
        mListener = listener;
        mUrl = url;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        Bitmap bitmap;
        try {
           bitmap = Picasso.get().load(mUrl).get();
           mListener.deliverImage(bitmap);
        } catch (IOException e) {
            mListener.error(e.getMessage());
        }catch (IllegalArgumentException e){
            mListener.error(e.getMessage());
        }
        return null;
    }
}
