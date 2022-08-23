package ru.barsik.pictureslike.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PictureEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var author: String? = null,
    var width : Int = 0,
    var height : Int = 0,
    var url: String? = null,
    var download_url: String? = null
)