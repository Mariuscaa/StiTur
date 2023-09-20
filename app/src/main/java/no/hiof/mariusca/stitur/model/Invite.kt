package no.hiof.mariusca.stitur.model

data class Invite(
    val senderID: String = "",
    val recipientID: String = "",
    val active: Boolean = true
)