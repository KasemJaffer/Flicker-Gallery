package com.tigerspike.flickergallery.ui.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.tigerspike.flickergallery.R
import kotlinx.android.synthetic.main.activity_details.*

/**
 * This view is to show one feed.
 */
class DetailsActivity : AppCompatActivity() {

    companion object {
        val EXTRA_PIC_URL = "feed_image_url"
        val EXTRA_TITLE = "feed_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        title_tv.text = intent.getStringExtra(EXTRA_TITLE)
        Glide.with(this).load(intent.getStringExtra(EXTRA_PIC_URL)).into(image)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
