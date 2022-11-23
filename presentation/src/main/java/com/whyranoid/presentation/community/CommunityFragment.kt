package com.whyranoid.presentation.community

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.whyranoid.presentation.R
import com.whyranoid.presentation.base.BaseFragment
import com.whyranoid.presentation.databinding.FragmentCommunityBinding

internal class CommunityFragment :
    BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CommunityCategoryAdapter(this)
        setTabLayout(adapter)
    }

    private fun setTabLayout(adapter: FragmentStateAdapter) {
        binding.viewPager.adapter = adapter
        setOnMenuClickListener()
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            // TODO 게시글, 내가 쓴 글 추가 필요
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    CommunityCategory.MY_GROUP.ordinal -> {
                        binding.topAppBar.inflateMenu(R.menu.my_group_menu)
                    }
                }
            }

            // TODO 게시글, 내가 쓴 글 추가 필요
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    CommunityCategory.MY_GROUP.ordinal -> {
                        binding.topAppBar.menu.clear()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(CommunityCategory.values()[position].stringId)
        }.attach()
    }

    private fun setOnMenuClickListener() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.create_group -> {
                    val action =
                        CommunityFragmentDirections.actionCommunityFragmentToCreateGroupFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}
