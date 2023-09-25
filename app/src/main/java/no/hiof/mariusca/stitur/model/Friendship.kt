package no.hiof.mariusca.stitur.model

enum class FriendshipStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
}

data class Friendship(
    val latestStatusChangingUserID: String = "",
    val receiverUserID: String = "",
    val friendshipStatus: FriendshipStatus = FriendshipStatus.PENDING
)