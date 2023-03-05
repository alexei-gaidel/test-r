package ru.gas.humblr.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.gas.humblr.R
import ru.gas.humblr.databinding.FragmentHomeBinding
import ru.gas.humblr.domain.model.LoadingState
import ru.gas.humblr.domain.model.SubredditListItem

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val viewModel: HomeViewModel by viewModels()
    val adapter = NewSubredditListAdapter({ subreddit -> onItemClick(subreddit) },
        { isSubscribed, subreddit -> onSubscribeClick(isSubscribed, subreddit) })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadNewSubreddits()
        viewModel.newSubreddits.onEach {
            adapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.recycler.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState.collect { state ->
                when (state) {

                    is LoadingState.Loading -> {
                        binding.progress.isVisible = true
                    }
                    is LoadingState.Success -> {
                        binding.progress.isVisible = false
                    }
                    is LoadingState.Error -> {
                        binding.progress.isVisible = false

                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()

                    }
                }

            }
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab?.text == getString(R.string.new_string)) {
                    viewModel.loadNewSubreddits()
                    viewModel.newSubreddits.onEach {
                        adapter.setData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                else {
                viewModel.loadPopularSubreddits()
                viewModel.popularSubreddits.onEach {
                    adapter.setData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
    }


    fun onSubscribeClick(isSubscribed: Boolean, subreddit: SubredditListItem) {

        if (isSubscribed) {
            viewModel.getUserUnsubscribed(subreddit.id)

        } else {
            viewModel.getUserSubscribed(subreddit.id)

        }
    }

    fun onItemClick(item: SubredditListItem) {
        val bundle = Bundle()
        bundle.putString("subredditId", item.id)
        findNavController().popBackStack(R.id.navigation_subreddit, true)
        findNavController().navigate(R.id.action_navigation_home_to_subredditFragment, bundle)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}