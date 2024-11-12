package com.lion.a066ex_animalmanager.util

// 프래그먼트들의 이름을 나타내는 값
enum class FragmentName(var number:Int, var str: String) {
    // 메인화면
    MAIN_FRAGMENT(1,"MainFragment"),
    // 입력화면
    INPUT_FRAGMENT(2,"InputFragment")
}