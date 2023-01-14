package com.example.delawaretrackandtraceapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CartFragmentTest {

//    val straat : kotlin.String = "Voskenslaan"
//    val huisNr : kotlin.String = "20"
//    val postCod : kotlin.String = "9000"
//    val stad : kotlin.String = "Gent"

    @Test
    fun test_kanNietNavigerenZonderCartItems() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        val scenario = SimpleShopFragment()
        //naar shop navigeren
        onView(withId(R.id.shipmentButton)).perform(click())

//        onView(withId(R.id.product_list)).perform(RecyclerViewActions.actionOnItemAtPosition<ProductItemViewHolder>(0, click()))
//        onView(withId(R.id.product_list)).perform(RecyclerViewActions.actionOnItemAtPosition<ProductItemViewHolder>(0, typeText(String.valueOf(1))))
//        onView(withId(R.id.product_list)).perform(RecyclerViewActions.actionOnItemAtPosition<ProductItemViewHolder>(0, click()))
        //naar winkelwagen navigeren

        onView(withId(R.id.bekijkWinkelwagen)).perform(click())
        //naar adresScherm navigeren
        onView(withId(R.id.bestelProducten)).perform(click())
        onView(withId(R.id.cartFragment)).check(matches(ViewMatchers.isDisplayed()))
//        //adres gegevens ingeven
//        onView(withId(R.id.textStraat)).perform(typeText(straat))
//        onView(withId(R.id.textHuisNr)).perform(typeText(huisNr))
//        onView(withId(R.id.textPostcode)).perform(typeText(postCod))
//        onView(withId(R.id.textStad)).perform(typeText(stad))
//        //navigeer naar schipmentScherm
//        onView(withId(R.id.adresIngevuld)).perform(click())
//        //controleer adres
//        onView(withId(R.id.shipmentText)).check(matches(withText("$straat $huisNr, $postCod $stad")))
    }

}