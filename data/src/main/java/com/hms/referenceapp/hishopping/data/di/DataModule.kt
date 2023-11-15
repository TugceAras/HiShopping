package com.hms.referenceapp.hishopping.data.di

import android.content.Context
import com.hms.referenceapp.hishopping.data.clouddb.CloudDbDataSource
import com.hms.referenceapp.hishopping.data.clouddb.CloudDbDataSourceImpl
import com.hms.referenceapp.hishopping.data.datasource.ram.RamDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideRamDb(@ApplicationContext context: Context) = RamDb(context)

    @Provides
    @Singleton
    fun provideCloudDbHelper(provide: CloudDbDataSourceImpl): CloudDbDataSource = provide

}