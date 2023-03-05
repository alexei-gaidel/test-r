package ru.gas.humblr.presentation.subreddit


class CommentData(
    var id: String,
    var parentId: String,
    var avatar: Int,
    var name: String,
    var comment: String
) :
    NestedItem {

    override val nestedItemId: Any
        get() = id
    override val nestedItemParentId: Any
        get() = parentId
}