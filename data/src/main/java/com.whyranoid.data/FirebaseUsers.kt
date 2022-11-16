import com.whyranoid.data.db
import kotlinx.coroutines.tasks.await

// 유저 등록
fun writeUser() {
    val uid = "123123123"
    db.collection("Users")
        .document(uid).set(
            User(
                "신승민",
                "집주소",
                uid,
                listOf("그룹", "가입", "못하죠")
            )
        )
}

// uid로 유저 읽기
suspend fun readUser() {
    val uid = "123123123"
    val userData = db.collection("Users")
        .document(uid)
        .get()
        .await()

//    val hi = userData.toObject((FirebaseUser::class.java))
    println(
        "유저 ${userData.data?.values}"
    )
//        .toObject((FirebaseUser::class.java))
}

data class User(
    val name: String = "",
    val profileUrl: String = "",
    val uid: String = "",
    val joinedGroupList: List<String> = emptyList()
)

data class FirebaseUser(
    val name: String = "",
    val profileUrl: String = "",
    val uid: String = "",
    val joinedGroupList: String = ""
)

fun FirebaseUser.toUser(): User {
    return User(
        this.name,
        this.profileUrl,
        this.uid,
        this.joinedGroupList
            .replace("[", "")
            .replace("]", "")
            .split(",").map {
                it.trim()
            }
    )
}
