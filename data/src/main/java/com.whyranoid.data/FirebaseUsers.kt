import com.google.firebase.firestore.FieldValue
import com.whyranoid.data.db
import com.whyranoid.data.fakeUser
import kotlinx.coroutines.tasks.await

val uid = "123123123"

data class FirebaseUser(
    val name: String = "",
    val profileUrl: String = "",
    val uid: String = "",
    val joinedGroupList: List<String> = emptyList()
)

// 유저 등록
fun writeUser() {
    db.collection("Users")
        .document(uid).set(
            FirebaseUser(
                "신승민",
                "집주소",
                uid,
                arrayListOf("그룹", "가입", "못하죠")
            )
        )
}

// uid로 유저 읽기
suspend fun readUser(): FirebaseUser {
    val userData = db.collection("Users")
        .document(uid)
        .get()
        .await()

    return userData.toObject((FirebaseUser::class.java)) ?: fakeUser
}

// 유저가 그룹에 가입함
fun userJoinGroup() {
    db.collection("Users")
        .document(uid)
        .update("joinedGroupList", FieldValue.arrayUnion("현수 그룹"))
}

// 유저가 그룹에서 나감
fun userExitGroup() {
    db.collection("Users")
        .document(uid)
        .update("joinedGroupList", FieldValue.arrayRemove("가입"))
}
