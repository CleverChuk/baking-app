package com.cleverchuk.bakingfun.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * a pojo class abstraction for ingredients
 * Created by chuk on 4/30/18.
 */

public class Ingredient implements Parcelable {
    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {
    }

    public Ingredient(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    private Ingredient(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public static String stringify(ArrayList<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();

        for (Ingredient in :
                ingredients) {
            builder.append(in.toString());
            builder.append("\n");
        }

        if(builder.length() == 0)
            return null;

        return builder.toString().substring(0, builder.length() - 1/*remove last new line*/);
    }

    @Override
    public String toString() {
        return quantity + " " + measure + " " + ingredient;
    }
}
