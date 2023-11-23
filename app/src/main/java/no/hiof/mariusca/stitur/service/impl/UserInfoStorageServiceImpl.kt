package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.LeaderboardEntry
import no.hiof.mariusca.stitur.model.PersonalRanking
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.model.Tiers
import no.hiof.mariusca.stitur.service.storage.UserInfoStorageService
import javax.inject.Inject

class UserInfoStorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : UserInfoStorageService {

    override suspend fun getProfile(profileID: String): Profile? =
        firestore.collection(USER_INFO_COLLECTION).document(profileID).get().await().toObject()

    // Saves profile and leaderboard entry at the same time.
    override suspend fun save(profile: Profile) {
        firestore.collection(USER_INFO_COLLECTION).document(profile.userID).set(profile).await()
        val leaderboardEntry = LeaderboardEntry(
            uid = profile.userID,
            username = profile.username,
            profileimageUrl = profile.profileImageUrl,
            personalRanking = PersonalRanking(Tiers.SILVER, 0, 0, 0)
        )

        firestore.collection("LeaderboardEntry").document(profile.userID).set(leaderboardEntry)
            .await()
    }

    override suspend fun update(profile: Profile) {
        firestore.collection(USER_INFO_COLLECTION).document(profile.userID).set(profile).await()
    }

    override suspend fun delete(userId: String) {
        firestore.collection(USER_INFO_COLLECTION).document(userId).delete().await()
    }

    companion object {
        private const val USER_INFO_COLLECTION = "UserInfo"
    }

}