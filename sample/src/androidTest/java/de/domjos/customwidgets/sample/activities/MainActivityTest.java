/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.sample.activities;

import android.app.Activity;
import android.content.Context;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.domjos.customwidgets.sample.R;
import de.domjos.customwidgets.sample.activities.MainActivity;
import de.domjos.customwidgets.sample.activities.SwipeRefreshDeleteListActivity;
import de.domjos.customwidgets.sample.utils.Helper;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainActivityTest {
    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        this.context = Helper.getContext();
    }

    @Test
    public void testContext() {
        assertEquals("de.domjos.customwidgets.sample", context.getPackageName());
    }

    @Test
    public void testOpenList() {
        onView(ViewMatchers.withId(R.id.cmdMainList)).perform(ViewActions.click());
        Activity activity = Helper.getActivityInstance();
        assertEquals(activity.getComponentName().getClassName(), SwipeRefreshDeleteListActivity.class.getName());
        assertNotNull(activity.findViewById(R.id.lvList));
    }

    @Test
    public void testOpenCalendar() {
        onView(ViewMatchers.withId(R.id.cmdMainCalendar)).perform(ViewActions.click());
        Activity activity = Helper.getActivityInstance();
        assertEquals(activity.getComponentName().getClassName(), CalendarActivity.class.getName());
        assertNotNull(activity.findViewById(R.id.calendar));
    }
}
