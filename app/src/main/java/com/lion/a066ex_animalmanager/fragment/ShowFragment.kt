package com.lion.a066ex_animalmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentShowBinding
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.FragmentName


class ShowFragment : Fragment() {

    private lateinit var fragmentShowBinding: FragmentShowBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentShowBinding = FragmentShowBinding.inflate(layoutInflater)
        // Toolbar
        setUpToolbar()
        // SetTextData
        setTextData()
        return fragmentShowBinding.root
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
                        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT,true,null)
                    }
                    // 정보 삭제
                    R.id.show_toolbar_menu_delete -> {
                        // MainFragment 이동
                        mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
                    }
                }
                true
            }
        }
    }

    // 텍스트 세팅
    private fun setTextData() {
        fragmentShowBinding.apply {
            //동물 종류
            textViewShowAnimalType.text = "강아지"

            // 동물 이름
            textViewShowAnimalName.text = "이름"

            // 동물 나이
            textViewShowAnimalAge.text = "나이"

            // 성별
            textViewShowAnimalGender.text = "1살"

            // 몸무게
            textViewShowAnimalWeight.text = "10Kg"
        }

    }
}