package com.lion.a066ex_animalmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return fragmentMainBinding.root
    }

    // Toolbar 세팅
    private fun setUpToolbar() {
        fragmentMainBinding.apply {
            toolbarMain.title = "전체 동물 목록"
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

    // RecyclerViewAdapter
    private inner class AnimalRecyclerViewAdapter() :

        RecyclerView.Adapter<AnimalRecyclerViewAdapter.AnimalViewHolder>() {
        // ViewHolder
        inner class AnimalViewHolder(val rowMainBinding: RowMainBinding) :
            RecyclerView.ViewHolder(rowMainBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater, parent, false)
            val holder = AnimalViewHolder(rowMainBinding)
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