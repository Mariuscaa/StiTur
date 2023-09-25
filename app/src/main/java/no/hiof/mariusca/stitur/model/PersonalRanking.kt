package no.hiof.mariusca.stitur.model

enum class Tiers {
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND
}
data class PersonalRanking (
    val tier: Tiers = Tiers.SILVER,
    val dailyPoints: Int = 0,
    val weeklyPoints: Int = 0,
    val totalPoints: Int = 0,
)