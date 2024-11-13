package com.lion.a066ex_animalmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentInputBinding
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.util.FragmentName


class InputFragment : Fragment() {

    private lateinit var fragmentInputBinding: FragmentInputBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentInputBinding = FragmentInputBinding.inflate(layoutInflater)
        // Toolbar
        setUpToolbar()
        return fragmentInputBinding.root
    }

    // Toolbar 세팅
    private fun setUpToolbar() {
        fragmentInputBinding.apply {
            toolbarInput.title = "동물 정보 입력"
            // 네비게이션 아이콘
            toolbarInput.setNavigationIcon(R.drawable.arrow_back_24px)
            // 네비게이션 아이콘 클릭 리스터
            toolbarInput.setNavigationOnClickListener { mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT) }
            // 메뉴 등록
            toolbarInput.inflateMenu(R.menu.input_toolbar_menu)
            // 메뉴 클릭 리스너
            toolbarInput.setOnMenuItemClickListener {
                when (it.itemId) {
                    // 정보 저장
                    R.id.show_toolbar_menu_modify -> saveData()
                }
                true
            }
        }
    }

    // 저장 세팅
    private fun saveData() {
        fragmentInputBinding.apply {
            // 동물 타입
            val animalType = when (toggleGroupType.checkedButtonId) {
                R.id.buttonTypeDog -> AnimalType.ANIMAL_TYPE_DOG
                R.id.buttonTypeCat -> AnimalType.ANIMAL_TYPE_CAT
                else -> AnimalType.ANIMAL_TYPE_PARROT
            }

            // 동물 이름
            val animalName = textFieldInputName.editText?.text.toString()

            // 동물 나이
            val animalAge = textFieldInputAge.editText?.text.toString()

            // 성별
            val animalGender = when (toggleGroupGender.checkedButtonId) {
                R.id.buttonGenderMale -> AnimalGender.ANIMAL_GENDER_MALE
                else -> AnimalGender.ANIMAL_GENDER_FEMALE
            }

            // 몸무게
            val animalWeight = sliderAnimalWeight.value

            // MainFragment로 돌아간다.
            mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
        }
    }
}