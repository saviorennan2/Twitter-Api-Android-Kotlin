package com.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.holder.BaseViewHolder
import com.android.holder.TextTweetViewHolder
import com.android.model.BaseListModel
import com.android.model.Tweet

class SearchListAdapter(
    context: Context?,
    private val itemList: List<BaseListModel>?,
    private val itemClicked: (tweet: Tweet) -> Unit)
    : RecyclerView.Adapter<BaseViewHolder>() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.viewHolderfactory(inflater, parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (itemList?.get(position)?.type) {
            BaseListModel.TYPE_TEXT -> (holder as TextTweetViewHolder).bindViews(itemList[position] as Tweet, itemClicked)
        }
    }

    override fun getItemCount() = itemList?.count() ?: 0

    override fun getItemViewType(position: Int) = itemList?.get(position)?.type ?: BaseListModel.TYPE_EMPTY
}