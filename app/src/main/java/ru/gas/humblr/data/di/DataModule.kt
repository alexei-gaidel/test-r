package ru.gas.humblr.data.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.openid.appauth.AuthorizationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.gas.humblr.authorization.network.AuthorizationFailedInterceptor
import ru.gas.humblr.authorization.network.AuthorizationInterceptor
import ru.gas.humblr.data.remote.AuthRepository
import ru.gas.humblr.data.remote.RedditApi
import ru.gas.humblr.data.remote.RemoteRepository
import ru.gas.humblr.data.remote.models.TokenStorage
import ru.gas.humblr.domain.usecase.GetNewSubreddits
import ru.gas.humblr.domain.usecase.GetSubscribed
import ru.gas.humblr.domain.usecase.GetUnsubscribed
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepository()
    }

    @Provides
    @Singleton
    fun provideTokenStorage(): TokenStorage {
        return TokenStorage
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(redditApi: RedditApi): RemoteRepository {
        return RemoteRepository(redditApi)
    }

    @Provides
    @Singleton
    fun provideGetNewSubredditsUseCase(remoteRepository: RemoteRepository): GetNewSubreddits {
        return GetNewSubreddits(remoteRepository)
    }

    @Provides
    @Singleton
    fun provideGetSubscribedUseCase(remoteRepository: RemoteRepository): GetSubscribed {
        return GetSubscribed(remoteRepository)
    }

    @Provides
    @Singleton
    fun provideGetUnubscribedUseCase(remoteRepository: RemoteRepository): GetUnsubscribed {
        return GetUnsubscribed(remoteRepository)
    }

    @Provides
    @Singleton
    fun provideAuthorizationService(@ApplicationContext context: Context): AuthorizationService {
        return AuthorizationService(context)
    }

    @Provides
    fun provideRedditApi(
        authRepository: AuthRepository,
        tokenStorage: TokenStorage,
        authorizationService: AuthorizationService
    ): RedditApi {
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .addInterceptor(
                AuthorizationFailedInterceptor(
                    authorizationService, tokenStorage, authRepository
                )
            )
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            )
            .build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(RedditApi.BASE_URL)
            .client(okhttpClient)
            .build()
            .create(RedditApi::class.java)

    }

}