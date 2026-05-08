package com.bbeniful.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.bbeniful.data.dao.ExerciseDao
import com.bbeniful.data.dao.ProgressDao
import com.bbeniful.data.database.ProgressTrackDatabase
import com.bbeniful.domain.DomainModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module(includes = [DomainModule::class])
@ComponentScan("com.bbeniful.data")
class DataModule {

    @Single
    fun provideDatabase(context: Context): ProgressTrackDatabase {
        return Room.databaseBuilder(
            context,
            ProgressTrackDatabase::class.java,
            "progress_tracker_db"
        ).addMigrations(ProgressTrackDatabase.MIGRATION_1_2).build()
    }

    @Single
    fun provideExerciseDao(db: ProgressTrackDatabase): ExerciseDao {
        return db.exerciseDao()
    }

    @Single
    fun provideProgressDao(db: ProgressTrackDatabase): ProgressDao {
        return db.progressDao()
    }

    @Single
    fun provideUserProfileDataStore(context: Context): DataStore<UserProfileProto> {
        return DataStoreFactory.create(
            serializer = UserProfileSerializer,
            produceFile = { context.dataStoreFile("user_profile.pb") }
        )
    }
}
