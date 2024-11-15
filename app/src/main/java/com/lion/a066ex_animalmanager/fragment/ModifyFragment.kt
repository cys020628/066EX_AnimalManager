package com.lion.a066ex_animalmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentModifyBinding
import com.lion.a066ex_animalmanager.databinding.InputDialogLayoutBinding
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.util.FragmentName
import com.lion.a066ex_animalmanager.viewModel.AnimalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ModifyFragment : Fragment() {

    private lateinit var fragmentModifyBinding: FragmentModifyBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentModifyBinding = FragmentModifyBinding.inflate(layoutInflater)
        // initialize
        initializeComponents()
        // Toolbar
        setUpToolbar()
        // 초기값 설정
        inputInitialValue()
        return fragmentModifyBinding.root
    }

    // initialize 세팅
    private fun initializeComponents() {
        mainActivity = activity as MainActivity
    }

    // Toolbar 세팅
    private fun setUpToolbar() {
        fragmentModifyBinding.apply {
            toolbarModify.title = "동물 정보 수정"
            // 네비게이션 아이콘
            toolbarModify.setNavigationIcon(R.drawable.arrow_back_24px)
            // 네비게이션 아이콘 클릭 리스터
            toolbarModify.setNavigationOnClickListener { mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT) }
            // 메뉴 등록
            toolbarModify.inflateMenu(R.menu.modify_toolbar_menu)
            // 메뉴 클릭 리스너
            toolbarModify.setOnMenuItemClickListener {
                when (it.itemId) {
                    // 수정 정보 저장
                    R.id.modify_toolbar_menu_save -> {
                        // 빈공간이 있는지 검사한다.
                        if (isEmptyTextSW()) {
                            // 빈공간이 없다면
                            showDialog(1)
                        } else {
                            // 빈공간이 있다면
                            showDialog(2)
                        }
                    }
                }
                true
            }
        }
    }

    // 입력 요소들 초기 설정
    private fun inputInitialValue() {
        fragmentModifyBinding.apply {
            // arguments가 있는지 검사한다.
            if (arguments != null) {
                // 동물 번호를 가져온다.
                val animalIdx = arguments?.getInt("animalIdx")!!
                // 동물 데이터를 가져온다.
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO) {
                        AnimalRepository.selectAnimalInfoByAnimalIdx(mainActivity, animalIdx)
                    }
                    val animalViewModel = work1.await()

                    // 종류 toggleButton
                    when (animalViewModel.animalType) {
                        AnimalType.ANIMAL_TYPE_DOG -> {
                            toggleModifyGroupType.check(R.id.buttonModifyTypeDog)
                        }

                        AnimalType.ANIMAL_TYPE_CAT -> {
                            toggleModifyGroupType.check(R.id.buttonModifyTypeCat)
                        }

                        AnimalType.ANIMAL_TYPE_PARROT -> {
                            toggleModifyGroupType.check(R.id.buttonModifyTypeParrot)
                        }
                    }
                    // 이름
                    textFieldModifyName.editText?.setText(animalViewModel.animalName)
                    // 나이
                    textFieldModifyAge.editText?.setText(animalViewModel.animalAge.toString())

                    // 성별 toggleButton
                    when (animalViewModel.animalGender) {
                        AnimalGender.ANIMAL_GENDER_MALE -> {
                            toggleModifyGroupGender.check(R.id.buttonModifyGenderMale)
                        }

                        AnimalGender.ANIMAL_GENDER_FEMALE -> {
                            toggleModifyGroupGender.check(R.id.buttonModifyGenderFemale)
                        }
                    }

                    // 몸무게
                    sliderModifyAnimalWeight.value = animalViewModel.animalWeight.toFloat()
                }
            }
        }
    }

    private fun showDialog(value: Int) {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)

        when (value) {
            1 -> {
                materialAlertDialogBuilder.setTitle("수정")
                materialAlertDialogBuilder.setMessage("이전 데이터로 복원할 수 없습니다")
                materialAlertDialogBuilder.setNeutralButton("취소", null)
                materialAlertDialogBuilder.setPositiveButton("수정") { dialogInterface: DialogInterface, i: Int ->
                    editAnimalData()
                }
                materialAlertDialogBuilder.show()
            }

            2 -> {
                val dialogBinding = InputDialogLayoutBinding.inflate(mainActivity.layoutInflater)
                materialAlertDialogBuilder.setView(dialogBinding.root)
                val dialog = materialAlertDialogBuilder.create()
                dialogBinding.dialogTitle.text = "오류"
                dialogBinding.dialogMessage.text = "빈공간이 있습니다.\n다시 입력해주세요."

                dialogBinding.dialogButton.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }


    // 빈공간이 있는지 검사 하는 메서드
    private fun isEmptyTextSW(): Boolean {
        fragmentModifyBinding.apply {
            if ((textFieldModifyName.editText?.text?.isEmpty() == true) or
                (textFieldModifyAge.editText?.text?.isEmpty() == true)
            ) {
                return false
            } else {
                return true
            }
        }
    }

    // 수정 메서드
    private fun editAnimalData() {
        fragmentModifyBinding.apply {
            // arguments가 있는지 검사한다.
            if (arguments != null) {
                // 동물 번호를 가져온다.
                val animalIdx = arguments?.getInt("animalIdx")!!

                // 동물 종류
                val animalType = when (toggleModifyGroupType.checkedButtonId) {
                    R.id.buttonModifyTypeDog -> AnimalType.ANIMAL_TYPE_DOG
                    R.id.buttonModifyTypeCat -> AnimalType.ANIMAL_TYPE_CAT
                    else -> AnimalType.ANIMAL_TYPE_PARROT
                }

                // 동물 이름
                val animalName = textFieldModifyName.editText?.text.toString()

                // 동물 나이
                val animalAge = textFieldModifyAge.editText?.text.toString().toInt()

                // 동물 성별
                val animalGender = when (toggleModifyGroupGender.checkedButtonId) {
                    R.id.buttonModifyGenderMale -> AnimalGender.ANIMAL_GENDER_MALE
                    else -> AnimalGender.ANIMAL_GENDER_FEMALE
                }

                // 동물 몸무게
                val animalWeight = sliderModifyAnimalWeight.value.toInt()

                // AnimalViewModel에 담는다.
                val animalViewModel = AnimalViewModel(animalIdx,animalType,animalName,animalAge,animalGender,animalWeight)

                // 동물 정보를 수정
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        AnimalRepository.updateAnimalInfo(mainActivity,animalViewModel)
                    }
                    work1.join()
                    mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
                }
            }
        }
    }
}