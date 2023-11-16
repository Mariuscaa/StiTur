package no.hiof.mariusca.stitur.model

import com.google.firebase.firestore.DocumentId

// This model class represents a leaderboard entry. The model reflects stored data inside the corresponding firebase document
data class LeaderboardEntry(
    @DocumentId val uid: String = "", // For referencing Firebase Document with data
    val personalRanking: PersonalRanking = PersonalRanking(Tiers.SILVER, 0, 0, 0),
    val profileimageUrl: String = "",
    val username: String = "",
)