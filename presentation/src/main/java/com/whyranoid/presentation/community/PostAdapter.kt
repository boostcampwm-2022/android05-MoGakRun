package com.whyranoid.presentation.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.presentation.databinding.ItemRecruitPostBinding

class PostAdapter(private val myUid: String) :
    ListAdapter<Post, PostAdapter.RecruitPostViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post) =
                oldItem.postId == newItem.postId

            override fun areContentsTheSame(oldItem: Post, newItem: Post) =
                oldItem == newItem
        }
    }

    inner class RecruitPostViewHolder(
        parent: ViewGroup,
        private val binding: ItemRecruitPostBinding = ItemRecruitPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            if (post is RecruitPost) {
                binding.recruitPost = post
                if (myUid == post.author.uid) {
                    println("테스트 $myUid ${post.author.uid}")
                    binding.btnJoinGroup.visibility = View.GONE
                } else {
                    binding.btnJoinGroup.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruitPostViewHolder {
        return RecruitPostViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecruitPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
