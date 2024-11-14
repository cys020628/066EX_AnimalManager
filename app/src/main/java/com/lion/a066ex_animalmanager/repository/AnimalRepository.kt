package com.lion.a066ex_animalmanager.repository

import android.content.Context
import com.lion.a066ex_animalmanager.database.AnimalDatabase
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.viewModel.AnimalViewModel
import com.lion.a066ex_animalmanager.vo.AnimalVo

class AnimalRepository(context: Context) {
    companion object {

        // 동물 정보를 저장하기 위한 메서드
        fun insertAnimalInfo(context: Context, animalViewModel: AnimalViewModel) {
            // 데이터 베이스 객체를 가져온다.
            val animalDatabase = AnimalDatabase.getInstance(context)

            // Viemodel에 있는 데이터를 vo에 담아준다.
            val animalType = animalViewModel.animalType.number
            val animalName = animalViewModel.animalName
            val animalAge = animalViewModel.animalAge
            val animalGender = animalViewModel.animalGender.number
            val animalWeight = animalViewModel.animalWeight

            // Animal Vo 객체를 생성한다.
            val animalVO = AnimalVo(
                animalType = animalType,
                animalName = animalName,
                animalAge = animalAge,
                animalGender = animalGender,
                animalWeight = animalWeight,
            )

            // animalVO를 저장한다.
            animalDatabase?.animalDAO()?.insertAnimalData(animalVO)
        }
    }
}