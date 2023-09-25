package no.hiof.mariusca.stitur.model

data class Trip(
    val tripID: String = "",
    val externalID: String = "",
    val name: String = "",
    val reviews: List<Review> = emptyList(),
)