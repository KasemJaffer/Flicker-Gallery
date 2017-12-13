package com.tigerspike.flickergallery.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.tigerspike.flickergallery.R
import com.tigerspike.flickergallery.entities.FeedItem
import kotlinx.android.synthetic.main.item.view.*


class FeedsAdapter(val context: Context, mItems: List<FeedItem>?)
    : RecyclerView.Adapter<FeedsAdapter.SimpleViewHolder>() {

    private var lastPosition = -1
    private val glide: RequestManager = Glide.with(context)
    var onItemClickListener: OnItemClickListener? = null

    var items: List<FeedItem>? = mItems
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return SimpleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val feedItem = items!![position]
        glide.load(feedItem.media!!.url).into(holder.view.image)
        animateItem(position, holder)
    }

    //This will animate the item transition from bottom to top
    private fun animateItem(position: Int, holder: SimpleViewHolder) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom)
            holder.view.startAnimation(animation)
            lastPosition = position
        }
    }

    inner class SimpleViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.card.setOnClickListener {
                onItemClickListener?.onItemClicked(view.image, adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}
