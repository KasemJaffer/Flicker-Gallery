package com.tigerspike.flickergallery.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Toast
import com.tigerspike.flickergallery.R
import com.tigerspike.flickergallery.entities.FeedItem
import com.tigerspike.flickergallery.repository.FlickerRepository
import com.tigerspike.flickergallery.ui.details.DetailsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), FeedsAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private var flickerRepository = FlickerRepository()
    private var feeds: List<FeedItem>? = null
        set(value) {
            field = value

            //For each set of the feeds we attach it the adapter so the list can be redrawn
            val adapter = FeedsAdapter(this, feeds)
            adapter.onItemClickListener = this
            list.adapter = adapter

            //Show the retry button so the user can retry fetching feeds
            if (feeds == null) {
                retry.visibility = View.VISIBLE
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = GridLayoutManager(this, 2)

        //To show the effect of staggered list we give each third item a 2 span width
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 3 == 0) 2 else 1
            }
        }
        list.layoutManager = layoutManager
        refresh.setOnRefreshListener(this)
        fetchFeeds()
    }

    //When the user clicks on the feed we will smooth transition to the details activity
    override fun onItemClicked(view: View, position: Int) {
        val feedItem = feeds!!.get(position)
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_PIC_URL, feedItem.media?.url)
        intent.putExtra(DetailsActivity.EXTRA_TITLE, feedItem.title)
        startActivity(intent, ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view, getString(R.string.image_trans_name)).toBundle())
    }

    //Called when the user pull to refresh
    override fun onRefresh() {
        fetchFeeds()
    }

    fun onRetryButtonClicked(view: View) {
        fetchFeeds()
    }

    //Calls the api to get live feeds
    private fun fetchFeeds() {
        retry.visibility = View.GONE
        refresh.isRefreshing = true

        flickerRepository.feeds
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response, error ->
                    refresh.isRefreshing = false

                    if (error != null || !response.isSuccessful || response.body() == null) {
                        Toast.makeText(this@MainActivity, (error?.message ?: "Unable to get feeds."),
                                Toast.LENGTH_LONG).show()
                        feeds = null
                        return@subscribe
                    }
                    feeds = response.body()!!.items
                })
    }
}
