package com.cleverchuk.bakingfun;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cleverchuk.bakingfun.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 *  ui test for {@link MainActivity}
 * Created by chuk on 5/6/18,at 13:38.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingresource(){
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().
                getCountingIdlingResource());
    }

    @Test
    public void onRecipeClickedTest() {
        /*
            perform click action on the recipe recycler view item to start the
            list activity
         */
        onView(withId(R.id.recipe_rv))
                .check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        /*
            check if the list activity was started with the steps recycler view displayed
         */
        onView(withId(R.id.steps_rv))
                .check(matches(isDisplayed()));

        /*
            check if the recycler view contains an item with the text
         */
        onView(withText("Recipe Introduction")).check(matches(isDisplayed()));
    }

    @After
    public void unRegisterIdlingResource(){
        Espresso.unregisterIdlingResources(mActivityTestRule.getActivity().
                getCountingIdlingResource());
    }

}
