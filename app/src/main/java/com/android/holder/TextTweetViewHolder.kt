package com.android.holder
import android.view.View
import com.android.model.Tweet

import kotlinx.android.synthetic.main.list_item_search_regular.view.*

class TextTweetViewHolder(itemView: View) : BaseViewHolder(itemView) {

    val userPhoto = itemView.itemSearchUserPhoto
    val userName = itemView.itemSearchUserName
    val userScreenName = itemView.itemSearchScreenName
    val date = itemView.itemSearchDate
    val desc = itemView.itemSearchDesc

    fun bindViews(tweet: Tweet, itemClicked: (tweet: Tweet) -> Unit) {

        userName.text = tweet.user?.name ?: ""
        userScreenName.text = "@" + tweet.user?.screenName
        desc.text = tweet.text

        itemView.setOnClickListener {
            itemClicked(tweet)
        }
    }
}