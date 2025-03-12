/**
 * @file    SofaSoGood.kt
 * @author  Kenzie Lim
 * @par     Email: 2200709@sit.singaopretech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    17 February 2025
 *
 * @brief   The entity, dao database, and repository for this application.
 */

package com.google.ar.core.examples.kotlin.sofasogood.repo

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@Entity(tableName = "sofa_table")
data class SofaSoGoodEntity(
    @PrimaryKey val qrCode: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "modelID") val modelID: Int,
    @ColumnInfo(name = "isUnlocked") val isUnlocked: Boolean = false,
//    @PrimaryKey(autoGenerate = true) val id : Int = 0
) {
//    @PrimaryKey(autoGenerate = true) val id : Int = 0
//    constructor(
//        anchor: DummyAnchor
//    ) : this(anchor.model,
//        anchor.scale.x, anchor.scale.y, anchor.scale.z,
//        anchor.position.x, anchor.position.y, anchor.position.z,
//        anchor.rotation.x, anchor.rotation.y, anchor.rotation.z,
//        0)
}

@Dao
interface SofaSoGoodDao {
    @Query("SELECT * FROM sofa_table")
    fun getAllSofas(): Flow<List<SofaSoGoodEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(repo: SofaSoGoodEntity)

    @Update
    suspend fun update(digit: SofaSoGoodEntity)

    @Query("SELECT * FROM sofa_table WHERE qrCode = :qrCode")
    fun getSofaByQrCode(qrCode: String): Flow<SofaSoGoodEntity?>

    @Query("DELETE FROM sofa_table")
    suspend fun deleteAll()
}

@Database(entities = [SofaSoGoodEntity::class], version = 1, exportSchema = false)
abstract class SofaSoGoodDatabase : RoomDatabase() {
    abstract fun repoDao(): SofaSoGoodDao

    companion object {
        @Volatile // ensure no cache
        private var INSTANCE: SofaSoGoodDatabase? = null

        fun getDatabase(
            context: Context
        ): SofaSoGoodDatabase {
            // if instance is not null, return it
            // else create database
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SofaSoGoodDatabase::class.java,
                    "sofa_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}

class SofaSoGoodRepository @Inject constructor(
    private val repoDao: SofaSoGoodDao,
    private val sofaRepoService: SofaRepoService
) {

    val allSofas: Flow<List<SofaSoGoodEntity>> = repoDao.getAllSofas()

    //@Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(repo: SofaSoGoodEntity) {
        repoDao.insert(repo)
    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun update(repo: SofaSoGoodEntity) {
//        repoDao.update(repo)
//    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    fun getCardByQrCode(qrCode: String): Flow<SofaSoGoodEntity?> {
//        return repoDao.getSofaByQrCode(qrCode)
//    }

    //    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun insertUsingWrappedAnchors(anchors: List<DummyAnchor>) {
//        for(anchor in anchors) {
//            repoDao.update(SofaSoGoodEntity(anchor))
//        }
//    }
    fun getSofaByQrCode(qrCode: String): Flow<SofaSoGoodEntity?> {
        return repoDao.getSofaByQrCode(qrCode)
    }

    //@Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun unlockSofa(qrCode: String) {
        val sofa = repoDao.getSofaByQrCode(qrCode).firstOrNull()
        if (sofa != null && !sofa.isUnlocked) {
            val updatedSofa = sofa.copy(isUnlocked = true)
            repoDao.update(updatedSofa)
        }
    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun insertUsingWrappedAnchors(anchors: List<DummyAnchor>) {
//        for(anchor in anchors) {
//            repoDao.update(SofaSoGoodEntity(anchor))
//        }
//    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun updateWrappedAnchors(anchors: List<DummyAnchor>) {
//        // somehow supposed to update anchors?
//        // smth like
//        // SofaArRenderer().updateWrappedAnchors(anchors)
//    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun insertUsingWrappedAnchors(anchors: List<DummyAnchor>) {
//        for(anchor in anchors) {
//            repoDao.update(SofaSoGoodEntity(anchor))
//        }
//    }

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun updateWrappedAnchors(anchors: List<DummyAnchor>) {
//        // somehow supposed to update anchors?
//        // smth like
//        // SofaArRenderer().updateWrappedAnchors(anchors)
//    }

    //@Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        repoDao.deleteAll()
    }

}