package de.domjos.customwidgets.sample.activities;

import android.app.Activity;
import android.os.SystemClock;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.domjos.customwidgets.sample.R;
import de.domjos.customwidgets.sample.utils.Helper;
import de.domjos.customwidgets.widgets.swiperefreshdeletelist.SwipeRefreshDeleteList;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SwipeRefreshDeleteListActivityTest {
    private Activity activity;
    private SwipeRefreshDeleteList lvList;

    @Rule
    public ActivityTestRule<SwipeRefreshDeleteListActivity> activityTestRule = new ActivityTestRule<>(SwipeRefreshDeleteListActivity.class);

    @Before
    public void setup() {
        this.activity = Helper.getActivityInstance();
        this.lvList = this.activity.findViewById(R.id.lvList);
        this.lvList.getAdapter().clear();
    }

    @Test
    public void testAddingItems() {
        for(int i = 1; i<=10; i++) {
            onView(withId(R.id.txtTitle)).perform(ViewActions.typeText("Test " + i));
            onView(withId(R.id.txtSubTitle)).perform(ViewActions.typeText("Sub-Test " + i));
            onView(withId(R.id.cmdAdd)).perform(click());

            assertThat(lvList.getAdapter().getItemCount(), is(i));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRemovingItems() {
        for(int i = 1; i<=10; i++) {
            onView(withId(R.id.txtTitle)).perform(ViewActions.typeText("Test " + i));
            onView(withId(R.id.txtSubTitle)).perform(ViewActions.typeText("Sub-Test " + i));
            onView(withId(R.id.cmdAdd)).perform(click());

            assertThat(lvList.getAdapter().getItemCount(), is(i));
        }

        for(int i = 1; i<=10; i++) {
            onData(anything()).inAdapterView(allOf(withId(R.id.lvList))).atPosition(0).perform(click());
            SystemClock.sleep(800);
            onData(anything()).inAdapterView(allOf(withId(R.id.lvList))).atPosition(0).perform(ViewActions.swipeLeft());
        }
        assertThat(lvList.getAdapter().getItemCount(), is(0));
    }
}
