package com.whyranoid.presentation.community

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentCommunityItemBinding
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.util.getSerializableData
import com.whyranoid.presentation.util.makeSnackBar
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class CommunityItemFragment :
    BaseFragment<FragmentCommunityItemBinding>(R.layout.fragment_community_item) {

    private val viewModel: CommunityViewModel by activityViewModels()
    private lateinit var myGroupAdapter: MyGroupAdapter
    private lateinit var postAdapter: PostAdapter
    private val category by lazy {
        arguments?.getSerializableData(COMMUNITY_CATEGORY_KEY) ?: CommunityCategory.BOARD
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeState()
    }

    private fun initViews() {
        when (category) {
            CommunityCategory.BOARD -> {
                inflateShimmerLoadingLayout(isGroup = false)
                setPostAdapter()
            }
            CommunityCategory.MY_GROUP -> {
                inflateShimmerLoadingLayout()
                setMyGroupAdapter()
            }
            CommunityCategory.MY_POST -> {
                inflateShimmerLoadingLayout(isGroup = false)
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
                if (category == CommunityCategory.BOARD) {
                    if (event.isSuccess) {
                        binding.root.makeSnackBar(getString(R.string.text_join_group_success))
                            .show()
                    } else {
                        binding.root.makeSnackBar(getString(R.string.text_join_group_fail)).show()
                    }
                }
            }
            is Event.DeletePost -> {
                if (category == CommunityCategory.MY_POST) {
                    if (event.isSuccess) {
                        binding.root.makeSnackBar(getString(R.string.text_delete_post_success))
                            .show()
                        event.block.invoke()
                    } else {
                        binding.root.makeSnackBar(getString(R.string.text_delete_post_fail)).show()
                    }
                }
            }
        }
    }

    private fun setPostAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            postAdapter = PostAdapter(
                buttonClickListener = {
                    viewModel.onGroupJoinButtonClicked(it)
                }
            )

            viewLifecycleOwner.repeatWhenUiStarted {
                postAdapter.loadStateFlow.collectLatest { loadStates ->
                    binding.shimmerCommunity.isVisible = loadStates.refresh is LoadState.Loading
                    binding.rvCommunity.isVisible = (loadStates.refresh is LoadState.Loading).not()
                }
            }

            binding.rvCommunity.adapter = postAdapter

            binding.swipeRefreshLayout.apply {
                setOnRefreshListener {
                    postAdapter.refresh()
                    binding.rvCommunity.scrollToPosition(POSITION_TOP)
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                setColorSchemeColors(context.getColor(R.color.mogakrun_on_primary))
            }

            viewLifecycleOwner.repeatWhenUiStarted {
                observeGroupInfoListState(isMyGroupTab = false)
            }

            viewLifecycleOwner.repeatWhenUiStarted {
                viewModel.pagingPost.collectLatest { postList ->
                    postAdapter.submitData(postList)
                }
            }
        }
    }

    private fun setMyGroupAdapter() {
        binding.swipeRefreshLayout.isEnabled = false
        myGroupAdapter = MyGroupAdapter { groupInfo ->
            viewModel.onGroupItemClicked(groupInfo)
        }
        binding.rvCommunity.adapter = myGroupAdapter

        viewLifecycleOwner.repeatWhenUiStarted {
            observeGroupInfoListState()
        }
    }

    private fun setMyPostAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            postAdapter = PostAdapter(
                isMyPost = true,
                itemLongClickListener = { postId ->
                    binding.root.makeSnackBar(getString(R.string.text_check_delete_post))
                        .setAction(R.string.text_delete) {
                            viewModel.deletePost(postId) {
                                postAdapter.refresh()
                            }
                        }.show()
                }
            )

            viewLifecycleOwner.repeatWhenUiStarted {
                postAdapter.loadStateFlow.collectLatest { loadStates ->
                    binding.shimmerCommunity.isVisible = loadStates.refresh is LoadState.Loading
                    binding.rvCommunity.isVisible = (loadStates.refresh is LoadState.Loading).not()
                }
            }

            binding.rvCommunity.adapter = postAdapter

            binding.swipeRefreshLayout.apply {
                setOnRefreshListener {
                    postAdapter.refresh()
                    binding.rvCommunity.scrollToPosition(POSITION_TOP)
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                setColorSchemeColors(context.getColor(R.color.mogakrun_on_primary))
            }

            viewLifecycleOwner.repeatWhenUiStarted {
                observeGroupInfoListState(isMyGroupTab = false)
            }

            viewLifecycleOwner.repeatWhenUiStarted {
                viewModel.getMyPagingPostsUseCase(viewLifecycleOwner.lifecycleScope)
                    .collectLatest { myPostList ->
                        postAdapter.submitData(myPostList)
                    }
            }
        }
    }

    private fun inflateShimmerLoadingLayout(isGroup: Boolean = true) {
        repeat(10) {
            binding.shimmerLinearLayout.addView(
                layoutInflater.inflate(
                    if (isGroup) R.layout.my_group_item_loading else R.layout.post_item_loading,
                    binding.shimmerLinearLayout,
                    false
                )
            )
        }
    }

    // 게시글, 내가 쓴 글 탭은 PagingState에 따라 Shimmer를 관리해줌.
    private suspend fun observeGroupInfoListState(isMyGroupTab: Boolean = true) {
        viewModel.myGroupListState.collectLatest { groupInfoListState ->

            when (groupInfoListState) {
                is UiState.UnInitialized -> {}
                is UiState.Loading -> {
                    if (isMyGroupTab) binding.shimmerCommunity.isVisible = true
                }
                is UiState.Success -> {
                    if (isMyGroupTab) {
                        myGroupAdapter.submitList(groupInfoListState.value.sortedBy { it.name })
                        binding.shimmerCommunity.isVisible = false
                    } else {
                        postAdapter.setMyGroupList(groupInfoListState.value)
                    }
                }
                is UiState.Failure -> {
                    if (isMyGroupTab) binding.shimmerCommunity.isVisible = false
                }
            }
        }
    }

    companion object {
        private const val COMMUNITY_CATEGORY_KEY = "communityCategoryKey"
        private const val POSITION_TOP = 0

        fun newInstance(communityCategory: CommunityCategory): CommunityItemFragment {
            val fragment = CommunityItemFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(COMMUNITY_CATEGORY_KEY, communityCategory)
            }
            return fragment
        }
    }
}
