package com.cleverchuk.bakingfun.models;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * a simple class to represents the web response
 * Created by chuk on 4/30/18.
 */

abstract public class WebResponse<T extends WebResponse> implements Parcelable {
    private ArrayList<T> recipes;

    public ArrayList<T> getRecipes() {
        return recipes;
    }
}
