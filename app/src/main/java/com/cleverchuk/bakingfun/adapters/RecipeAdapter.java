package com.cleverchuk.bakingfun.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleverchuk.bakingfun.R;
import com.cleverchuk.bakingfun.models.Recipe;
import com.squareup.picasso.Picasso;

/**
 * a subclass of {@link CleverChukBaseAdapter} for displaying recipes
 * Created by chuk on 4/30/18,at 20:56.
 */

public class RecipeAdapter extends CleverChukBaseAdapter<RecipeAdapter.ViewHolder, Recipe> {
    public static final String KEY = "com.cleverchuk.bakingapp.recipe.data";
    private final Context mContext;

    public RecipeAdapter(Context context, OnItemClickListener listener) {
        super(KEY);
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = mData.get(position);
        if (!TextUtils.isEmpty(recipe.getImage()))
            Picasso.get()
                    .load(recipe.getImage())
                    .placeholder(R.drawable.ic_recipe)
                    .error(R.drawable.ic_recipe)
                    .into(holder.imageView);

        holder.textView.setText(recipe.getName());
    }


    class ViewHolder extends CleverChukBaseAdapter.BaseHolder {
        final ImageView imageView;
        final TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_iv);
            textView = itemView.findViewById(R.id.recipe_title_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mListener.itemClicked(pos);
        }
    }
}
