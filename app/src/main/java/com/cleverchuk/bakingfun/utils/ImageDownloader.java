/*
 * MIT License
 *
 * Copyright (c) 2018 Chukwubuikem Ume-Ugwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
