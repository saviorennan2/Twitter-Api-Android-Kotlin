package com.android.network

import com.android.common.BASE_URL
import com.android.common.OAUTH2_TOKEN_SUFFIX
import com.android.common.SEARCH_SUFFIX
import com.android.model.SearchResponseModel
import com.android.model.TokenResponseModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST(OAUTH2_TOKEN_SUFFIX)
    fun getToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String
    ): Call<TokenResponseModel>


    @GET(SEARCH_SUFFIX)
    fun getTweetList(
        @Header("Authorization") authorization: String,
        @Query("q") queryText: String
    ): Call<SearchResponseModel>


    companion object {
        val instance: ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            retrofit.create(ApiService::class.java)
        }

    }
}