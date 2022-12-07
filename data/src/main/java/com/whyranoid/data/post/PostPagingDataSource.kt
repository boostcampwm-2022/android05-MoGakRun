package com.whyranoid.data.post

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.QuerySnapshot
import com.whyranoid.domain.model.Post

class PostPagingDataSource(
    private val postDataSource: PostDataSource
) : PagingSource<QuerySnapshot, Post>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val postList = mutableListOf<Post>()

            // 현재 페이지
            val currentPage = postDataSource.getCurrentPagingPost(params.key)

            // Post 타입 캐스팅
            // TODO 예외 처리
            currentPage.forEach { document ->
                postDataSource.convertPostType(document)?.let { post ->
                    postList.add(post)
                }
            }

            // 마지막 스냅샷 저장
            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            // 마지막 스냅샷 이후 페이지 불러오기
            val nextPage = postDataSource.getNextPagingPost(lastDocumentSnapshot)

            LoadResult.Page(
                data = postList,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
