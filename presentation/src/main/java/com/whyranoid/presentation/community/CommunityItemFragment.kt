package com.whyranoid.presentation.community

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentCommunityItemBinding
import com.whyranoid.presentation.util.getSerializableData
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class CommunityItemFragment :
    BaseFragment<FragmentCommunityItemBinding>(R.layout.fragment_community_item) {

    private val viewModel: CommunityViewModel by activityViewModels()

    private val category by lazy {
        arguments?.getSerializableData(COMMUNITY_CATEGORY_KEY) ?: CommunityCategory.BOARD
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeState()
    }

    private fun initViews() {
        // TODO : 카테고리 별 다른 Shimmer Layout 생성
        when (category) {
            CommunityCategory.BOARD -> {
                setPostAdapter()
            }
            CommunityCategory.MY_GROUP -> {
                setMyGroupAdapter()
            }
            CommunityCategory.MY_POST -> {
                setMyPostAdapter()
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.GroupItemClick -> {
                // TODO : 뷰모델의 구조를 변경할 필요가 있어보임.
                // 공통된 뷰모델을 사용하여 eventFlow가 3개가 collect중이라서 앱이 터지는 이슈 임시 해결
                if (category == CommunityCategory.MY_GROUP) {
                    val action =
                        CommunityFragmentDirections.actionCommunityFragmentToGroupDetailFragment(
                            event.groupInfo
                        )
                    findNavController().navigate(action)
                }
            }
            is Event.JoinGroup -> {
                if (event.isSuccess) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_join_group_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_join_group_fail),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            is Event.DeletePost -> {
                if (event.isSuccess) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_delete_post_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_delete_post_fail),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setPostAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            val postAdapter = PostAdapter(
                buttonClickListener = {
                    viewModel.onGroupJoinButtonClicked(it)
                }
            )
            binding.rvCommunity.adapter = postAdapter

            viewLifecycleOwner.repeatWhenUiStarted {
                viewModel.myGroupList.collect { myGroupList ->
                    postAdapter.setMyGroupList(myGroupList)
                }
            }

            viewLifecycleOwner.repeatWhenUiStarted {
                viewModel.pagingPost.collectLatest { postList ->
                    removeShimmer()
                    postAdapter.submitData(postList)
                }
            }
        }
    }

    private fun setMyGroupAdapter() {
        val myGroupAdapter = MyGroupAdapter { groupInfo ->
            viewModel.onGroupItemClicked(groupInfo)
        }
        binding.rvCommunity.adapter = myGroupAdapter

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.myGroupList.collect { groupList ->
                removeShimmer()
                myGroupAdapter.submitList(groupList.sortedBy { it.name })
            }
        }
    }

    private fun setMyPostAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            val postAdapter = PostAdapter(
                isMyPost = true,
                itemLongClickListener = { postId ->
                    Snackbar.make(
                        binding.root,
                        getString(R.string.text_check_delete_post),
                        Snackbar.LENGTH_SHORT
                    ).setAction(R.string.text_delete) {
                        viewModel.deletePost(postId)
                    }.show()
                }
            )
            binding.rvCommunity.adapter = postAdapter

            viewLifecycleOwner.repeatWhenUiStarted {
                viewModel.myGroupList.collect { myGroupList ->
                    postAdapter.setMyGroupList(myGroupList)
                }
            }

            viewLifecycleOwner.repeatWhenUiStarted {
                viewModel.getMyPagingPostsUseCase().collectLatest { myPostList ->
                    removeShimmer()
                    postAdapter.submitData(myPostList)
                }
            }
        }
    }

    private fun removeShimmer() {
        binding.shimmerCommunity.apply {
            stopShimmer()
            isVisible = false
        }
    }

    companion object {
        private const val COMMUNITY_CATEGORY_KEY = "communityCategoryKey"

        fun newInstance(communityCategory: CommunityCategory): CommunityItemFragment {
            val fragment = CommunityItemFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(COMMUNITY_CATEGORY_KEY, communityCategory)
            }
            return fragment
        }
    }
}
