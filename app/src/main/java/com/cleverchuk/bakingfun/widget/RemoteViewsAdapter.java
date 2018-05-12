package com.cleverchuk.bakingfun.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.cleverchuk.bakingfun.R;
import com.cleverchuk.bakingfun.adapters.data.SQLiteHelper;
import com.cleverchuk.bakingfun.adapters.data.WidgetEntryDao;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * an implementation of {@link RemoteViewsService.RemoteViewsFactory}
 * Created by chuk on 5/5/18,at 13:21.
 */

class RemoteViewsAdapter implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private final int mAppWidgetId;
    private final ArrayList<String> ingredients;

    public RemoteViewsAdapter(Context context, Intent intent){
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        ingredients = new ArrayList<>();
    }


    @Override
    public void onCreate() {
        SQLiteHelper helper = new SQLiteHelper(mContext);
        WidgetEntryDao dao = new WidgetEntryDao(helper);

        String[] arr = dao.query(mAppWidgetId);
        ingredients.addAll(Arrays.asList(arr));
    }

    @Override
    public void onDataSetChanged() {
        ingredients.clear();
        SQLiteHelper helper = new SQLiteHelper(mContext);
        WidgetEntryDao dao = new WidgetEntryDao(helper);

        String[] arr = dao.query(mAppWidgetId);
        ingredients.addAll(Arrays.asList(arr));
    }

    @Override
    public void onDestroy() {
        ingredients.clear();
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views;
        if(position == 0){
            views = new RemoteViews(mContext.getPackageName(),R.layout.item_widget_header);
            String text = ingredients.get(position);
            views.setTextViewText(R.id.title_tv,text);
        }else {
            views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
            String ingredient = ingredients.get(position);
            views.setTextViewText(R.id.appwidget_item,ingredient);
        }

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
