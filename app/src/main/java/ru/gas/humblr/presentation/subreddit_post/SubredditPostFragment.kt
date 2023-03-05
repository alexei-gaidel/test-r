package ru.gas.humblr.presentation.subreddit_post

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.gas.humblr.R
import ru.gas.humblr.databinding.FragmentSubredditDescriptionBinding
import ru.gas.humblr.databinding.FragmentSubredditPostBinding
import ru.gas.humblr.domain.utils.AppUtils
import ru.gas.humblr.presentation.subreddit_description.SubredditDescriptionViewModel

@AndroidEntryPoint
class SubredditPostFragment : Fragment(), AppUtils {

    private var _binding: FragmentSubredditPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubredditPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSubredditPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = arguments?.getString("postId")
        Log.d("eee", "postId $postId")
        postId?.let {
            viewModel.loadComments(it)
            viewModel.loadPost(it)
        }

        binding.myToolbar.setNavigationOnClickListener { _ ->
            findNavController().popBackStack(
                ru.gas.humblr.R.id.navigation_subreddit_post,
                inclusive = true
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.post.collect { post ->
                Log.d("eee", "post $post")

                binding.postTitle.text = post?.title
                binding.author.text = post?.author
                binding.score.text = post?.score.toString()
                post?.body?.let {
                    binding.fullPost.isVisible = true
                    binding.fullPost.text = it
                }
                post?.image?.let {
                    binding.titleImg.isVisible = true
                    Glide
                        .with(binding.titleImg.context)
                        .load(it)
                        .centerCrop()
                        .into(binding.titleImg)
                }


                val comments = getFormatedNumber(post?.numComments ?: 0)
                binding.commentsCount.text = String.format(
                    resources.getString(R.string.comments),
                    comments
                )

                post?.created?.let { dateLong ->
                    binding.created.text = String.format(
                        resources.getString(
                            R.string.created,
                            dateLong.toDate()
                        )
                    )
                }


            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}