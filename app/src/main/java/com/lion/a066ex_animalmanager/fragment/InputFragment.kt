package com.lion.a066ex_animalmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentInputBinding
import com.lion.a066ex_animalmanager.databinding.InputDialogLayoutBinding
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.util.FragmentName
import com.lion.a066ex_animalmanager.viewModel.AnimalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.job
import kotlinx.coroutines.launch


class InputFragment : Fragment() {

    private lateinit var fragmentInputBinding: FragmentInputBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentInputBinding = FragmentInputBinding.inflate(layoutInflater)
        // initialize
        initializeComponents()
        // Toolbar
        setUpToolbar()
        return fragmentInputBinding.root
    }

    // initialize 세팅
    private fun initializeComponents() {
        mainActivity = activity as MainActivity
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
                    R.id.show_toolbar_menu_modify -> {
                        if (isEmptyTextSW()) {
                            // 빈공간이 없는 경우
                            showDialog( "저장", "저장 완료되었습니다",true)
                        } else {
                            // 빈공간이 있는 경우ㅊ
                            showDialog("오류", "빈공간이 있습니다.\n다시 입력해주세요.",false)
                        }
                    }
                }
                true
            }
        }
    }

    private fun showDialog(title: String, message: String, saveSW: Boolean) {
        // 다이얼로그를 띄워주다.
        val dialogBinding = InputDialogLayoutBinding.inflate(mainActivity.layoutInflater)
        val materialAlertDialogBuilder =
            MaterialAlertDialogBuilder(mainActivity)

        materialAlertDialogBuilder.setView(dialogBinding.root)

        val dialog = materialAlertDialogBuilder.create()

        dialogBinding.dialogTitle.text = title
        dialogBinding.dialogMessage.text = message

        dialogBinding.dialogButton.setOnClickListener {
            // saveSW가 true를 반환하면 데이터를 저장 false를 반환하면 저장하지 않는다.
            if (saveSW) {
                saveData()
                dialog.dismiss()
            } else {
                dialog.dismiss()
            }
        }

        dialog.show()
    }


    // 빈공간이 있는지 검사 하는 메서드
    private fun isEmptyTextSW(): Boolean {
        fragmentInputBinding.apply {
            if ((textFieldInputName.editText?.text?.isEmpty() == true) or
                (textFieldInputAge.editText?.text?.isEmpty() == true)
            ) {
                return false
            } else {
                return true
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
            val animalAge = textFieldInputAge.editText?.text.toString().toInt()

            // 성별
            val animalGender = when (toggleGroupGender.checkedButtonId) {
                R.id.buttonGenderMale -> AnimalGender.ANIMAL_GENDER_MALE
                else -> AnimalGender.ANIMAL_GENDER_FEMALE
            }

            // 몸무게
            val animalWeight = sliderAnimalWeight.value.toInt()
            Log.e("test", "animalWeight : $animalWeight")

            // 객체에 담는다
            val animalViewModel =
                AnimalViewModel(0, animalType, animalName, animalAge, animalGender, animalWeight)

            // 데이터를 저장한다.
            CoroutineScope(Dispatchers.Main).launch {
                val saveWork = async(Dispatchers.IO) {
                    // 저장한다.
                    AnimalRepository.insertAnimalInfo(mainActivity, animalViewModel)
                }
                // 저장 작업이 끝날때 까지 기다린다.
                saveWork.join()

                // MainFragment로 돌아간다.
                mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
            }
        }
    }
}