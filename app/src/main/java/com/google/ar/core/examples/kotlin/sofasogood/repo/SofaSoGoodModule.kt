/**
 * @file    SofaSoGoodModule.kt
 * @author  Kenzie Lim
 * @par     Email: 2200709@sit.singaopretech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    17 February 2025
 *
 * @brief   The module for this application.
 */

package com.google.ar.core.examples.kotlin.sofasogood.repo


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SofaSoGoodDatabase {
        return Room.databaseBuilder(
            context,
            SofaSoGoodDatabase::class.java,
            "sofa_database.db"
        ).build()
    }

    @Provides
    fun provideWordDao(database: SofaSoGoodDatabase): SofaSoGoodDao = database.repoDao()
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<androidx.datastore.preferences.core.Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("RepoPreference") }
        )
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideBaseUrl(): String = "https://api.github.com"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSofaRepoService(retrofit: Retrofit): SofaRepoService =
        retrofit.create(SofaRepoService::class.java)
}