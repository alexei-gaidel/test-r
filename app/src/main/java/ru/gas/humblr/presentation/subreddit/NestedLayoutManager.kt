package ru.gas.humblr.presentation.subreddit

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager


class NestedLinearLayoutManager : LinearLayoutManager, NestedLinearManager {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }
}