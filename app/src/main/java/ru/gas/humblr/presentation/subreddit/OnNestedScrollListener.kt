package ru.gas.humblr.presentation.subreddit

import android.content.Context
import androidx.recyclerview.widget.RecyclerView


class OnNestedScrollListener(
    context: Context?,
    layoutManager: NestedLinearManager?,
    commentDataList: List<NestedItem>?
) :
    RecyclerView.OnScrollListener() {
    private val nestedScrollHelper: NestedScrollHelper

    init {
        nestedScrollHelper = NestedScrollHelper(context!!, layoutManager!!, commentDataList)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        nestedScrollHelper.onScrolled()
    }
}