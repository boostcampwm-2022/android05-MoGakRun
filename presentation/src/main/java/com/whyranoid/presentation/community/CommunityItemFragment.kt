package com.whyranoid.presentation.community

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentCommunityItemBinding
import com.whyranoid.presentation.util.repeatWhenUiStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class CommunityItemFragment :
    BaseFragment<FragmentCommunityItemBinding>(R.layout.fragment_community_item) {

    private val viewModel: CommunityViewModel by viewModels()

    init {
        observe()
    }

    private val category by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(COMMUNITY_CATEGORY_KEY, CommunityCategory::class.java)
                ?: CommunityCategory.BOARD
        } else {
            arguments?.getSerializable(COMMUNITY_CATEGORY_KEY) as? CommunityCategory
                ?: CommunityCategory.BOARD
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO : 카테고리 별 다른 Shimmer Layout 생성
        when (category) {
            CommunityCategory.BOARD -> {
                // TODO: Adapter 설정
            }
            CommunityCategory.MY_GROUP -> {
                setMyGroupAdapter()
                setupMenu()
            }
            CommunityCategory.MY_POST -> {
                // TODO: Adapter 설정
            }
        }
    }

    private fun observe() {
        repeatWhenUiStarted {
            viewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.CategoryItemClick -> {
                Snackbar.make(binding.root, "${event.groupInfo.name} 클릭됨", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // TODO 카테고리 별 다른 아이템 처리
    private fun setMyGroupAdapter() {
        val adapter = MyGroupAdapter { groupInfo ->
            viewModel.onCategoryItemClicked(groupInfo)
        }
        binding.rvCommunity.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMyGroupListUseCase().collect { groupList ->
                removeShimmer()
                adapter.submitList(groupList.sortedBy { it.name })
            }
        }
    }

    private fun removeShimmer() {
        binding.shimmerCommunity.apply {
            if (isShimmerStarted) {
                stopShimmer()
                isVisible = false
            }
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.my_group_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.create_group -> {
                        Snackbar.make(binding.root, "생성하기", Snackbar.LENGTH_SHORT).show()
                    }
                }
                return true
            }
        })
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
