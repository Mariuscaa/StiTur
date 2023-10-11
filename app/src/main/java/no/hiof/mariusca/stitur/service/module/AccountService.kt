package no.hiof.mariusca.stitur.service.module

import kotlinx.coroutines.flow.Flow
import no.hiof.mariusca.stitur.model.Profile

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<Profile>

    suspend fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)
    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    suspend fun signOut()

}




