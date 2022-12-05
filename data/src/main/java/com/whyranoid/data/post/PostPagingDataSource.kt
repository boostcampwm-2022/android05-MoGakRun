package com.whyranoid.data.post

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.whyranoid.data.constant.CollectionId
import com.whyranoid.data.constant.CollectionId.POST_COLLECTION
import com.whyranoid.data.constant.FieldId.RUNNING_HISTORY_ID
import com.whyranoid.data.constant.FieldId.UPDATED_AT
import com.whyranoid.data.model.GroupInfoResponse
import com.whyranoid.data.model.RecruitPostResponse
import com.whyranoid.data.model.RunningPostResponse
import com.whyranoid.data.model.UserResponse
import com.whyranoid.data.model.toGroupInfo
import com.whyranoid.data.model.toUser
import com.whyranoid.domain.model.Post
import com.whyranoid.domain.model.RecruitPost
import com.whyranoid.domain.model.RunningHistory
import com.whyranoid.domain.model.RunningPost
import com.whyranoid.domain.model.toRule
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostPagingDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : PagingSource<QuerySnapshot, Post>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val postList = mutableListOf<Post>()
            // 현재 페이지
            val currentPage = params.key ?: db.collection(POST_COLLECTION)
                .orderBy(UPDATED_AT, Query.Direction.DESCENDING)
                .limit(DATA_COUNT_PER_PAGE)
                .get()
                .await()

            // Post 타입 캐스팅
            // TODO 예외 처리
            currentPage.forEach { document ->

                if (document[RUNNING_HISTORY_ID] != null) {
                    document.toObject(RunningPostResponse::class.java).let { postResponse ->
                        val authorResponse = db.collection(CollectionId.USERS_COLLECTION)
                            .document(postResponse.authorId)
                            .get()
                            .await()
                            .toObject(UserResponse::class.java)

                        authorResponse?.let {
                            val runningHistory =
                                db.collection(CollectionId.RUNNING_HISTORY_COLLECTION)
                                    .document(postResponse.runningHistoryId)
                                    .get()
                                    .await()
                                    .toObject(RunningHistory::class.java)

                            runningHistory?.let {
                                postList.add(
                                    RunningPost(
                                        postId = postResponse.postId,
                                        author = authorResponse.toUser(),
                                        updatedAt = postResponse.updatedAt,
                                        runningHistory = it,
                                        likeCount = 0,
                                        content = postResponse.content
                                    )
                                )
                            }
                        }
                    }
                } else {
                    document.toObject(RecruitPostResponse::class.java).let { postResponse ->
                        val authorResponse = db.collection(CollectionId.USERS_COLLECTION)
                            .document(postResponse.authorId)
                            .get()
                            .await()
                            .toObject(UserResponse::class.java)

                        authorResponse?.let {
                            val groupInfoResponse = db.collection(CollectionId.GROUPS_COLLECTION)
                                .document(postResponse.groupId)
                                .get()
                                .await()
                                .toObject(GroupInfoResponse::class.java)

                            groupInfoResponse?.let {
                                val author = authorResponse.toUser()
                                postList.add(
                                    RecruitPost(
                                        postId = postResponse.postId,
                                        author = author,
                                        updatedAt = postResponse.updatedAt,
                                        groupInfo = groupInfoResponse
                                            .toGroupInfo(
                                                author,
                                                rules = groupInfoResponse.rules.map {
                                                    it.toRule()
                                                }
                                            )
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 마지막 스냅샷 저장
            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            // 마지막 스냅샷 이후 페이지 불러오기
            val nextPage = db.collection(POST_COLLECTION)
                .orderBy(UPDATED_AT, Query.Direction.DESCENDING)
                .limit(DATA_COUNT_PER_PAGE).startAfter(lastDocumentSnapshot)
                .get()
                .await()

            LoadResult.Page(
                data = postList,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val DATA_COUNT_PER_PAGE = 10L
    }
}
