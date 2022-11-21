package com.whyranoid.presentation.community

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityCategoryAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = CATEGORY_COUNT

    override fun createFragment(position: Int): Fragment {
        return CommunityItemFragment.newInstance(CommunityCategory.values()[position])
    }

    companion object {
        const val CATEGORY_COUNT = 3
    }
}
