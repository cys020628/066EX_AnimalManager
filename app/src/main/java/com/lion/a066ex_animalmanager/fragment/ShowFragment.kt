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
import com.lion.a066ex_animalmanager.databinding.FragmentShowBinding
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ShowFragment : Fragment() {

    private lateinit var fragmentShowBinding: FragmentShowBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowBinding = FragmentShowBinding.inflate(layoutInflater)
        // initialize
        initializeComponents()
        // Toolbar
        setUpToolbar()
        // SetTextData
        setTextData()
        return fragmentShowBinding.root
    }

    // initialize 세팅
    private fun initializeComponents() {
        mainActivity = activity as MainActivity
    }

    // Toolbar 세팅
    private fun setUpToolbar() {
        fragmentShowBinding.apply {
            // 타이틀
            toolbarShow.title = "동물 정보 보기"
            // 네비게이션 아이콘
            toolbarShow.setNavigationIcon(R.drawable.arrow_back_24px)
            // 네비게이션 버튼 리스너
            toolbarShow.setNavigationOnClickListener { mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT) }
            // 메뉴 등록
            toolbarShow.inflateMenu(R.menu.show_toolbar_menu)
            // 메뉴 클릭 리스너
            toolbarShow.setOnMenuItemClickListener {
                when(it.itemId) {
                    // 정보 수정
                    R.id.show_toolbar_menu_modify -> {
                        // 동물 번호 담기
                        val dataBundle = Bundle()
                        dataBundle.putInt("animalIdx", arguments?.getInt("animalIdx")!!)
                        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT,true,dataBundle)
                    }
                    // 정보 삭제
                    R.id.show_toolbar_menu_delete -> {
                        // 삭제 처리 다이얼로그
                        showDeleteDialog()
                    }
                }
                true
            }
        }
    }

    // 동물 정보 삭제
    private fun deleteAnimalInfo() {
        // arguments가 있는지 검사한다.
        if(arguments != null) {
            val animalIdx = arguments?.getInt("animalIdx")!!

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    AnimalRepository.deleteAnimalInfoByStudentIdx(mainActivity, animalIdx)
                }
                work1.join()
                // MainFragment 이동
                mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
            }
        }
    }

    // 다이얼로그
    fun showDeleteDialog(){
        // 다이얼로그를 띄워주다.
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("삭제")
        materialAlertDialogBuilder.setMessage("삭제를 할 경우 복원이 불가능합니다")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
            // 삭제 처리 메서드
            deleteAnimalInfo()
        }
        materialAlertDialogBuilder.show()
    }

    // 텍스트 세팅
    private fun setTextData() {
        fragmentShowBinding.apply {
            // arguments가 있는지 검사한다.
            if(arguments != null) {
                // 텍스트뷰 초기화
                textViewShowAnimalType.text = ""
                textViewShowAnimalName.text = ""
                textViewShowAnimalAge.text = ""
                textViewShowAnimalGender.text = ""
                textViewShowAnimalWeight.text = ""

                // 학생 번호 추출
                val animalIdx = arguments?.getInt("animalIdx")

                // 학생 데이터를 가져와 출력한다.
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        AnimalRepository.selectAnimalInfoByAnimalIdx(mainActivity, animalIdx!!)
                    }
                    val studentViewModel = work1.await()

                    // 설정한 문자열 가져오기
                    val animalTypeFormat = String.format(getString(R.string.animalTypeFormat),studentViewModel.animalType.str)
                    val animalNameFormat = String.format(getString(R.string.animalNameFormat),studentViewModel.animalName)
                    val animalAgeFormat = String.format(getString(R.string.animalAgeFormat),studentViewModel.animalAge.toString())
                    val animalGenderFormat = String.format(getString(R.string.animalGenderFormat),studentViewModel.animalGender.str)
                    val animalWeightFormat = String.format(getString(R.string.animalWeightFormat),studentViewModel.animalWeight.toString())

                    // 동물 종류
                    fragmentShowBinding.textViewShowAnimalType.text = animalTypeFormat
                    // 동물 이름
                    fragmentShowBinding.textViewShowAnimalName.text = animalNameFormat
                    // 동물 나이
                    fragmentShowBinding.textViewShowAnimalAge.text = animalAgeFormat
                    // 동물 성별
                    fragmentShowBinding.textViewShowAnimalGender.text = animalGenderFormat
                    // 동물 몸무게
                    fragmentShowBinding.textViewShowAnimalWeight.text = animalWeightFormat
                }
            }
        }
    }
}