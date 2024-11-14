package com.lion.a066ex_animalmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.a066ex_animalmanager.databinding.ActivityMainBinding
import com.lion.a066ex_animalmanager.fragment.InputFragment
import com.lion.a066ex_animalmanager.fragment.MainFragment
import com.lion.a066ex_animalmanager.fragment.ModifyFragment
import com.lion.a066ex_animalmanager.fragment.ShowFragment
import com.lion.a066ex_animalmanager.util.FragmentName
import com.lion.a066ex_animalmanager.viewModel.AnimalViewModel

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    // 동물 데이터
    var animalList = mutableListOf<AnimalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Navigation
        setUpNavigation()
        // 첫 실행시 보여줄 화면 세팅(MainFragment)
        replaceFragment(FragmentName.MAIN_FRAGMENT, false, null)
    }

    // Navigation 설정
    private fun setUpNavigation() {
        activityMainBinding.apply {
            navigationView.inflateMenu(R.menu.main_navigation_menu)
            // 동물 전체 보기 메뉴를 선택된 상태로 설정
            navigationView.setCheckedItem(R.id.navigationMenuShowAllAnimalInfo)

            // 누른 아이템에 대한 리스너 처리
            navigationView.setNavigationItemSelectedListener {
                if (it.isCheckable == true) {
                    // 체크 상태를 true로 준다.
                    it.isChecked = true
                }
                val bundleData = Bundle()

                when (it.itemId) {
                    R.id.navigationMenuShowDogInfo -> {
                        // 강아지 정보 전체 보기에 대한 처리 필요
                        bundleData.putString("animal", "dog")
                        replaceFragment(FragmentName.MAIN_FRAGMENT, false, bundleData)
                    }

                    R.id.navigationMenuShowCatInfo -> {
                        //  고양이 정보 전체 보기에 대한 처리 필요
                        bundleData.putString("animal", "cat")
                        replaceFragment(FragmentName.MAIN_FRAGMENT, false, bundleData)
                    }

                    R.id.navigationMenuShowParrotInfo -> {
                        // 앵무새 정보 전체 보기에 대한 처리 필요
                        bundleData.putString("animal", "parrot")
                        replaceFragment(FragmentName.MAIN_FRAGMENT, false, bundleData)
                    }

                    R.id.navigationMenuShowAllAnimalInfo -> {
                        // 동물 전체 보기에 대한 처리
                        bundleData.putString("animal", "all")
                        replaceFragment(FragmentName.MAIN_FRAGMENT, false, bundleData)
                    }
                }
                // NavigationView를 닫는다.
                drawerlayoutMain.close()
                true
            }
        }
    }

    // 프래그먼트 교체 함수
    fun replaceFragment(
        fragmentName: FragmentName,
        isAddToBackStack: Boolean,
        dataBundle: Bundle?
    ) {
        // 프래그먼트 객체
        val newFragment = when (fragmentName) {
            FragmentName.MAIN_FRAGMENT -> MainFragment()
            FragmentName.INPUT_FRAGMENT -> InputFragment()
            FragmentName.SHOW_FRAGMENT -> ShowFragment()
            FragmentName.MODIFY_FRAGMENT -> ModifyFragment()
        }

        // bundle 객체가 null이 아니라면
        if (dataBundle != null) {
            newFragment.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

            newFragment.exitTransition =
                MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
            newFragment.reenterTransition =
                MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            newFragment.enterTransition =
                MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
            newFragment.returnTransition =
                MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)

            replace(R.id.containerMain, newFragment)
            if (isAddToBackStack) {
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: FragmentName) {
        supportFragmentManager.popBackStack(
            fragmentName.str,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}