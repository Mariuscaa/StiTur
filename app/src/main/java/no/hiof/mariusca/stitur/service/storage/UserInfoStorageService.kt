package no.hiof.mariusca.stitur.service.storage

import no.hiof.mariusca.stitur.model.Profile

interface UserInfoStorageService {

    suspend fun getProfile(profileID: String): Profile?
    suspend fun save(profile: Profile): String
    suspend fun update(profile: Profile)
    suspend fun delete(userId: String)
}