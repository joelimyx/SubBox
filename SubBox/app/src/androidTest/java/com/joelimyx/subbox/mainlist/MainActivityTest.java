package com.joelimyx.subbox.mainlist;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.joelimyx.subbox.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchByNameTest() {
        onView(allOf(withId(R.id.search), withContentDescription("search"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.search_src_text), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.search_src_text), isDisplayed()))
                .perform(replaceText("crate"), closeSoftKeyboard());

        onView(allOf(withId(R.id.name_text), withText("Loot Crate"), isDisplayed()))
                .check(matches(withText("Loot Crate")));

        onView(allOf(withId(R.id.name_text), withText("Runner Crate"), isDisplayed()))
                .check(matches(withText("Runner Crate")));
    }

    @Test
    public void searchByTypeTest() {
        onView(allOf(withId(R.id.search), withContentDescription("search"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.search_src_text), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.search_src_text), isDisplayed()))
                .perform(replaceText("outdoor"), closeSoftKeyboard());

        onView(allOf(withId(R.id.name_text), withText("Platte Pack"), isDisplayed()))
                .check(matches(withText("Platte Pack")));

        onView(allOf(withId(R.id.name_text), withText("BattlBox"), isDisplayed()))
                .check(matches(withText("BattlBox")));
    }

    @Test
    public void priceSortTest(){
        //Price Sort Test
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(allOf(withId(R.id.title), withText("Sort by price"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.price_text), withText("$7.00"), isDisplayed()))
                .check(matches(withText("$7.00")));
    }

    @Test
    public void filterTest(){
        //Filter Test
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(allOf(withId(R.id.title), withText("Filter by ..."), isDisplayed()))
                .perform(click());

        onView(allOf(withId(android.R.id.text1), withText("Personal Care"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(android.R.id.text1), withText("Food/Beverage"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(android.R.id.button1), withText("Filter"), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.name_text), withText("Dollar Shaving Club"), isDisplayed()))
                .check(matches(withText("Dollar Shaving Club")));

        onView(allOf(withId(R.id.name_text), withText("Universal Yum"), isDisplayed()))
                .check(matches(withText("Universal Yum")));

        onView(allOf(withId(R.id.name_text), withText("Soylent"), isDisplayed()))
                .check(matches(withText("Soylent")));
    }

}
