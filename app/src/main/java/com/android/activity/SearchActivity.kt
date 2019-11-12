package com.android.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.R
import com.android.adapter.SearchListAdapter
import com.android.common.*
import com.android.common.PreferenceHelper.defaultPrefs
import com.android.common.PreferenceHelper.get
import com.android.common.PreferenceHelper.set
import com.android.model.SearchResponseModel
import com.android.model.TokenResponseModel
import com.android.model.Tweet
import com.android.network.ApiService


import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private val tweetItemClicked: (tweet: Tweet) -> Unit = {
        DetailActivity.start(this@SearchActivity, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val prefs = defaultPrefs(this)
        val accessToken: String? = prefs[PREF_TWITTER_ACCESS_TOKEN, ""]

        if (!Util.isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show()
            return
        }

        if (accessToken.isNullOrEmpty()) {
            getToken()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)

        val searchManager = this@SearchActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this@SearchActivity.componentName))
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length > 2) {

                        getData(newText)

                } else if (newText.isEmpty()) {
                    resultNotFoundView.visibility = View.GONE
                }
                return false
            }

            override fun onQueryTextSubmit(query: String) = false

        })

        return super.onCreateOptionsMenu(menu)

    }

    private fun getData(queryText: String) {

        if (!Util.isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show()
            return
        }


            resultNotFoundView.visibility = View.GONE
            searchOverlayProgressBar.show()


        val prefs = defaultPrefs(this)
        val accessToken: String? = prefs[PREF_TWITTER_ACCESS_TOKEN, ""]

        val searchCall = ApiService.instance.getTweetList("Bearer $accessToken", queryText)
        searchCall.enqueue(object : Callback<SearchResponseModel> {
            override fun onFailure(call: Call<SearchResponseModel>, t: Throwable) {

                    searchOverlayProgressBar.stop()

                Toast.makeText(this@SearchActivity, getString(R.string.handle_twitter_token_error), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(call: Call<SearchResponseModel>, response: Response<SearchResponseModel>) {
                searchRecyclerView.adapter =
                        SearchListAdapter(this@SearchActivity, response.body()?.statuses, tweetItemClicked)
                searchRecyclerView.addItemDecoration(DividerItemDecoration(this@SearchActivity, LinearLayout.VERTICAL))

                    searchOverlayProgressBar.stop()
                    if (response.body()?.statuses?.count() ?: 0 < 1) {
                        resultNotFoundView.visibility = View.VISIBLE
                    }

            }
        })
    }

    private fun getToken() {

        val tokenCall = ApiService.instance.getToken("Basic " + Util.getBase64String(BEARER_TOKEN_CREDENTIALS), GRANT_TYPE)
        tokenCall.enqueue(object : Callback<TokenResponseModel> {
            override fun onFailure(call: Call<TokenResponseModel>, t: Throwable) {
                Toast.makeText(this@SearchActivity, getString(R.string.handle_twitter_token_error), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(
                call: Call<TokenResponseModel>,
                response: Response<TokenResponseModel>
            ) {
                val prefs = defaultPrefs(this@SearchActivity)
                prefs[PREF_TWITTER_TOKEN_TYPE] = response.body()?.tokenType
                prefs[PREF_TWITTER_ACCESS_TOKEN] = response.body()?.accessToken
            }

        })

    }
}