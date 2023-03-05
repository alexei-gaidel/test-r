package ru.gas.humblr.presentation.subreddit

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.gas.humblr.databinding.FragmentSubredditBinding
import ru.gas.humblr.domain.model.LoadingState
import ru.gas.humblr.domain.model.SubredditPostsItem

@AndroidEntryPoint
class SubredditFragment : Fragment() {
    private var _binding: FragmentSubredditBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SubredditFragment()
    }

    private val viewModel: SubredditViewModel by viewModels()
    val adapter = SubredditPostsAdapter({ subreddit -> onItemClick(subreddit) })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSubredditBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subredditId = arguments?.getString("subredditId")
        binding.toolbarTitle.text = String.format(resources.getString(ru.gas.humblr.R.string.toolbar_feed), subredditId)


        binding.myToolbar.setNavigationOnClickListener { _ ->
//            findNavController().navigate(ru.gas.humblr.R.id.action_navigation_subreddit_to_navigation_home)
            findNavController().popBackStack(ru.gas.humblr.R.id.navigation_subreddit, inclusive = true)
        }

        binding.informationImage.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("subredditId", subredditId)
            findNavController().navigate(ru.gas.humblr.R.id.action_navigation_subreddit_to_navigation_subreddit_description, bundle)
        }


        if (subredditId != null) {
            viewModel.loadSubredditPosts(subredditId)
        }
        viewModel.subredditPosts.onEach {
            adapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.subredditRecycler.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState.collect { state ->
                when (state) {

                    is LoadingState.Loading -> {
                        binding.postsProgress.isVisible = true
                    }
                    is LoadingState.Success -> {
                        binding.postsProgress.isVisible = false
                    }
                    is LoadingState.Error -> {
                        binding.postsProgress.isVisible = false

                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()

                    }
                }

            }
        }

    }

        fun onItemClick(item: SubredditPostsItem) {
            val bundle = Bundle()
            bundle.putString("postId", item.postId)
            findNavController().navigate(ru.gas.humblr.R.id.action_navigation_subreddit_to_navigation_subreddit_post, bundle)

        }

