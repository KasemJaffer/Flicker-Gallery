package com.tigerspike.flickergallery.ui.splash

import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.tigerspike.flickergallery.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule<SplashActivity>(SplashActivity::class.java)

    @Test
    fun navigateToMainActivityTest() {

        Intents.init()
        //Wait for data to load
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(4))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //Check if we are in the Main screen
        intended(hasComponent(MainActivity::class.java.name))
        Intents.release()
    }
}