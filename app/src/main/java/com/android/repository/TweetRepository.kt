package com.android

import com.android.model.Tweet


interface TweetRepository {

    fun save(tweet: Tweet)
    fun remove(tweet: Tweet)
    fun list(callback:(MutableList<Tweet>) -> Unit)
}




