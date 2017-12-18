package com.tigerspike.flickergallery.ui.details

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.tigerspike.flickergallery.R
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DetailsActivityTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule<DetailsActivity>(DetailsActivity::class.java)

    @Test
    fun titleBoundTest() {
        val intent = mActivityTestRule.activity.intent
        val title = intent.getStringExtra(DetailsActivity.EXTRA_TITLE)

        //Check if the
        onView(allOf<View>(withId(R.id.title_tv), withText(title), isDisplayed()))
    }
}