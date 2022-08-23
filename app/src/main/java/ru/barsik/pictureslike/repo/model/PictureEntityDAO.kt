package ru.barsik.pictureslike.repo.model

import androidx.room.*
import io.reactivex.rxjava3.core.Single

@Dao
interface PictureEntityDAO {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun savePictureEntity(pictureEntity: PictureEntity) : Single<Long>

    @Transaction
    @Query("DELETE FROM pictures WHERE id=:picId")
    fun deletePictureById(picId: String) : Single<Int>

    @Transaction
    @Query("SELECT * FROM pictures")
    fun readAllSavedPictures() : Single<List<PictureEntity>>
}