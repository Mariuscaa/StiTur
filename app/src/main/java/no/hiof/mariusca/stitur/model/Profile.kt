package no.hiof.mariusca.stitur.model

data class Profile(
    val userID: String = "",
    val username: String = "",
    val nickname: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val tripHistory: List<TripHistory> = emptyList(),
    val favorites: List<Trip> = emptyList()
)