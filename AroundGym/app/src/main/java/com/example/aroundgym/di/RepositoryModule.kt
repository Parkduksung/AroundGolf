package com.example.aroundgym.di


import com.example.aroundgym.data.repo.KAKAORepository
import com.example.aroundgym.data.repo.KAKAORepositoryImpl
import com.example.aroundgym.data.source.KAKAORemoteDataSource
import com.example.aroundgym.data.source.KAKAORemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindKAKAORepository(kakaoRepositoryImpl: KAKAORepositoryImpl): KAKAORepository

    @Singleton
    @Binds
    abstract fun bindKAKAORemoteDataSource(kakaoRemoteDataSourceImpl: KAKAORemoteDataSourceImpl): KAKAORemoteDataSource

}

