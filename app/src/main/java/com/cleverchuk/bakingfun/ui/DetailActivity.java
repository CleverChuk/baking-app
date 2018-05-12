package com.cleverchuk.bakingfun.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cleverchuk.bakingfun.R;
import com.cleverchuk.bakingfun.adapters.RecipeAdapter;
import com.cleverchuk.bakingfun.adapters.StepsAdapter;
import com.cleverchuk.bakingfun.databinding.ActivityDetailBinding;
import com.cleverchuk.bakingfun.models.Step;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private int mCurrentStepPosition;
    private static final String TAG = "fragment";
    private ArrayList<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Intent intent = getIntent();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        getSupportActionBar().setTitle(intent.getStringExtra(getString(R.string.title)));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.prevFab.setVisibility(View.GONE);
            mBinding.nextFab.setVisibility(View.GONE);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);

        }

        /*
            only create a fragment when there is none available
            to preserve state across orientation
          */
        if (fragment == null) {
            mCurrentStepPosition = intent.getIntExtra(getString(R.string.step_position), 0);
            Step currentStep = intent.getParcelableExtra(StepsAdapter.KEY);
            mSteps = intent.getParcelableArrayListExtra(RecipeAdapter.KEY);
            fragment = DetailFragment.getInstance(currentStep, true);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.item_detail_container, fragment, TAG)
                .commit();

        mBinding.nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = mSteps.size() - 1;
                if (mCurrentStepPosition < count) {
                    Step step = mSteps.get(++mCurrentStepPosition);
                    DetailFragment fragment = DetailFragment.getInstance(step, true);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.item_detail_container, fragment, TAG)
                            .commit();
                }
            }
        });
        mBinding.prevFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentStepPosition > 0) {
                    Step step = mSteps.get(--mCurrentStepPosition);
                    DetailFragment fragment = DetailFragment.getInstance(step, true);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.item_detail_container, fragment, TAG)
                            .commit();
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(StepsAdapter.KEY, mSteps);
        outState.putInt(getString(R.string.cur_pos), mCurrentStepPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSteps = savedInstanceState.getParcelableArrayList(StepsAdapter.KEY);
        mCurrentStepPosition = savedInstanceState.getInt(getString(R.string.cur_pos));
    }
}
