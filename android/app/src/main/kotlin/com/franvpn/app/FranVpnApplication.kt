package com.franvpn.app

import android.app.Application
import androidx.room.Room
import com.franvpn.app.data.api.SubscriptionApi
import com.franvpn.app.data.db.FranVpnDatabase
import com.franvpn.app.data.repository.ConfigRepository
import com.franvpn.app.data.repository.SettingsRepository
import com.franvpn.app.data.repository.SubscriptionRepository
import com.franvpn.app.ui.viewmodel.MainViewModel
import com.franvpn.app.ui.viewmodel.SettingsViewModel
import com.franvpn.app.ui.viewmodel.SubscriptionViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Application class for FRANVPN
 * Initializes Koin DI, Timber logging, and database
 */
class FranVpnApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
        
        // Initialize Koin DI
        startKoin {
            androidLogger()
            androidContext(this@FranVpnApplication)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
        
        Timber.d("FranVpnApplication initialized")
    }

    companion object {
        /**
         * Database module for Koin
         */
        private val databaseModule = module {
            single {
                Room.databaseBuilder(
                    get(),
                    FranVpnDatabase::class.java,
                    "franvpn_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            
            single { get<FranVpnDatabase>().configDao() }
            single { get<FranVpnDatabase>().subscriptionDao() }
            single { get<FranVpnDatabase>().settingsDao() }
        }

        /**
         * Network module for Koin (Retrofit, OkHttp)
         */
        private val networkModule = module {
            single {
                Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }
            }
            
            single {
                val loggingInterceptor = HttpLoggingInterceptor { message ->
                    Timber.tag("OkHttp").d(message)
                }.apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.BASIC
                    }
                }
                
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
            }
            
            single {
                val json = get<Json>()
                val client = get<OkHttpClient>()
                
                Retrofit.Builder()
                    .baseUrl("https://api.example.com/")
                    .client(client)
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .build()
            }
            
            single {
                get<Retrofit>().create(SubscriptionApi::class.java)
            }
        }

        /**
         * Repository module for Koin
         */
        private val repositoryModule = module {
            single {
                ConfigRepository(
                    configDao = get(),
                    subscriptionApi = get()
                )
            }
            
            single {
                SubscriptionRepository(
                    subscriptionDao = get(),
                    configDao = get(),
                    subscriptionApi = get()
                )
            }
            
            single {
                SettingsRepository(
                    settingsDao = get()
                )
            }
        }

        /**
         * ViewModel module for Koin
         */
        private val viewModelModule = module {
            viewModel {
                MainViewModel(
                    configRepository = get()
                )
            }
            
            viewModel {
                SubscriptionViewModel(
                    repository = get()
                )
            }
            
            viewModel {
                SettingsViewModel(
                    repository = get()
                )
            }
        }
    }
}

/**
 * Release tree for Timber (no logging in production)
 */
private class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Don't log anything in production
    }
}
