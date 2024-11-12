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
import com.lion.a066ex_animalmanager.util.FragmentName

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
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
        // 첫 실행시 보여줄 화면 세팅(MainFragment)
        replaceFragment(FragmentName.MAIN_FRAGMENT, false, null)
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