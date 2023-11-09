package no.hiof.mariusca.stitur.model

import com.google.firebase.firestore.DocumentId

data class Profile(
    @DocumentId val userID: String = "",
    val isAnonymous: Boolean = true,
    val username: String = "",
    val nickname: String = "",
    //val email: String = "",
    val profileImageUrl: String = "",

    // Connected properties
    val personalRanking: PersonalRanking = PersonalRanking(Tiers.SILVER, 0, 0, 0),
    val friends: List<String> = emptyList(),
    val sentInvites: List<Friendship> = emptyList(),
    val receivedInvites: List<Friendship> = emptyList(),
    val tripHistory: List<TripHistory> = emptyList(),
    val favorites: List<Trip> = emptyList(),
    val geoTreasures: List<GeoTreasure> = emptyList()
)