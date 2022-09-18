package com.github.hattamaulana.felm.di

import android.content.Context
import com.github.hattamaulana.felm.data.MovieRepository
import com.github.hattamaulana.felm.data.remote.MovieDbFactory
import com.github.hattamaulana.felm.data.remote.MovieService
import com.github.hattamaulana.felm.data.repository.MovieRepositoryImpl
import com.github.hattamaulana.felm.data.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MovieDbFactory.API_URI)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieService(
        retrofit: Retrofit
    ): MovieService = retrofit.create(MovieService::class.java)

    @Binds
    @Provides
    fun bindMovieRepository(
        @ApplicationContext context: Context,
        service: MovieService,
    ) = MovieRepositoryImpl(context, service)
}