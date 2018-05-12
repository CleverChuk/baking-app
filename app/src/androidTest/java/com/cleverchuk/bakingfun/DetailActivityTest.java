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
 * ui test for {@link com.cleverchuk.bakingfun.ui.DetailActivity}
 * Created by chuk on 5/7/18,at 00:44.
 */
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Before
    public void registerIdlingresource(){
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().
                getCountingIdlingResource());
    }

    @Test
    public void detailActivityTest(){
        /* perform click to start the list activity */
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        /* perform click to start the detail activity */
        onView(withId(R.id.steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        /* check if the previous button is displayed */
        onView(withId(R.id.prev_fab)).check(matches(isDisplayed()));
        /* navigates back to the previous step  */
        onView(withId(R.id.prev_fab)).perform(click());
        /*check if the navigation was successful */
        onView(withText("Recipe Introduction")).check(matches(isDisplayed()));
    }

    @After
    public void unRegisterIdlingResource(){
        Espresso.unregisterIdlingResources(mActivityTestRule.getActivity().
                getCountingIdlingResource());
    }
}
