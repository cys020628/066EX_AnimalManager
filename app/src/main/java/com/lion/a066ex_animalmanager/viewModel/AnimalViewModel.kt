package com.lion.a066ex_animalmanager.viewModel

import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType

data class AnimalViewModel (
    var animalIdx:Int,
    var animalType: AnimalType,
    var animalName:String,
    var animalAge: Int,
    var animalGender: AnimalGender,
    var animalWeight:Int
)