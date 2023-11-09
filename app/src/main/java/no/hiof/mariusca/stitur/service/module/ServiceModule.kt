package no.hiof.mariusca.stitur.service.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.hiof.mariusca.stitur.service.impl.AccountServiceImpl
import no.hiof.mariusca.stitur.service.impl.GeoTreasureStorageServiceImpl
import no.hiof.mariusca.stitur.service.impl.TripHistoryStorageServiceImpl
import no.hiof.mariusca.stitur.service.impl.TripStorageServiceImpl
import no.hiof.mariusca.stitur.service.impl.UserInfoStorageServiceImpl
import no.hiof.mariusca.stitur.service.storage.GeoTreasureStorageService
import no.hiof.mariusca.stitur.service.storage.TripHistoryStorageService
import no.hiof.mariusca.stitur.service.storage.TripStorageService
import no.hiof.mariusca.stitur.service.storage.UserInfoStorageService

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
ProfileViewAndLogging
    abstract fun provideUserStorageService(impl: UserInfoStorageServiceImpl): UserInfoStorageService


    abstract fun provideTripHistoryStorageService(impl: TripHistoryStorageServiceImpl): TripHistoryStorageService

    /*@Binds
    abstract fun provideGeoTreasureStorageService(impl: UserInfoStorageServiceImpl): UserInfoStorageService
*/


}