package com.example.aroundgolf.di

import androidx.room.Room
import com.example.aroundgolf.api.GolfApi
import com.example.aroundgolf.data.repo.GolfRepository
import com.example.aroundgolf.data.repo.GolfRepositoryImpl
import com.example.aroundgolf.data.source.local.GolfLocalDataSource
import com.example.aroundgolf.data.source.local.GolfLocalDataSourceImpl
import com.example.aroundgolf.data.source.remote.GolfRemoteDataSource
import com.example.aroundgolf.data.source.remote.GolfRemoteDataSourceImpl
import com.example.aroundgolf.room.GolfDatabase
import com.example.aroundgolf.viewmodel.BookmarkViewModel
import com.example.aroundgolf.viewmodel.HomeViewModel
import com.example.aroundgolf.viewmodel.MapViewModel
import com.example.aroundgolf.viewmodel.SplashViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppKoinSetup : KoinBaseKoinSetup() {

    companion object {
        private const val GOLF_BASE_URL =
            "https://openapi.gg.go.kr/"
    }

    private val viewModelModule = module {
        viewModel { HomeViewModel(androidApplication()) }
        viewModel { MapViewModel(androidApplication()) }
        viewModel { BookmarkViewModel(androidApplication()) }
        viewModel { SplashViewModel(androidApplication()) }
    }

    private val repositoryModule = module {
        single<GolfRepository> { GolfRepositoryImpl() }
    }

    private val sourceModule = module {
        single<GolfRemoteDataSource> { GolfRemoteDataSourceImpl() }
        single<GolfLocalDataSource> { GolfLocalDataSourceImpl() }
    }
    private val apiModule = module {
        single<GolfApi> {
            Retrofit.Builder()
                .baseUrl(GOLF_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GolfApi::class.java)
        }
    }

    private val databaseModule = module {
        single {
            Room.databaseBuilder(
                get(),
                GolfDatabase::class.java,
                "golf_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    override fun getModules(): List<Module> {
        return listOf(
            viewModelModule,
            repositoryModule,
            sourceModule,
            apiModule,
            databaseModule
        )
    }
}