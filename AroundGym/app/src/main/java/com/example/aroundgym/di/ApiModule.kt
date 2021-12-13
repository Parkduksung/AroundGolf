package com.example.aroundgym.di

import com.example.aroundgym.api.KakaoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideKakaoApi(): KakaoApi {
        return KakaoApi.create()
    }
}