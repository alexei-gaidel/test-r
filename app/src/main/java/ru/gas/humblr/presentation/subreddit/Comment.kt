package ru.gas.humblr.presentation.subreddit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class Comment : LinearLayout {
    private var avatar: ImageView? = null
    private var comment: TextView? = null
    private var name: TextView? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val rootView: View = LayoutInflater.from(context).inflate(ru.gas.humblr.R.layout.comment_view, this)
        avatar = rootView.findViewById<View>(ru.gas.humblr.R.id.avatar) as ImageView
        name = rootView.findViewById<View>(ru.gas.humblr.R.id.name) as TextView
        comment = rootView.findViewById<View>(ru.gas.humblr.R.id.comment) as TextView
    }

    fun setAvatar(img: Int) {
        avatar?.setImageResource(img)
    }

    fun setName(commentName: String?) {
        name!!.text = commentName
    }

    fun setComment(com: String?) {
        comment!!.text = com
    }
}