//
//
//
//
//        val layoutManager = NestedLinearLayoutManager(context)
//        binding.subredditRecycler.layoutManager = layoutManager
//
//        // specify an adapter (see also next example)
//        val commentDataList = generateComments()
//        val mAdapter = CommentsAdapter(commentDataList)
//        binding.subredditRecycler.adapter = mAdapter
//        binding.subredditRecycler.addOnScrollListener(
//            OnNestedScrollListener(
//                context,
//                layoutManager,
//                commentDataList
//            )
//        )
//    }
//
//    private fun generateComments(): List<ru.gas.humblr.data.remote.models.CommentData> {
//        val commentDataList: ArrayList<ru.gas.humblr.data.remote.models.CommentData> = ArrayList()
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1",
//                "0",
//                R.drawable.ic_dialog_email,
//                "Michael Jackson",
//                "1. She was more like a beauty queen from a movie scene I said don't mind, but what do you mean I am the one Who will dance on the floor in the round She said I am the one who will dance on the floor in the round"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.1",
//                "1",
//                R.drawable.ic_dialog_email,
//                "John Lennon",
//                "1.1 Imagine there's no heaven It's easy if you try No hell below us Above us only sky Imagine all the people Living for today... Imagine there's no countries It isn't hard to do Nothing to kill or die for And no religion too Imagine all the people Living life in peace..."
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.2",
//                "1",
//                R.drawable.ic_dialog_email,
//                "Led Zeppelin",
//                "1.2 And as we wind on down the road Our shadows taller than our souls There walks a lady we all know Who shines white light and wants to show How everything still turns to gold And if you listen very hard The tune will come to you at last When all is one and one is all To be a rock and not to roll"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.2.1",
//                "1.2",
//                R.drawable.ic_dialog_email,
//                "Queen",
//                "1.2.1 This song actually tells a story (so I've heard) The protagonist (we shall name him Fred) is in love with a girl, but her heart is stolen by a crime lord. Enraged, Fred kills the crime lord. He's had his revenge, but now the entire mob is after him, so he must flee the country. He says goodbye to his mother (one of the most moving goodbye's in history) and sets off in hope of escaping the mob's wrath. Unfortunately, in vain: The mob catches up with him at last. He pleads for mercy (mama mia! Mama mia! Mama mia let me go) but of course he is cruelly refused (bismillah, no! We will not let you go)"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.2.2",
//                "1.2",
//                R.drawable.ic_dialog_email,
//                "Keane",
//                "1.2.2 Although this song can be interpreted in a million and one ways, the lyrics are absolutely amazing. They can represent so many different times during a life, which allows basically everyone to be able to relate to it.M+8 This song remembering me about we life for god and if we dead, there we have the next life in heaven because we life in earth only a few years but in heaven we life forever.. \"This can be the end of everything, So Why don't we go to Somewhere Only We Know\"! Always trust the GOD!"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.2.2.1",
//                "1.2.2",
//                R.drawable.ic_dialog_email,
//                "Bob Dylan",
//                "1.2.2.1 No one can beat Bob Dylan when it comes to lyrics. After a point of time they aren't even song lyrics, they are sheer poetry.. Whats disappointing is that they haven't mentioned 'Mr Tambourine Man' by him HM+19 How amazing that Michael Jackson beats Bobby in this poll. Dylan was a genius, and probably the most impressive song writer that I have ever had the privilege of listening toM+12 Bob Dylan is truly a legend when it comes to music and poetry, I'm surprised this song of his has not made the top tens as of yet. I also listen to Michael Jackson and John Lennon, but Bob tops them both with lyrical brilliance shining through his work."
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.2.2.1.1",
//                "1.2.2.1",
//                R.drawable.ic_dialog_email,
//                "Led Zeppelin",
//                "1.2.2.1.1 And as we wind on down the road Our shadows taller than our souls There walks a lady we all know Who shines white light and wants to show How everything still turns to gold And if you listen very hard The tune will come to you at last When all is one and one is all To be a rock and not to roll"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.2.2.1.1.1",
//                "1.2.2.1.1",
//                R.drawable.ic_dialog_email,
//                "Queen",
//                "1.2.2.1.1.1 This song actually tells a story (so I've heard) The protagonist (we shall name him Fred) is in love with a girl, but her heart is stolen by a crime lord. Enraged, Fred kills the crime lord. He's had his revenge, but now the entire mob is after him, so he must flee the country. He says goodbye to his mother (one of the most moving goodbye's in history) and sets off in hope of escaping the mob's wrath. Unfortunately, in vain: The mob catches up with him at last. He pleads for mercy (mama mia! Mama mia! Mama mia let me go) but of course he is cruelly refused (bismillah, no! We will not let you go)"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.3",
//                "1",
//                R.drawable.ic_dialog_email,
//                "Michael Jackson",
//                "1.3 And The Dream We Were Conceived In Will Reveal A Joyful Face And The World We Once Believed In Will Shine Again In Grace Then Why Do We Keep Strangling Life Wound This Earth Crucify Its Soul Though It's Plain To See This World Is Heavenly Be God's Glow"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.4",
//                "1",
//                R.drawable.ic_dialog_email,
//                "Don McLean",
//                "1.4 And in the streets the children screamed The lovers cried and the poets dreamed But not a word was spoken The church bells all were broken And the three men I admire most The Father Son and Holy Ghost They caught the last train for the coast The day the music died"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.4.1",
//                "1.4",
//                R.drawable.ic_dialog_email,
//                "Michael Jackson",
//                "1.4.1 She was more like a beauty queen from a movie scene I said don't mind, but what do you mean I am the one Who will dance on the floor in the round She said I am the one who will dance on the floor in the round"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "1.4.2",
//                "1.4",
//                R.drawable.ic_dialog_email,
//                "John Lennon",
//                "1.4.2 Imagine there's no heaven It's easy if you try No hell below us Above us only sky Imagine all the people Living for today... Imagine there's no countries It isn't hard to do Nothing to kill or die for And no religion too Imagine all the people Living life in peace..."
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "2",
//                "0",
//                R.drawable.ic_dialog_email,
//                "Led Zeppelin",
//                "2 And as we wind on down the road Our shadows taller than our souls There walks a lady we all know Who shines white light and wants to show How everything still turns to gold And if you listen very hard The tune will come to you at last When all is one and one is all To be a rock and not to roll"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "2.1",
//                "2",
//                R.drawable.ic_dialog_email,
//                "Queen",
//                "2.1 This song actually tells a story (so I've heard) The protagonist (we shall name him Fred) is in love with a girl, but her heart is stolen by a crime lord. Enraged, Fred kills the crime lord. He's had his revenge, but now the entire mob is after him, so he must flee the country. He says goodbye to his mother (one of the most moving goodbye's in history) and sets off in hope of escaping the mob's wrath. Unfortunately, in vain: The mob catches up with him at last. He pleads for mercy (mama mia! Mama mia! Mama mia let me go) but of course he is cruelly refused (bismillah, no! We will not let you go)"
//            )
//        )
//        commentDataList.add(
//            ru.gas.humblr.data.remote.models.CommentData(
//                "2.2",
//                "2",
//                R.drawable.ic_dialog_email,
//                "Keane",
//                "2.2 Although this song can be interpreted in a million and one ways, the lyrics are absolutely amazing. They can represent so many different times during a life, which allows basically everyone to be able to relate to it.M+8 This song remembering me about we life for god and if we dead, there we have the next life in heaven because we life in earth only a few years but in heaven we life forever.. \"This can be the end of everything, So Why don't we go to Somewhere Only We Know\"! Always trust the GOD!"
//            )
//        )
//        return commentDataList
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}







