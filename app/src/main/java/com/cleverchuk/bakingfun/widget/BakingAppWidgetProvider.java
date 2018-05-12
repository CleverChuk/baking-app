package com.cleverchuk.bakingfun.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.cleverchuk.bakingfun.R;
import com.cleverchuk.bakingfun.adapters.data.SQLiteHelper;
import com.cleverchuk.bakingfun.adapters.data.WidgetEntryDao;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    /**
     * updates the widget with new information
     * @param context calling context
     * @param appWidgetManager appwidget manager instance use to perform widget update
     * @param appWidgetId widget to update
     */
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        Intent intent = new Intent(context, MyRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setRemoteAdapter(appWidgetId, R.id.appwidget_stackView/*id for the collection view*/, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SQLiteHelper helper = new SQLiteHelper(context);
        WidgetEntryDao dao = new WidgetEntryDao(helper);
        for(int i : appWidgetIds){
            dao.delete(i);
        }
    }
}

