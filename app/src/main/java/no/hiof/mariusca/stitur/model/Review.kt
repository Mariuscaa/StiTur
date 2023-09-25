package no.hiof.mariusca.stitur.model

data class Review (
    val reviewID: String = "",
    val userID: String = "",
    val rating: Double = 0.0,
    val textContent: String = "",
)