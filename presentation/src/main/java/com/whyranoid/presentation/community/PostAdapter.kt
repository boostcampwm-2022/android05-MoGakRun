package com.whyranoid.presentation.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.domain.model.RunningPost
import com.whyranoid.presentation.databinding.ItemRecruitPostBinding
import com.whyranoid.presentation.databinding.ItemRunningPostBinding
import com.whyranoid.presentation.model.GroupInfoUiModel

class PostAdapter(
    private val buttonClickListener: (String) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(diffUtil) {

    private lateinit var myGroupList: List<GroupInfoUiModel>

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post) =
                oldItem.postId == newItem.postId

            override fun areContentsTheSame(oldItem: Post, newItem: Post) =
                oldItem == newItem
        }

        const val RECRUIT_POST_TYPE = 0
        const val RUNNING_POST_TYPE = 1
    }

    abstract class PostViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(post: Post)
    }

    inner class RecruitPostViewHolder(
        parent: ViewGroup,
        private val binding: ItemRecruitPostBinding = ItemRecruitPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : PostViewHolder(binding) {
        override fun bind(post: Post) {
            if (post is RecruitPost) {
                with(binding) {
                    recruitPost = post
                    if (post.groupInfo.groupId in myGroupList.map { it.groupId }) {
                        btnJoinGroup.visibility = View.GONE
                    } else {
                        btnJoinGroup.visibility = View.VISIBLE
                        btnJoinGroup.setOnClickListener {
                            buttonClickListener(post.groupInfo.groupId)
                        }
                    }
                }
            }
        }
    }

    inner class RunningHistoryPostViewHolder(
        parent: ViewGroup,
        private val binding: ItemRunningPostBinding = ItemRunningPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : PostViewHolder(binding) {
        override fun bind(post: Post) {
            if (post is RunningPost) {
                binding.runningPost = post
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecruitPost -> RECRUIT_POST_TYPE
            else -> RUNNING_POST_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return when (viewType) {
            RECRUIT_POST_TYPE -> RecruitPostViewHolder(parent)
            else -> RunningHistoryPostViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setMyGroupList(myGroupList: List<GroupInfoUiModel>) {
        this.myGroupList = myGroupList
    }
}
