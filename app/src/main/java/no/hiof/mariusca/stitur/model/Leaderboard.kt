package no.hiof.mariusca.stitur.model

data class Leaderboard (
    val silverProfiles: List<Profile> = emptyList(),
    val goldProfiles: List<Profile> = emptyList(),
    val platinumProfiles: List<Profile> = emptyList(),
    val diamondProfiles: List<Profile> = emptyList(),
)