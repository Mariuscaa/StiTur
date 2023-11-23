package no.hiof.mariusca.stitur.service.storage

import no.hiof.mariusca.stitur.model.Profile

interface ProfileStorageService {

    suspend fun getProfile(profileID: String): Profile?
    suspend fun save(profile: Profile): Unit
    suspend fun update(profile: Profile)
    suspend fun delete(userId: String)
}