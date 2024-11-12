package com.lion.a066ex_animalmanager.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AnimalTable")
data class AnimalVO (
    @PrimaryKey(autoGenerate = true)
    var animalIdx:Int = 0,
    var animalType:String = "",
    var animalName:String = "",
    var animalAge:Int = 0,
)