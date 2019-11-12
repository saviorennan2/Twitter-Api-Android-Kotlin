package com.android.model

import com.google.gson.annotations.SerializedName

data class TokenResponseModel(@SerializedName("token_type") val tokenType: String?,
                              @SerializedName("access_token") val accessToken: String?
)