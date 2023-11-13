package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.LeaderboardEntry
import no.hiof.mariusca.stitur.service.storage.LeaderboardsService
import javax.inject.Inject

class LeaderboardsServiceImpl
@Inject // Annotation for later DI-purposes
constructor(private val firestore: FirebaseFirestore) : LeaderboardsService {

    /**FLOW
     * Defining the Flow of lists of Leaderboard Entries.
    Using dataObjects() to retrieve data from collection - then converting data to a flow of objects
     **/
    override val leaderboardEntries: Flow<List<LeaderboardEntry>>
        get() = firestore.collection(LEADERBOARDS_DATA).dataObjects()

    /*
    GET
    Retrieving single Leaderboard Entries by leaderboardEntryId from the collection
    and converts the data into an object*/
    override suspend fun get(leaderboardEntryId: String): LeaderboardEntry? =
        firestore.collection(LEADERBOARDS_DATA).document(leaderboardEntryId).get().await().toObject()

    /**SAVE
     * Saving the new leaderboard entry object to the firestore collection -
     * then returning the ID of the newly created doc**/
    override suspend fun save(leaderboardEntry: LeaderboardEntry): String =
        firestore.collection(LEADERBOARDS_DATA).add(leaderboardEntry).await().id

    /** UPDATE
     * Updating an existing Leaderboard Entry object.
     * This function will first get the Firestore document corresponding to the
     * Leaderboard object's uid and then update the document's data with the values.
     * **/
    override suspend fun update(leaderboardEntry: LeaderboardEntry){
        val leaderboardDocument= firestore.collection(LEADERBOARDS_DATA).document(leaderboardEntry.uid)
        leaderboardDocument.set(leaderboardEntry).await()
    }

    /**DELETE
     * Deleting an existing Leaderboard Entry object.
     * This function will select and delete a leaderboard object based on its uid.
     * **/
    override suspend fun delete(leaderboardEntryId: String){
        firestore.collection(LEADERBOARDS_DATA).document(leaderboardEntryId).delete().await()
    }

    /** COMPANION OBJECT
     * Companion object holding a constant representing the firestore document collection "LeaderboardsData"*/
    companion object{
        private const val LEADERBOARDS_DATA = "LeaderboardsData"
    }
}