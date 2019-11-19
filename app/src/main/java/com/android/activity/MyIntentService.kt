package com.android.activity

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.R
import com.android.SQLiteRepository
import com.android.adapter.SearchListAdapter
import com.android.common.*
import com.android.common.PreferenceHelper.get
import com.android.common.PreferenceHelper.set
import com.android.model.SearchResponseModel
import com.android.model.TokenResponseModel
import com.android.network.ApiService
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyIntentService : IntentService("ServiceTwitter"){

    private var tweetsRepository: SQLiteRepository? = null

    override fun onHandleIntent(intent: Intent?) {

        tweetsRepository= SQLiteRepository(this)

        getData("androidbti")

    }

    private fun getData(queryText: String) {

        tweetsRepository?.cleanTables()

        if (!Util.isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show()
            return
        }

        val prefs = PreferenceHelper.defaultPrefs(this)
        val accessToken: String? = prefs[PREF_TWITTER_ACCESS_TOKEN, ""]

        val searchCall = ApiService.instance.getTweetList("Bearer $accessToken", queryText)
        searchCall.enqueue(object : Callback<SearchResponseModel> {
            override fun onFailure(call: Call<SearchResponseModel>, t: Throwable) {

                Toast.makeText(applicationContext, getString(R.string.handle_twitter_token_error), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(call: Call<SearchResponseModel>, response: Response<SearchResponseModel>) {

                var tweets = response.body()?.statuses;

                for (tweet in tweets!! ){
                    tweetsRepository?.save(tweet)
                }
                Log.d("tweets", tweets?.get(0)?.text)

            }
        })
    }




}