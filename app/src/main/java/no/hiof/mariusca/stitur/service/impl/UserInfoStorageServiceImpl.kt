package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.service.storage.UserInfoStorageService

class UserInfoStorageServiceImpl
constructor(private val firestore: FirebaseFirestore) : UserInfoStorageService{

    override suspend fun getProfile(profileID: String): Profile? =
    firestore.collection(USER_INFO_COLLECTION).document(profileID).get().await().toObject()


    override suspend fun save(profile: Profile): String =
        firestore.collection(UserInfoStorageServiceImpl.USER_INFO_COLLECTION).add(profile).await().id

    override suspend fun update(profile: Profile) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(userId: String) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val USER_INFO_COLLECTION = "UserInfo"
    }

}