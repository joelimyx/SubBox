package com.joelimyx.subbox;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.joelimyx.subbox.R;
import com.joelimyx.subbox.mainlist.MainActivity;

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
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.joelimyx.subbox.CheckOutTesting.withRecyclerView;
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
    }

    @Test
    public void priceSortTest(){
        //Price Sort Test
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(allOf(withId(R.id.title), withText("Sort by price"), isDisplayed()))
                .perform(click());

        onView(withRecyclerView(R.id.recyclerview).atPosition(0))
                .check(matches(hasDescendant(withText("$7.00"))));
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
