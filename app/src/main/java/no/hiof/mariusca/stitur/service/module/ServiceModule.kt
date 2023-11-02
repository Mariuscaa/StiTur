package no.hiof.mariusca.stitur.service.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.hiof.mariusca.stitur.service.impl.AccountServiceImpl
import no.hiof.mariusca.stitur.service.impl.GeoTreasureStorageServiceImpl
import no.hiof.mariusca.stitur.service.impl.TripStorageServiceImpl
import no.hiof.mariusca.stitur.service.storage.GeoTreasureStorageService
import no.hiof.mariusca.stitur.service.storage.TripStorageService

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideTripStorageService(impl: TripStorageServiceImpl): TripStorageService

    @Binds
    abstract fun provideGeoTreasureStorageService(impl: GeoTreasureStorageServiceImpl): GeoTreasureStorageService

    /*@Binds
    abstract fun provideGeoTreasureStorageService(impl: UserInfoStorageServiceImpl): UserInfoStorageService
*/

}