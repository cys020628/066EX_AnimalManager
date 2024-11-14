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

        // 동물 정보 전체를 읽어오기 위한 리스너
        fun selectAnimalDataAll(context: Context): MutableList<AnimalViewModel> {
            // 데이터 베이스 객체를 가져온다.
            val animalDatabase = AnimalDatabase.getInstance(context)

            // 동물 데이터 전체를 가져온다.
            val animalVoList = animalDatabase?.animalDAO()?.selectAnimalDataAll()

            // 동물 데이터를 담을 리스트
            val animalViewModelList = mutableListOf<AnimalViewModel>()

            // 동물의 수 만큼 반복
            animalVoList?.forEach {
                // 동물 데이터 추출

                // 동물 타입
                val animalType = when(it.animalType) {
                   AnimalType.ANIMAL_TYPE_DOG.number -> AnimalType.ANIMAL_TYPE_DOG
                   AnimalType.ANIMAL_TYPE_CAT.number -> AnimalType.ANIMAL_TYPE_CAT
                   else -> AnimalType.ANIMAL_TYPE_PARROT
                }

                // 동물 성별
                val  animalGender = when(it.animalGender) {
                    AnimalGender.ANIMAL_GENDER_MALE.number -> AnimalGender.ANIMAL_GENDER_MALE
                    else -> AnimalGender.ANIMAL_GENDER_FEMALE
                }

                // 동물 Idx
                val animalIdx = it.animalIdx

                // 동물 이름
                val animalName = it.animalName

                // 동물 나이
                val animalAge = it.animalAge

                // 동물 무게
                val animalWeight = it.animalWeight

                // 객체에 담는다.
                val animalViewModel = AnimalViewModel(animalIdx,animalType,animalName,animalAge,animalGender,animalWeight)

                // 리스트에 담는다.
                animalViewModelList.add(animalViewModel)
            }
            return animalViewModelList
        }
    }
}