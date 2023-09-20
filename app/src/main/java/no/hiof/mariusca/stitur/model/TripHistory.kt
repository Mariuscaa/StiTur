package no.hiof.mariusca.stitur.model

data class TripHistory(
    val tripHistoryID: String = "",
    val trip: Trip = Trip(),
    val trackedDistanceKm: Double = 0.0,
    val durationMinutes: Int = 0
)