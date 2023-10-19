package no.hiof.mariusca.stitur.leaderboards

import no.hiof.mariusca.stitur.model.Leaderboard
import no.hiof.mariusca.stitur.model.PersonalRanking
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.model.Tiers

class LeaderboardsDummyData {

    fun createDummyProfiles(): List<Profile> {

        val dummyUserProfiles = mutableListOf<Profile>()

        val profile1 = Profile("1a2b3c",
            false,
            "Sindre",
            "Wocky boy",
            "sindrelh@hiof.no",
            "",
            PersonalRanking(Tiers.SILVER, 250, 500, 750)
        )

        val profile2 = Profile("2a3b4c",
            false,
            "JsonStatham",
            "Json boy",
            "json@hiof.no",
            "",
            PersonalRanking(Tiers.GOLD, 500,1250, 3500)
        )

        val profile3 = Profile("4a3b4c",
            false,
            "Arne",
            "Weather boy",
            "weather@hiof.no",
            "",
            PersonalRanking(Tiers.PLATINUM, dailyPoints = 800, 750, 10_000)
        )

        val profile4 = Profile("1a3b4c",
            false,
            "NPC",
            "Generic NPC boy",
            "npc@hiof.no",
            "",
            PersonalRanking(Tiers.DIAMOND, 250, 1500, 5000)
        )

        dummyUserProfiles.addAll(listOf(profile1, profile2, profile3, profile4))

        return dummyUserProfiles
    }

    /**TIER LEVEL THRESHOLDS:

    WEEKLY TIERS:
    SILVER: 0-999 pts,
    GOLD: 1000 - 4999 pts,
    PLATINUM: 5000 - 9999 pts
    DIAMOND: >= 10.000 pts

    ALL TIME TIERS:
    SILVER: 0- 9999 pts
    GOLD: 10.000 - 19.999 pts
    PLATINUM: 20.000 - 49.999 pts
    DIAMOND: >= 50.000 pts

     */

    fun createDummyLeaderboardsWeekly(): Leaderboard{
        val profiles = createDummyProfiles()

        val silverTiersProfiles = profiles.filter{it.personalRanking.weeklyPoints <= 999}
        val goldTiersProfiles = profiles.filter{ it.personalRanking.weeklyPoints in 1000..4999 }
        val platinumTiersProfiles = profiles.filter{it.personalRanking.weeklyPoints in 5000..9999}
        val diamondTiersProfiles = profiles.filter{it.personalRanking.weeklyPoints >= 10_000}

        return Leaderboard(
            silverProfiles = silverTiersProfiles,
            goldProfiles = goldTiersProfiles,
            platinumProfiles = platinumTiersProfiles,
            diamondProfiles = diamondTiersProfiles
        )
    }

    fun createDummyLeaderboardsAllTime(): Leaderboard{

        val profiles = createDummyProfiles()

        val highScoreSilverTierProfiles = profiles.filter{it.personalRanking.totalPoints <= 9999}
        val highScoreGoldTierProfiles = profiles.filter{it.personalRanking.totalPoints in 10_000..19_999 }
        val highScorePlatinumTierProfiles = profiles.filter{it.personalRanking.totalPoints in 20_000..49_999 }
        val highScoreDiamondTierProfiles = profiles.filter{it.personalRanking.totalPoints >= 50_000}

        return Leaderboard(
            silverProfiles = highScoreSilverTierProfiles,
            goldProfiles = highScoreGoldTierProfiles,
            platinumProfiles = highScorePlatinumTierProfiles,
            diamondProfiles = highScoreDiamondTierProfiles
        )
    }


}