package no.hiof.mariusca.stitur.model

data class Profile(
    val userID: String = "",
    val username: String = "",
    val nickname: String = "",
    val email: String = "",
    val profileImageUrl: String = "",

    // Connected properties
    val personalRanking: PersonalRanking,
    val friends: List<String> = emptyList(),
    val sentInvites: List<Friendship> = emptyList(),
    val receivedInvites: List<Friendship> = emptyList(),
    val tripHistory: List<TripHistory> = emptyList(),
    val favorites: List<Trip> = emptyList(),
    val geoTreasures: List<GeoTreasure> = emptyList()
)