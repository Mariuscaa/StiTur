package no.hiof.mariusca.stitur.model

import com.google.firebase.firestore.DocumentId

data class Profile(
    @DocumentId val userID: String = "",
    val isAnonymous: Boolean = true,
    val username: String = "",
    val profileImageUrl: String = "", // Not in use.

    // Connected properties
    val friends: List<String> = emptyList(), // Not in use
    val sentInvites: List<Friendship> = emptyList(), // Not in use
    val receivedInvites: List<Friendship> = emptyList(), // Not in use
    var tripHistory: List<TripHistory> = emptyList(),
    val favorites: List<Trip> = emptyList(), // Not in use
    var geoTreasures: List<GeoTreasure> = emptyList()
)

data class MinimalProfile(
    val userID: String = "",
    val userName: String = ""
)