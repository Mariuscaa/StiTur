package no.hiof.mariusca.stitur.model

import com.google.firebase.firestore.DocumentId

data class Profile(
    @DocumentId val userID: String = "",
    val isAnonymous: Boolean = true,
    val username: String = "",
    val nickname: String = "",
    val profileImageUrl: String = "",

    // Connected properties
    val friends: List<String> = emptyList(),
    val sentInvites: List<Friendship> = emptyList(),
    val receivedInvites: List<Friendship> = emptyList(),
    var tripHistory: List<TripHistory> = emptyList(),
    val favorites: List<Trip> = emptyList(),
    var geoTreasures: List<GeoTreasure> = emptyList()
)

data class MinimalProfile(
    val userID: String = "",
    val userName: String = ""
)