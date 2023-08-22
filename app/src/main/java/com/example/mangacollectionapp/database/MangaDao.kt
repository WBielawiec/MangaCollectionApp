package com.example.mangacollectionapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mangacollectionapp.models.Manga
import com.example.mangacollectionapp.models.MangaVolume
import com.example.mangacollectionapp.models.MangaWithVolumes
import kotlinx.coroutines.Deferred

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertManga(manga: Manga)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVolume(volume : MangaVolume)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVolumes(volumes : List<MangaVolume>)

    @Update
    suspend fun updateManga(manga: Manga)

    @Update
    suspend fun updateMangaVolumes(volumes : List<MangaVolume>)

    @Delete
    suspend fun deleteManga(manga: Manga)

    @Delete
    suspend fun deleteMangaVolumes(volumes : List<MangaVolume>)



    @Query("SELECT * FROM manga ORDER BY title ASC")
    fun getAllMangas(): List<Manga>

    @Query("SELECT * FROM manga WHERE userStatus = 'Completed' ORDER BY title ASC")
    fun getAllCompletedMangas() : List<Manga>

    @Query("SELECT * FROM manga WHERE userStatus = 'In progress' ORDER BY title ASC")
    fun getAllInProgressMangas() : List<Manga>

    @Query("SELECT * FROM manga WHERE userStatus = 'Wanted' ORDER BY title ASC")
    fun getAllWantedMangas() : List<Manga>

    @Query("SELECT * FROM Volume WHERE mangaId = :mangaId ORDER BY volumeId ASC")
    fun getAllMangaVolumes(mangaId : Int) : List<MangaVolume>

    @Query("SELECT * FROM manga WHERE status = :status ORDER BY title ASC")
    fun getAllMangasByStatus(status: String): LiveData<List<Manga>>

    @Query("SELECT * FROM Manga")
    fun getAllMangaWithVolumes() : LiveData<List<MangaWithVolumes>>

    @Query("SELECT COUNT(*) FROM Volume WHERE mangaId = :mangaId")
    fun countMangaVolumes(mangaId : Int) : String

    @Query("DELETE FROM Manga")
    fun deleteMangas()

    @Transaction
    suspend fun insertMangaWithVolumes(manga: Manga, volumes: List<MangaVolume>) {
        insertManga(manga)
        val mangaId = manga.id
        //val volumesWithMangaId = volumes.map { volume -> volume.copy(volumeId = mangaId) }
        insertVolumes(volumes)
    }

}