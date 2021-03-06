package com.joelimyx.subbox;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.joelimyx.subbox.R;
import com.joelimyx.subbox.mainlist.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void detailAddToCartTest() {
        onView(allOf(withId(R.id.recyclerview), isDisplayed()))
                .perform(actionOnItemAtPosition(1, click()));

        onView(allOf(withId(R.id.title_text), withText("Dollar Shaving Club"), isDisplayed()))
                .check(matches(withText("Dollar Shaving Club")));

        onView(allOf(withId(R.id.detail_image), isDisplayed()))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.detail_price_text), withText("$7.99"), isDisplayed()))
                .check(matches(withText("$7.99")));

        onView(allOf(withId(R.id.detail_price_text), withText("$7.99"), isDisplayed()))
                .check(matches(withText("$7.99")));

        onView(allOf(withId(R.id.detail_button), isDisplayed()))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.detail_button), withText("Add to cart"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.detail_button), withText("In Cart"), isDisplayed()));

        onView(allOf(withId(R.id.cart), withContentDescription("Go to cart"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.checkout_title_text), withText("Dollar Shaving Club"), isDisplayed()))
                .check(matches(withText("Dollar Shaving Club")));

        onView(allOf(withId(R.id.checkout_price_text), withText("$7.99"), isDisplayed()))
                .check(matches(withText("$7.99")));

        onView(allOf(withId(R.id.checkout_count_text), withText("1"), isDisplayed() ))
                .check(matches(withText("1")));

        onView(allOf(withId(R.id.checkout_count_text), withText("1"), isDisplayed() ))
                .check(matches(withText("1")));

    }
}
