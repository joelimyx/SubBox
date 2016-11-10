package com.joelimyx.subbox;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.joelimyx.subbox.Classes.RecyclerViewMatcher;
import com.joelimyx.subbox.R;
import com.joelimyx.subbox.mainlist.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CheckOutTesting {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainCheckOutTesting() throws Exception{
        //Checkout start checking
        onView(allOf(withId(R.id.checkout_title_text), withText("Dollar Shaving Club"), isDisplayed()))
                .check(matches(withText("Dollar Shaving Club")));

        onView(allOf(withId(R.id.checkout_title_text), withText("Universal Yum"), isDisplayed()))
                .check(matches(withText("Universal Yum")));

        onView(allOf(withId(R.id.checkout_title_text), withText("Four Five Club"), isDisplayed()))
                .check(matches(withText("Four Five Club")));

        onView(withRecyclerView(R.id.checkout_recyclerview).atPosition(0))
                .check(matches(hasDescendant(withText("1"))));
        onView(withRecyclerView(R.id.checkout_recyclerview).atPosition(1))
                .check(matches(hasDescendant(withText("1"))));
        onView(withRecyclerView(R.id.checkout_recyclerview).atPosition(2))
                .check(matches(hasDescendant(withText("1"))));

    }

    @Test
    public void additionTesting() throws Exception{
        //Test Addition

        onView(withRecyclerView(R.id.checkout_recyclerview).atPositionOnView(0,R.id.green_plus))
                .perform(click());
        onView(withRecyclerView(R.id.checkout_recyclerview).atPosition(0))
                .check(matches(hasDescendant(withText("2"))));

        onView(withRecyclerView(R.id.checkout_recyclerview).atPositionOnView(2,R.id.green_plus))
                .perform(click());
        onView(withRecyclerView(R.id.checkout_recyclerview).atPositionOnView(2,R.id.green_plus))
                .perform(click());
        onView(allOf(withId(R.id.checkout_count_text), withText("3"), isDisplayed()))
                .check(matches(withText("3")));

        onView(allOf(withId(R.id.subtotal_text), withText("Subtotal: $209.98"), isDisplayed()))
                .check(matches(withText("Subtotal: $209.98")));
    }

    @Test
    public void minusRemoveTesting() throws Exception{
        //Delete work with minus
        onView(withRecyclerView(R.id.checkout_recyclerview).atPositionOnView(1,R.id.red_minus))
                .perform(click());

        onView(withRecyclerView(R.id.checkout_recyclerview).atPositionOnView(1,R.id.checkout_title_text))
                .check(matches(withText("Four Five Club")));

        onView(allOf(withId(R.id.subtotal_text), withText("Subtotal: $67.99"), isDisplayed()))
                .check(matches(withText("Subtotal: $67.99")));
    }

    @Test
    public void checkOutClearTesting() {
        //Checkout button

        onView(allOf(withId(R.id.checkout_button), withText("checkout"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.subtotal_text), withText("Subtotal: $0.00"), isDisplayed()))
                .check(matches(withText("Subtotal: $0.00")));
    }

    @Before
    public void setUp() throws Exception {

        onView(withRecyclerView(R.id.recyclerview).atPosition(1)).perform(click());

        onView(allOf(withId(R.id.detail_button), withText("Add to cart"), isDisplayed()))
                .perform(click());

        onView(allOf(withContentDescription("Navigate up"), isDisplayed()))
                .perform(click());

        onView(withRecyclerView(R.id.recyclerview).atPosition(2)).perform(click());

        onView(allOf(withId(R.id.detail_button), withText("Add to cart"), isDisplayed()))
                .perform(click());

        onView(allOf(withContentDescription("Navigate up"), isDisplayed()))
                .perform(click());

        onView(withRecyclerView(R.id.recyclerview).atPosition(4)).perform(click());

        onView(allOf(withId(R.id.detail_button), withText("Add to cart"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.cart), withContentDescription("Go to cart"), isDisplayed()))
                .perform(click());
    }

    @After
    public void tearDown() throws Exception {
        onView(allOf(withId(R.id.checkout_button), withText("checkout"), isDisplayed()))
                .perform(click());
        onView(allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()))
                .perform(click());

    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
