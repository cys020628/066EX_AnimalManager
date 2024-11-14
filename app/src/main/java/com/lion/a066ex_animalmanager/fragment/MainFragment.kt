package com.lion.a066ex_animalmanager.fragment

import android.os.Bundle
import android.util.Log
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
    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var animalRecyclerView: AnimalRecyclerView
    private lateinit var animalRecyclerViewTouchHelper: AnimalRecyclerViewTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        // initialize(초기화)
        initializeComponents()
        // Toolbar
        setUpToolbar()
        // FAB
        setUpFAB()
        // RecyclerView
        setUpRecyclerView()
        // ItemTouchHelper
        setUpItemTouchHelper()
        // 데이터를 가져오고 RecyclerView 갱신
        refreshRecyclerView()
        return fragmentMainBinding.root
    }

    // initialize 세팅
    private fun initializeComponents() {
        mainActivity = activity as MainActivity
        animalRecyclerView = AnimalRecyclerView(mainActivity, mainActivity.animalList)
        animalRecyclerViewTouchHelper = AnimalRecyclerViewTouchHelper(
            mainActivity.animalList,
            fragmentMainBinding.recyclerViewMain,mainActivity
        )
    }


    // Toolbar 세팅
    private fun setUpToolbar() {
        fragmentMainBinding.apply {
            // 타이틀
            if (arguments != null) {
                // BundleData가 있을경우 목록에 따라 타이틀을 분류한다.
                val filterData = arguments?.getString("animal")
                toolbarMain.title = when (filterData) {
                    "dog" -> "강아지 동물 목록"
                    "cat" -> "고양이 동물 목록"
                    "parrot" -> "앵무새 동물 목록"
                    else -> "전체 동물 목록"
                }
            } else {
                toolbarMain.title = "전체 동물 목록"
            }
            // 좌측 네비게이션 버튼
            toolbarMain.setNavigationIcon(R.drawable.menu_24px)
            // 네비게이션 버튼 리스너
            toolbarMain.setNavigationOnClickListener {
                mainActivity.activityMainBinding.drawerlayoutMain.open()
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
        // 이동이나 스와프가 발생했을 때 동작할 요소를 설정
        val animalItemTouchHelper = ItemTouchHelper(animalRecyclerViewTouchHelper)
        animalItemTouchHelper.attachToRecyclerView(fragmentMainBinding.recyclerViewMain)
    }

    // 동물 목록이 어떤건지에 따라 필터링
    fun filterData(animalList: MutableList<AnimalViewModel>): MutableList<AnimalViewModel> {
        // 데이터가 null이 아니라면
        if (arguments != null) {
            val filterData = arguments?.getString("animal")
            val newData = when (filterData) {
                "dog" -> {
                    animalList.filter {
                        it.animalType.str == "강아지"
                    }
                }

                "cat" -> {
                    animalList.filter {
                        it.animalType.str == "고양이"
                    }
                }

                "parrot" -> {
                    animalList.filter {
                        it.animalType.str == "앵무새"
                    }
                }

                else -> {
                    animalList
                }
            }
            return newData.toMutableList()

        } else {
            return animalList
        }
    }

    // 데이터를 가져오고 RecyclerView를 갱신
    fun refreshRecyclerView() {
        // 동물 정보를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val selectWork = async(Dispatchers.IO) {
                // 동물 정보를 가져온다.
                val animalData = AnimalRepository.selectAnimalDataAll(mainActivity)
                // 선택된 동물에 따라 데이터를 가공
                filterData(animalData)
            }
            val newList = selectWork.await()
            // recyclerView 갱신(adapter에 있는 changeData 함수를 통해 직접 갱신)
            animalRecyclerView.changeData(newList)
        }
    }
}