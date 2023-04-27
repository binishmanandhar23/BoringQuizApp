package com.treeleaf.quiz.network.module

import android.content.Context
import com.treeleaf.quiz.BuildConfig
import com.treeleaf.quiz.database.module.DatabaseModule
import com.treeleaf.quiz.network.BoringQuizServiceImpl
import com.treeleaf.quiz.network.service.BoringQuizService
import com.treeleaf.quiz.network.utils.UrlEndPoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi

@Module
@InstallIn(SingletonComponent::class)
object BoringQuizServiceModule {
    @OptIn(ExperimentalSerializationApi::class)
    private fun provideClient(): HttpClient = HttpClient(Android) {
        defaultRequest {
            header("X-Api-Key", BuildConfig.TOKEN)
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        install(HttpSend) {
            intercept { clientCall, _ ->
                clientCall
            }
        }
    }


    @Provides
    fun provideMainModule(@ApplicationContext context: Context): BoringQuizService =
        BoringQuizServiceImpl(
            httpClient = provideClient(),
            database = DatabaseModule.provide(context = context)
        )
}