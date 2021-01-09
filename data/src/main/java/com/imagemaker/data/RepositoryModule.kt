package com.imagemaker.data

import com.imagemaker.data.repository.CharacterRepository
import com.imagemaker.data.repository.EpisodeRepository
import com.imagemaker.data.sources.CharacterLocalDataSource
import com.imagemaker.data.sources.CharacterRemoteDataSource
import com.imagemaker.data.sources.EpisodeRemoteDataSource
import dagger.Module
import dagger.Provides

/**
 * Created by Anibal Cortez on 09-01-21.
 */
@Module
class RepositoryModule {

    @Provides
    fun characterRepositoryProvider(
        remoteDataSource: CharacterRemoteDataSource,
        localDataSource: CharacterLocalDataSource
    ) = CharacterRepository(remoteDataSource, localDataSource)

    @Provides
    fun episodeRepositoryProvider(
        remoteDataSource: EpisodeRemoteDataSource
    ) = EpisodeRepository(remoteDataSource)
}