package com.lion.a066ex_animalmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentModifyBinding
import com.lion.a066ex_animalmanager.util.FragmentName


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
                        // ShowFragment 이동
                        mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
                    }
                }
                true
            }
        }
    }
}