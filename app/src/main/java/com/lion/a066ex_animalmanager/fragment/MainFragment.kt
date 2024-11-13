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
import com.lion.a066ex_animalmanager.util.FragmentName


class MainFragment : Fragment() {

    // 임시 데이터
    val animalList = MutableList(10) {
        "강아지 $it"
    }

    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
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
            recyclerViewMain.adapter = AnimalRecyclerViewAdapter()
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
        val animalRecyclerViewTouchHelper = AnimalRecyclerViewTouchHelper()
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

    // RecyclerViewAdapter
    private inner class AnimalRecyclerViewAdapter() :
        RecyclerView.Adapter<AnimalRecyclerViewAdapter.AnimalViewHolder>() {

        // ViewHolder
        inner class AnimalViewHolder(val rowMainBinding: RowMainBinding) :
            RecyclerView.ViewHolder(rowMainBinding.root), OnClickListener {
            override fun onClick(v: View?) {
                // 누른 동물의 번호를 담아준다.
                val dataBundle = Bundle()
                dataBundle.putInt("animalIdx", adapterPosition)
                // ShowFragment로 이동
                mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true, dataBundle)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater, parent, false)
            val holder = AnimalViewHolder(rowMainBinding)

            // 리스너 설정
            rowMainBinding.root.setOnClickListener(holder)
            return holder
        }

        override fun getItemCount(): Int {
            return animalList.size
        }

        override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
            holder.rowMainBinding.apply {
                textViewRowAnimalName.text = animalList[position]
            }
        }
    }

    // ItemTouchHelper
    private inner class AnimalRecyclerViewTouchHelper() : ItemTouchHelper.Callback() {

        // 이동 및 스와이프 설정
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            // 좌우로 슬라이드 가능하게 설정
            val flag = makeFlag(
                ItemTouchHelper.ACTION_STATE_SWIPE,
                ItemTouchHelper.START or ItemTouchHelper.END
            )
            return flag
        }

        // 항목 이동 시 설정
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // 가로만 슬라이드 하기 때문에 return false 반환
            return false
        }

        // 항목 스와이프 시 설정
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // 스와이프 된 순서의 항목을 제거
            animalList.removeAt(viewHolder.adapterPosition)
            // 삭제된 항목의 RecyclerView 갱신
            fragmentMainBinding.recyclerViewMain.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
        }
    }
}