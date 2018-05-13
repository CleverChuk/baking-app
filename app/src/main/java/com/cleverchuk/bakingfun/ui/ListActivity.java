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

package com.cleverchuk.bakingfun.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.cleverchuk.bakingfun.R;
import com.cleverchuk.bakingfun.adapters.CleverChukBaseAdapter;
import com.cleverchuk.bakingfun.adapters.RecipeAdapter;
import com.cleverchuk.bakingfun.adapters.StepsAdapter;
import com.cleverchuk.bakingfun.databinding.ActivityListBinding;
import com.cleverchuk.bakingfun.models.Ingredient;
import com.cleverchuk.bakingfun.models.Recipe;
import com.cleverchuk.bakingfun.models.Step;

public class ListActivity extends AppCompatActivity implements CleverChukBaseAdapter.OnItemClickListener {
    private static Recipe mRecipe;
    private StepsAdapter mAdapter;
    private static final String TAG = "fragment";
    private boolean mDualPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityListBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);


        if (getIntent().getParcelableExtra(RecipeAdapter.KEY) != null)
        {
            mRecipe = getIntent().getParcelableExtra(RecipeAdapter.KEY);
        }
        getSupportActionBar().setTitle(mRecipe.getName());

        mAdapter = new StepsAdapter(this, this);
        mBinding.ingredients.descriptionTv.setText(Ingredient.stringify(mRecipe.getIngredients()));
        mAdapter.addData(mRecipe.getSteps());

        mBinding.stepsRv.setAdapter(mAdapter);
        mBinding.stepsRv.setLayoutManager(new LinearLayoutManager(this));
        ViewGroup details = findViewById(R.id.item_detail_container);
        mDualPane = details != null;

        if (mDualPane) {
            /* get item in position 0 of the adapter data*/
            Step step = mAdapter.getItem(0);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            DetailFragment fragment = DetailFragment.getInstance(step,false);
                /* start transaction */
            transaction.replace(R.id.item_detail_container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.saveInstanceState(outState);
        outState.putParcelable(getString(R.string.recipe), mRecipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.restoreInstanceState(savedInstanceState);
        mRecipe = savedInstanceState.getParcelable(getString(R.string.recipe));
    }

    @Override
    public void itemClicked(int position) {
        Step step = mAdapter.getItem(position);
        if (mDualPane) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            DetailFragment fragment = DetailFragment.getInstance(step,true);
                /* start transaction */
            transaction.replace(R.id.item_detail_container, fragment, TAG)
                    .commit();


        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(StepsAdapter.KEY, step);
            intent.putExtra(getString(R.string.step_position), position);

            intent.putExtra(getString(R.string.title),mRecipe.getName());
            intent.putParcelableArrayListExtra(RecipeAdapter.KEY, mRecipe.getSteps());
            startActivity(intent);
        }
    }
}
