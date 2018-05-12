package com.cleverchuk.bakingfun.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cleverchuk.bakingfun.R;
import com.cleverchuk.bakingfun.models.Step;

/**
 * a subclass of {@link CleverChukBaseAdapter} for displaying
 * recipe steps
 * Created by chuk on 5/1/18,at 10:47.
 */

public class StepsAdapter extends CleverChukBaseAdapter<StepsAdapter.ViewHolder, Step> {
    public static final String KEY = "com.cleverchuk.bakingfun.steps.adapter";
    private final Context mContext;

    public StepsAdapter(Context context, OnItemClickListener listener) {
        super(KEY);
        mContext = context;
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = getItem(position);
        holder.titleBtn.setText(step.getShortDescription());
    }


    class ViewHolder extends CleverChukBaseAdapter.BaseHolder {
        final Button titleBtn;

        ViewHolder(View itemView) {
            super(itemView);
            titleBtn = itemView.findViewById(R.id.title_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mListener.itemClicked(pos);
        }
    }
}
