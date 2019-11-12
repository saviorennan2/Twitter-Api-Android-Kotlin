package com.android.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.R
import com.android.model.BaseListModel

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun viewHolderfactory(inflater: LayoutInflater, parent: ViewGroup, type: Int) : BaseViewHolder {
            return when (type) {
                BaseListModel.TYPE_TEXT -> TextTweetViewHolder(inflater.inflate(R.layout.list_item_search_regular, parent, false))
                else -> {
                    TextTweetViewHolder(inflater.inflate(R.layout.list_item_search_regular, parent, false))
                }
            }
        }
    }

}