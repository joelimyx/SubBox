package com.joelimyx.subbox;


import android.support.test.espresso.ViewInteraction;
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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.joelimyx.subbox.CheckOutTesting.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TransactionDetailTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void transactionTesting() {
        onView(withRecyclerView(R.id.transaction_recyclerview).atPositionOnView(0,R.id.transaction_title_text))
                .check(matches(withText("Dollar Shaving CLub")));
        onView(withRecyclerView(R.id.transaction_recyclerview).atPositionOnView(1,R.id.transaction_title_text))
                .check(matches(withText("Universal Yum")));
        onView(withRecyclerView(R.id.transaction_recyclerview).atPositionOnView(2,R.id.transaction_title_text))
                .check(matches(withText("Four Five Club")));

        onView(allOf(withId(R.id.transaction_subtotal_text), withText("Subtotal: $81.99"), isDisplayed()))
                .check(matches(withText("Subtotal: $81.99")));
    }

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

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(allOf(withId(R.id.title), withText("History"), isDisplayed()))
                .perform(click());
        onView(withRecyclerView(R.id.history_recyclerview).atPosition(0))
                .perform(click());
    }
}
