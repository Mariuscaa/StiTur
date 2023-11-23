package no.hiof.mariusca.stitur.service.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.hiof.mariusca.stitur.service.impl.AccountServiceImpl
import no.hiof.mariusca.stitur.service.impl.GeoTreasureStorageServiceImpl
import no.hiof.mariusca.stitur.service.impl.LeaderboardsServiceImpl
import no.hiof.mariusca.stitur.service.impl.TripStorageServiceImpl
import no.hiof.mariusca.stitur.service.impl.ProfileStorageServiceImpl
import no.hiof.mariusca.stitur.service.storage.GeoTreasureStorageService
import no.hiof.mariusca.stitur.service.storage.LeaderboardsService
import no.hiof.mariusca.stitur.service.storage.TripStorageService
import no.hiof.mariusca.stitur.service.storage.ProfileStorageService

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideTripStorageService(impl: TripStorageServiceImpl): TripStorageService

    @Binds
    abstract fun provideGeoTreasureStorageService(impl: GeoTreasureStorageServiceImpl): GeoTreasureStorageService

    @Binds
    abstract fun provideProfileStorageService(impl: ProfileStorageServiceImpl): ProfileStorageService

    @Binds
    abstract fun provideLeaderboardsService(impl: LeaderboardsServiceImpl): LeaderboardsService


}