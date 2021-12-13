package com.example.aroundgolf.di

import com.example.aroundgolf.viewmodel.BookmarkViewModel
import com.example.aroundgolf.viewmodel.HomeViewModel
import com.example.aroundgolf.viewmodel.MapViewModel
import com.example.aroundgolf.viewmodel.SplashViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class AppKoinSetup : KoinBaseKoinSetup() {

    companion object {
        private const val GO_CAMPING_BASE_URL =
            "http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/"
    }

    private val viewModelModule = module {
        viewModel { HomeViewModel(androidApplication()) }
        viewModel { MapViewModel(androidApplication()) }
        viewModel { BookmarkViewModel(androidApplication()) }
        viewModel { SplashViewModel(androidApplication()) }
    }

//    private val repositoryModule = module {
//        single<GoCampingRepository> { GoCampingRepositoryImpl() }
//        single<FirebaseRepository> { FirebaseRepositoryImpl() }
//    }
//
//    private val sourceModule = module {
//        single<GoCampingRemoteDataSource> { GoCampingRemoteDataSourceImpl() }
//        single<GoCampingLocalDataSource> { GoCampingLocalDataSourceImpl() }
//        single<FirebaseRemoteDataSource> {
//            FirebaseRemoteDataSourceImpl(
//                FirebaseAuth.getInstance(),
//                FirebaseFirestore.getInstance(),
//                FirebaseStorage.getInstance()
//            )
//        }
//    }
//
//    private val apiModule = module {
//        single<GoCampingApi> {
//            Retrofit.Builder()
//                .baseUrl(GO_CAMPING_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(GoCampingApi::class.java)
//        }
//    }
//
//    private val databaseModule = module {
//        single {
//            Room.databaseBuilder(
//                get(),
//                CampingDatabase::class.java,
//                "camping_database"
//            )
//                .fallbackToDestructiveMigration()
//                .build()
//        }
//    }

    override fun getModules(): List<Module> {
        return listOf(
            viewModelModule
//            repositoryModule,
//            sourceModule,
//            apiModule,
//            databaseModule
        )
    }
}