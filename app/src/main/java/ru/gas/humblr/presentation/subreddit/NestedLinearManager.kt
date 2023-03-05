package ru.gas.humblr.presentation.subreddit

import android.view.View

interface NestedLinearManager {
    fun findFirstVisibleItemPosition(): Int
    fun findLastVisibleItemPosition(): Int
    fun findViewByPosition(i: Int): View?
}