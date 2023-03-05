package ru.gas.humblr.presentation.subreddit

import android.content.Context
import android.util.Log
import android.view.View
import ru.gas.humblr.R


class NestedScrollHelper(
    context: Context,
    layoutManager: NestedLinearManager,
    commentDataList: List<NestedItem>?
) {
    private val FOCUS_VIEW_SLIDE_END: Int
    private val FOCUS_VIEW_SLIDE_START: Int
    private val NESTING_GAP: Int
    private val MAX_VISIBLE_NESTING: Int
    private var commentDataList: List<NestedItem>? = null
    private val layoutManager: NestedLinearManager
    private val nestingMap: HashMap<Any?, Int> = HashMap()

    init {
        FOCUS_VIEW_SLIDE_START = context.resources
            .getDimensionPixelSize(R.dimen.nesting_scroll_focus_view_slide_start)
        FOCUS_VIEW_SLIDE_END = context.resources
            .getDimensionPixelSize(R.dimen.nesting_scroll_focus_view_slide_end)
        NESTING_GAP = context.resources.getDimensionPixelSize(R.dimen.nesting_scroll_gap_size)
        MAX_VISIBLE_NESTING = 3 * NESTING_GAP
        this.layoutManager = layoutManager
        onDataSetChange(commentDataList)
    }

    fun onDataSetChange(commentDataList: List<NestedItem>?) {
        this.commentDataList = commentDataList
        invalidateNestingMap()
    }

    private fun invalidateNestingMap() {
        nestingMap.clear()
        for (data in commentDataList!!) {
            val parentNesting = nestingMap[data.nestedItemParentId]
            val childNesting = if (parentNesting == null) 0 else parentNesting + 1
            nestingMap[data.nestedItemId] = childNesting
        }
    }

    fun onScrolled() {
        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        val lastPosition = layoutManager.findLastVisibleItemPosition()
        var focusedViewNesting = 0
        var focusedViewShifting = 0
        var showParentHint = false

        // get focused view
//        if (firstPosition != lastPosition) {
//            var focusedView: View? = null
//            var focusedViewPosition = 0
//            for (i in firstPosition..lastPosition) {
//                val focusedViewCandidate: View? = layoutManager.findViewByPosition(i)
//                if (focusedViewCandidate != null) {
//                    if (focusedViewCandidate.top < FOCUS_VIEW_SLIDE_START) {
//                        focusedView = focusedViewCandidate
//                        focusedViewPosition = i
//                    } else {
//                        break
//                    }
//                }
//            }
//            focusedViewNesting = nestingMap[commentDataList!![focusedViewPosition].nestedItemId]!!

//            // calc view shifting
//            if (focusedView != null) {
//                if (focusedView.top > FOCUS_VIEW_SLIDE_END) {
//                    val nesting =
//                        nestingMap[commentDataList!![focusedViewPosition - 1].nestedItemId]
//                    val aboveViewNesting = nesting!! - focusedViewNesting
//                    val shiftingPercentage =
//                        (focusedView.top
//                                - FOCUS_VIEW_SLIDE_END).toFloat() / (FOCUS_VIEW_SLIDE_START - FOCUS_VIEW_SLIDE_END)
//                    focusedViewShifting =
//                        -(shiftingPercentage * aboveViewNesting * NESTING_GAP).toInt()
//                }
//            }

            // calc parent hint
//            val firstFocusNesting = nestingMap[commentDataList!![firstPosition].nestedItemId]!!
//            showParentHint = focusedViewNesting < firstFocusNesting
//        }

        // adjust views translationX
        for (i in firstPosition..lastPosition) {
            val view: View? = layoutManager.findViewByPosition(i)
            val nesting = nestingMap[commentDataList!![i].nestedItemId]
            val viewNesting = nesting!! - focusedViewNesting
            val absoluteTranslationX = NESTING_GAP * viewNesting + focusedViewShifting
            val relativeTranslationX =
                absoluteTranslationX.coerceAtMost(MAX_VISIBLE_NESTING)
                    .coerceAtLeast(-MAX_VISIBLE_NESTING)
            view?.setPadding(relativeTranslationX, 0, 0, 0)

//            view?.layoutParams?.width = view?.measuredWidth?.plus(relativeTranslationX)

//            view?.translationX = relativeTranslationX.toFloat()
            Log.d("ddd", "id $i, relative translation $relativeTranslationX, measured width ${view?.measuredWidth}, view width ${view?.width}")
        }
    }
}