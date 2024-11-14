package com.lion.a066ex_animalmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.ActivityMainBinding
import com.lion.a066ex_animalmanager.databinding.FragmentMainBinding
import com.lion.a066ex_animalmanager.databinding.RowMainBinding
import com.lion.a066ex_animalmanager.itemTouchHelper.AnimalRecyclerViewTouchHelper
import com.lion.a066ex_animalmanager.recyclerView.AnimalRecyclerView
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066ex_animalmanager.util.FragmentName
import com.lion.a066ex_animalmanager.viewModel.AnimalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    // 임시 데이터
//    val animalList = MutableList(10) {
//        "강아지 $it"
//    }

    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var animalRecyclerView: AnimalRecyclerView
    private lateinit var animalRecyclerViewTouchHelper: AnimalRecyclerViewTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        animalRecyclerView = AnimalRecyclerView(mainActivity, mainActivity.animalList)
        animalRecyclerViewTouchHelper = AnimalRecyclerViewTouchHelper(
            mainActivity.animalList,
            fragmentMainBinding.recyclerViewMain
        )
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        // Toolbar
        setUpToolbar()
        // FAB
        setUpFAB()
        // RecyclerView
        setUpRecyclerView()
        // ItemTouchHelper
        setUpItemTouchHelper()
        // Navigation
        setUpNavigation()
        // 데이터를 가져오고 RecyclerView 갱신
        selectAnimalDataAndRefreshRecyclerView()
        return fragmentMainBinding.root
    }

    // Toolbar 세팅
    private fun setUpToolbar() {
        fragmentMainBinding.apply {
            // 타이틀
            toolbarMain.title = "전체 동물 목록"
            // 좌측 네비게이션 버튼
            toolbarMain.setNavigationIcon(R.drawable.menu_24px)
            // 네비게이션 버튼 리스너
            toolbarMain.setNavigationOnClickListener {
                drawerlayoutMain.open()
            }
        }
    }

    // FAB 세팅
    private fun setUpFAB() {
        fragmentMainBinding.apply {
            fabMainAdd.setOnClickListener {
                // InputFragment 이동
                mainActivity.replaceFragment(FragmentName.INPUT_FRAGMENT, true, null)
            }
        }
    }

    // RecyclerView 세팅
    private fun setUpRecyclerView() {
        fragmentMainBinding.apply {
            // Adapter
            //recyclerViewMain.adapter = AnimalRecyclerViewAdapter()
            recyclerViewMain.adapter = animalRecyclerView
            // LayoutManager
            recyclerViewMain.layoutManager = LinearLayoutManager(mainActivity)
            // Divider
            val deco =
                MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewMain.addItemDecoration(deco)
        }
    }

    // ItemTouchHelper 설정
    private fun setUpItemTouchHelper() {
        // 이동이나 스와프가 발생했을 때 동작할 요소를 설정해준다.
        val animalItemTouchHelper = ItemTouchHelper(animalRecyclerViewTouchHelper)
        animalItemTouchHelper.attachToRecyclerView(fragmentMainBinding.recyclerViewMain)
    }

    // Navigation 설정
    private fun setUpNavigation() {
        fragmentMainBinding.apply {
            navigationView.inflateMenu(R.menu.main_navigation_menu)
            // 동물 전체 보기 메뉴를 선택된 상태로 설정
            navigationView.setCheckedItem(R.id.navigationMenuShowAllAnimalInfo)

            // 누른 아이템에 대한 리스너 처리
            navigationView.setNavigationItemSelectedListener {
                if (it.isCheckable == true) {
                    // 체크 상태를 true로 준다.
                    it.isChecked = true
                }

                when (it.itemId) {
                    R.id.navigationMenuShowDogInfo -> {
                        // 강아지 정보 전체 보기에 대한 처리 필요
                    }

                    R.id.navigationMenuShowCatInfo -> {
                        //  고양이 정보 전체 보기에 대한 처리 필요
                    }

                    R.id.navigationMenuShowParrotInfo -> {
                        // 앵무새 정보 전체 보기에 대한 처리 필요
                    }
                }
                // NavigationView를 닫아준다.
                drawerlayoutMain.close()
                true
            }
        }
    }

    // 데이터를 가져오고 RecyclerView를 갱신한다.
    fun selectAnimalDataAndRefreshRecyclerView() {
        // 동물 정보를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val selectWork = async(Dispatchers.IO) {
                // 동물 정보를 가져온다.
                AnimalRepository.selectAnimalDataAll(mainActivity)
            }
            val newList = selectWork.await()
            // recyclerView 갱신(adapter에 있는 changeData 함수를 통해 직접 갱신한다.)
            animalRecyclerView.changeData(newList)
        }
    }
}