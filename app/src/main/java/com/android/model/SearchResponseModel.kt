package com.android.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResponseModel(val statuses: List<Tweet>?) : Parcelable