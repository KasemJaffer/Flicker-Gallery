package com.tigerspike.flickergallery.ui.main

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.tigerspike.flickergallery.R
import com.tigerspike.flickergallery.ui.details.DetailsActivity
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun itemClickTest() {

        //Wait for the data to load
        delay(10)

        val recyclerView = mActivityTestRule.activity.findViewById(R.id.list) as RecyclerView

        //Assert adapter is not null and not empty
        val adapter = recyclerView.adapter as? FeedsAdapter
        assertThat(adapter, notNullValue())
        assertThat(adapter?.items, notNullValue())
        assertThat(adapter?.items?.size, greaterThan(0))

        Intents.init()

        //Click on the first item
        onView(withId(R.id.list)).perform(
                RecyclerViewActions.actionOnItemAtPosition<FeedsAdapter.SimpleViewHolder>(0, click()))

        //Wait for the new activity to open
        delay(2)


        //Check if the we started the activity with the same item data
        val feed = adapter?.items!![0]
        intended(allOf(hasComponent(DetailsActivity::class.java.name),
                hasExtra(DetailsActivity.EXTRA_PIC_URL, feed.media?.url),
                hasExtra(DetailsActivity.EXTRA_TITLE, feed.title)))

        Intents.release()

    }

    private fun delay(seconds: Long) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}