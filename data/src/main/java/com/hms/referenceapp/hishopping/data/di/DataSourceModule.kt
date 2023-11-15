package com.hms.referenceapp.hishopping.data.di

import com.hms.referenceapp.hishopping.data.datasource.*
import com.hms.referenceapp.hishopping.data.datasource.hawk.*
import com.hms.referenceapp.hishopping.data.datasource.ram.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideAddressDataSource(provide: AddressHawkSourceImpl): AddressDataSource = provide

    @Provides
    @Singleton
    fun provideOrderDataSource(provide: OrderRamDataSource): OrderDataSource = provide

    @Provides
    @Singleton
    fun provideCreditCardDataSource(provide: CreditCardHawkDataSource): CreditCardDataSource = provide

    @Provides
    @Singleton
    fun provideProductRamDataSource(provide: ProductRamDataSource): ProductDataSource = provide

    @Provides
    @Singleton
    fun provideStoreRamDataSource(provide: StoreRamDataSource): StoreDataSource = provide

    @Provides
    @Singleton
    fun provideCommentsHawkDataSource(provide: CommentsHawkDataSourceImpl): CommentDataSource = provide

    @Provides
    @Singleton
    fun provideCampaignRamDataSource(provide: CampaignRamDataSource): CampaignLocalDataSource = provide

    @Provides
    @Singleton
    fun provideBrandRamDataSource(provide: BrandRamDataSource): BrandLocalDataSource = provide

    @Provides
    @Singleton
    fun provideUsersLikeDislikeHawkDataSource(provide: UsersLikeDislikeHawkDataSourceImpl): UsersLikeDislikeDataSource = provide

    @Provides
    @Singleton
    fun provideMyCartHawkDataSource(provide: MyCartHawkDataSourceImpl): MyCartDataSource = provide

}