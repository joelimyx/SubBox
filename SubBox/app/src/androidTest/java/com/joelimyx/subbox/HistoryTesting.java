package com.joelimyx.subbox;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.joelimyx.subbox.mainlist.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.joelimyx.subbox.CheckOutTesting.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HistoryTesting {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Before
    public void setUp() throws Exception {

        onView(withRecyclerView(R.id.recyclerview).atPosition(1))
                .perform(click());
        onView(allOf(withId(R.id.detail_button), withText("Add to cart"), isDisplayed()))
                .perform(click());
        onView(allOf(withContentDescription("Navigate up"), isDisplayed()))
                .perform(click());

        onView(withRecyclerView(R.id.recyclerview).atPosition(2))
                .perform(click());
        onView(allOf(withId(R.id.detail_button), withText("Add to cart"), isDisplayed()))
                .perform(click());
        onView(allOf(withContentDescription("Navigate up"), isDisplayed()))
                .perform(click());

        onView(withRecyclerView(R.id.recyclerview).atPosition(4))
                .perform(click());
        onView(allOf(withId(R.id.detail_button), withText("Add to cart"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.cart), withContentDescription("Go to cart"), isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.checkout_button), withText("checkout"), isDisplayed()))
                .perform(click());
        onView(allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()))
                .perform(click());
        onView(allOf(withContentDescription("Navigate up"), isDisplayed()))
                .perform(click());
        onView(allOf(withContentDescription("Navigate up"), isDisplayed()))
                .perform(click());
    }
    @Test
    public void historyTesting() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(allOf(withId(R.id.title), withText("History"), isDisplayed()))
                .perform(click());

        onView(withRecyclerView(R.id.history_recyclerview).atPositionOnView(0,R.id.history_total_text))
                .check(matches(withText("$89.16")));

    }

}
