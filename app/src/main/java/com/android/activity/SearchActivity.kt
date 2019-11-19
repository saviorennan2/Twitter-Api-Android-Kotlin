package com.android.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.R
import com.android.SQLiteRepository
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

    var tweets : List<Tweet>? = null
    private var tweetsRepository: SQLiteRepository? = null

    private val tweetItemClicked: (tweet: Tweet) -> Unit = {
        DetailActivity.start(this@SearchActivity, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val prefs = PreferenceHelper.defaultPrefs(this)
        val accessToken: String? = prefs[PREF_TWITTER_ACCESS_TOKEN, ""]

        if (!Util.isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show()
            return
        }

        if (accessToken.isNullOrEmpty()) {
            getToken()
        }

        startService(Intent(this,MyIntentService::class.java))

        tweetsRepository= SQLiteRepository(this)

        updateList()


    }

    private fun list(tweets:MutableList<Tweet>){
        this.tweets  = tweets
//        Precisa reavaliar esse código para não criar sempre uma instância do adapter
                        searchRecyclerView.adapter =
                    SearchListAdapter(this@SearchActivity, this.tweets, tweetItemClicked)
                searchRecyclerView.addItemDecoration(DividerItemDecoration(this@SearchActivity, LinearLayout.VERTICAL))

    }

    fun updateList(){
        tweetsRepository?.list { list(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if(item.itemId == R.id.action_update){
            updateList()
        }

        return true
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

                } else if (newText.isEmpty()) {
                    resultNotFoundView.visibility = View.GONE
                }
                return false
            }

            override fun onQueryTextSubmit(query: String) = false

        })

        return super.onCreateOptionsMenu(menu)

    }

    private fun getToken() {

        val tokenCall = ApiService.instance.getToken("Basic " + Util.getBase64String(
            BEARER_TOKEN_CREDENTIALS
        ), GRANT_TYPE
        )
        tokenCall.enqueue(object : Callback<TokenResponseModel> {
            override fun onFailure(call: Call<TokenResponseModel>, t: Throwable) {
                Toast.makeText(this@SearchActivity, getString(R.string.handle_twitter_token_error), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(
                call: Call<TokenResponseModel>,
                response: Response<TokenResponseModel>
            ) {
                val prefs = PreferenceHelper.defaultPrefs(applicationContext)
                prefs[PREF_TWITTER_TOKEN_TYPE] = response.body()?.tokenType
                prefs[PREF_TWITTER_ACCESS_TOKEN] = response.body()?.accessToken
            }

        })

    }


}