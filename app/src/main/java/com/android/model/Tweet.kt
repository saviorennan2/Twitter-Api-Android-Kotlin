package com.android.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tweet(
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("id_str") val id: String?,
    val text: String?,
    @SerializedName("in_reply_to_status_id_str") val inReplyToStatusId: String?,
    @SerializedName("in_reply_to_user_id_str") val inReplyToUserId: String?,
    @SerializedName("in_reply_to_screen_name") val inReplyToScreenName: String?,
    val user: User?
) : Parcelable, BaseListModel {
    override val type: Int
        get() = BaseListModel.TYPE_TEXT
}