package ru.gas.humblr.presentation.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gas.humblr.R
import ru.gas.humblr.databinding.SubredditListItemBinding
import ru.gas.humblr.domain.model.SubredditListItem


class NewSubredditListAdapter(
    private val onClick: (SubredditListItem) -> Unit,
    private val onSubscribeClick: (Boolean, SubredditListItem) -> Unit
) : RecyclerView.Adapter<SubredditListViewHolder>() {

    private var data: List<SubredditListItem> = emptyList()

    fun setData(data: List<SubredditListItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListViewHolder {
        val binding =
            SubredditListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubredditListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubredditListViewHolder, position: Int) {
        val item = data.getOrNull(position)
        with(holder.binding) {
            var isSubscribed = item?.subscribed
            author.text = item?.author
            subredditTitle.text = item?.title

            if (isSubscribed == true) {
                subscribeButton.setImageResource(R.drawable.subscribed_icon)
            } else {
                subscribeButton.setImageResource(R.drawable.subscribe_icon)
            }

            subscribeButton.setOnClickListener {
                item?.let {

                    if (isSubscribed == true) {
                        onSubscribeClick(true, item)
                        subscribeButton.setImageResource(R.drawable.subscribe_icon)
                        isSubscribed = !isSubscribed!!
                    } else {
                        onSubscribeClick(false, item)
                        subscribeButton.setImageResource(R.drawable.subscribed_icon)
                        isSubscribed = !isSubscribed!!
                    }
                }
            }

            if (item?.img != null) {
                subredditPreview.isVisible = true
                Glide
                    .with(subredditPreview.context)
                    .load(item.img)
                    .centerCrop()
                    .into(subredditPreview)
            }


            holder.binding.root.setOnClickListener {
                item?.let {
                    onClick(item)
                }
            }

        }
    }


    override fun getItemCount(): Int = data.size
}


class SubredditListViewHolder(val binding: SubredditListItemBinding) :
    RecyclerView.ViewHolder(binding.root)