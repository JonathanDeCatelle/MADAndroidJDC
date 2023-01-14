package com.example.delawaretrackandtraceapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class NavigationTest {

    @Test
    fun testDelawareFragmentsNavigation() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        //Verify
        onView(withId(R.id.startFragment)).check(matches(isDisplayed()))
        //Navigate to cartFragment
        onView(withId(R.id.shipmentButton)).perform(click())
        //Verify
        onView(withId(R.id.cartFragment)).check(matches(isDisplayed()))
        pressBack()
        //Verify
        onView(withId(R.id.startFragment)).check(matches(isDisplayed()))
        //Navigate to cartFragment
        onView(withId(R.id.ordertrackingButton)).perform(click())
        //Verify
        onView(withId(R.id.startFragment)).check(matches(isDisplayed()))
        pressBack()
        //Verify
        onView(withId(R.id.startFragment)).check(matches(isDisplayed()))
    }
}