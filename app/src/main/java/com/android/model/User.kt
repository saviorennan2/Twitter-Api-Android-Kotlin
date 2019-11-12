package com.android.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id_str") val id: String?,
    val name: String?,
    @SerializedName("screen_name") val screenName: String?,
    @SerializedName("profile_image_url") val profileImageUrl: String?
) : Parcelable