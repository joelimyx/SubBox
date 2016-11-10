package com.joelimyx.subbox.mainlist;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.joelimyx.subbox.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
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
    public void detailActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.title_text), withText("Dollar Shaving Club"),
                        isDisplayed()));
        textView.check(matches(withText("Dollar Shaving Club")));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.detail_image),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.detail_price_text), withText("$7.99"),
                        isDisplayed()));
        textView2.check(matches(withText("$7.99")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.detail_price_text), withText("$7.99"),
                        isDisplayed()));
        textView3.check(matches(withText("$7.99")));

        ViewInteraction button = onView(
                allOf(withId(R.id.detail_button),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.detail_button), withText("Add to cart"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.detail_button),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.cart), withContentDescription("Go to cart"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction relativeLayout2 = onView(
                        isDisplayed());
        relativeLayout2.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.checkout_title_text), withText("Dollar Shaving Club"),
                        isDisplayed()));
        textView4.check(matches(withText("Dollar Shaving Club")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.checkout_price_text), withText("$7.99"),
                        isDisplayed()));
        textView5.check(matches(withText("$7.99")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.checkout_count_text), withText("1"),
                        isDisplayed()));
        textView6.check(matches(withText("1")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.checkout_count_text), withText("1"),
                        isDisplayed()));
        textView7.check(matches(withText("1")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
