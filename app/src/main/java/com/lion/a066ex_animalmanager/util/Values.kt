package com.lion.a066ex_animalmanager.util

// 프래그먼트들의 이름을 나타내는 값
enum class FragmentName(var number:Int, var str: String) {
    // 메인화면
    MAIN_FRAGMENT(1,"MainFragment"),
    // 입력화면
    INPUT_FRAGMENT(2,"InputFragment")
}

// 동물들 종류를 나타내는 값
enum class AnimalType(var number: Int, var str: String) {
    // 강아지
    ANIMAL_TYPE_DOG(1, "강아지"),
    // 고양이
    ANIMAL_TYPE_CAT(2, "고양이"),
    // 앵무새
    ANIMAL_TYPE_PARROT(3, "앵무새")
}