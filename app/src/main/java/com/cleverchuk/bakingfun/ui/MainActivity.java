package com.cleverchuk.bakingfun.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cleverchuk.bakingfun.R;
import com.cleverchuk.bakingfun.adapters.CleverChukBaseAdapter;
import com.cleverchuk.bakingfun.adapters.RecipeAdapter;
import com.cleverchuk.bakingfun.adapters.data.SQLiteHelper;
import com.cleverchuk.bakingfun.adapters.data.WidgetEntryDao;
import com.cleverchuk.bakingfun.databinding.ActivityMainBinding;
import com.cleverchuk.bakingfun.models.Ingredient;
import com.cleverchuk.bakingfun.models.Recipe;
import com.cleverchuk.bakingfun.utils.Query;
import com.cleverchuk.bakingfun.utils.Queue;
import com.cleverchuk.bakingfun.utils.RecipeGridLayoutManager;
import com.cleverchuk.bakingfun.widget.MyRemoteViewsService;
import com.google.gson.Gson;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity implements CleverChukBaseAdapter.OnItemClickListener {
    private final Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ERROR", new ParseError(error).getMessage());
        }
    };

    private RecipeAdapter mAdapter;
    private Queue mQueue;
    private int mAppWidgetId;



    /* testing variable */
    private CountingIdlingResource countingIdlingResource;
    public CountingIdlingResource getCountingIdlingResource() {
        return countingIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mAdapter = new RecipeAdapter(this, this);
        mBinding.recipeRv.setAdapter(mAdapter);

        // retrieve the column spans for different screen sizes
        TypedValue spanCount = new TypedValue();
        getResources().getValue(R.dimen.column_span, spanCount, true);
        mBinding.recipeRv.setLayoutManager(new RecipeGridLayoutManager(this, (int) spanCount.getFloat()));
        mQueue = Queue.getInstance(this);

        countingIdlingResource = new CountingIdlingResource("idlingResource");

        if (savedInstanceState == null) {
            requestRecipes();
        }

        //Handling for widget launch
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                getSupportActionBar().setTitle(getString(R.string.make_choice));
            }
        }
    }

    @Override
    public void itemClicked(int position) {
        Recipe recipe = mAdapter.getItem(position);
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra(RecipeAdapter.KEY, recipe);
            startActivity(intent);
        } else {

            SQLiteHelper helper = new SQLiteHelper(this);
            WidgetEntryDao dao = new WidgetEntryDao(helper);
            String ingredients = " " + recipe.getName() + " ingredients\n" + Ingredient.
                    stringify(recipe.getIngredients());
            dao.insert(mAppWidgetId,ingredients);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_layout);

            Intent intent = new Intent(this, MyRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(mAppWidgetId,R.id.appwidget_stackView/*id for the collection view*/,intent);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);

            Intent restValue = new Intent();
            restValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, restValue);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanUp();
    }

    private void requestRecipes() {
        /* used for testing */
        countingIdlingResource.increment();
        String url = Query.getEnpoint(this, R.string.data_endpoint);
        final JsonArrayRequest recipeRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                int count = response.length();

                for (int i = 0; i < count; i++) {
                    String str = response.optJSONObject(i).toString();
                    Recipe recipe = gson.fromJson(str, Recipe.class);
                    mAdapter.addData(recipe);
                }
                /* used testing */
                countingIdlingResource.decrement();
            }
        }, mErrorListener);
        mQueue.queue(recipeRequest, "Recipe request");

    }
}
