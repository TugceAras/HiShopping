package com.hms.referenceapp.hishopping.data.di

import com.hms.referenceapp.hishopping.data.repository.*
import com.hms.referenceapp.hishopping.data.repository.address.AddressRepository
import com.hms.referenceapp.hishopping.data.repository.address.AddressRepositoryImpl
import com.hms.referenceapp.hishopping.data.repository.basket.BasketRepository
import com.hms.referenceapp.hishopping.data.repository.basket.BasketRepositoryImpl
import com.hms.referenceapp.hishopping.data.repository.brand.BrandRepository
import com.hms.referenceapp.hishopping.data.repository.brand.BrandRepositoryImpl
import com.hms.referenceapp.hishopping.data.repository.campaign.CampaignRepository
import com.hms.referenceapp.hishopping.data.repository.campaign.CampaignRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAddressRepository(provide: AddressRepositoryImpl): AddressRepository = provide

    @Provides
    @Singleton
    fun provideOrderRepository(provide: OrderRepositoryImpl): OrderRepository = provide

    @Provides
    @Singleton
    fun provideCreditCardRepository(provide: CreditCardRepositoryImpl): CreditCardRepository = provide

    @Provides
    @Singleton
    fun provideProductRepository(provide: ProductRepositoryImpl): ProductRepository = provide

    @Provides
    @Singleton
    fun provideUserRepository(provide: UserRepositoryImpl): UserRepository = provide

    @Provides
    @Singleton
    fun provideStoreRepository(provide: StoreRepositoryImpl): StoreRepository = provide

    @Provides
    @Singleton
    fun provideCommentRepository(provide: CommentRepositoryImpl): CommentRepository = provide

    @Provides
    @Singleton
    fun provideCampaignRepository(provide: CampaignRepositoryImpl): CampaignRepository = provide

    @Provides
    @Singleton
    fun provideBrandRepository(provide: BrandRepositoryImpl): BrandRepository = provide

    @Provides
    @Singleton
    fun provideBasketRepository(provide: BasketRepositoryImpl): BasketRepository = provide

    @Provides
    @Singleton
    fun provideUserLikeDislikeRepository(provide: UserLikeDislikeRepositoryImpl): UserLikeDislikeRepository = provide

}