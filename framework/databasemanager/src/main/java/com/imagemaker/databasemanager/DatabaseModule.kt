package com.imagemaker.databasemanager

import android.app.Application
import com.imagemaker.data.sources.CharacterLocalDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Anibal Cortez on 09-01-21.
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun databaseProvider(app: Application): CharacterDatabase =
        CharacterDatabase.getDatabase(app)


    @Provides
    fun characterLocalDataSource(database: CharacterDatabase): CharacterLocalDataSource =
        CharacterLocalDataSourceImpl(database)



}