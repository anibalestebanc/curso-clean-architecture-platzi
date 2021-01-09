package com.imagemaker.requestmanager

import com.imagemaker.data.sources.CharacterRemoteDataSource
import com.imagemaker.data.sources.EpisodeRemoteDataSource
import com.imagemaker.requestmanager.APIConstants.BASE_API_URL
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Anibal Cortez on 09-01-21.
 */
@Module
class ApiModule {

    @Provides
    fun characterRequestProvider(
        @Named("baseUrl")
        baseUrl: String
    ) = CharacterRequest(baseUrl)

    @Provides
    fun episodeRequestProvider(
        @Named("baseUrl")
        baseUrl: String
    ) = EpisodeRequest(baseUrl)

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(): String = BASE_API_URL


    @Provides
    fun characterRemoteDataSourceProvider(characterRequest: CharacterRequest):
            CharacterRemoteDataSource = CharacterRemoteDataSourceImpl(characterRequest)


    @Provides
    fun episodeRemoteDataSourceProvider(episodeRequest: EpisodeRequest):
            EpisodeRemoteDataSource = EpisodeRemoteDataSourceImpl(episodeRequest)

}