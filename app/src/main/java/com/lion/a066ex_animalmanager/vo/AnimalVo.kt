package com.lion.a066ex_animalmanager.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AnimalTable")
data class AnimalVo (
    @PrimaryKey(autoGenerate = true)
    var animalIdx:Int = 0,
    var animalType:Int = 0,
    var animalName:String = "",
    var animalAge:Int = 0,
    var animalGender:Int = 0,
    var animalWeight:Int = 0
)