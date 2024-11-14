package com.lion.a066ex_animalmanager.itemTouchHelper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeFlag
import androidx.recyclerview.widget.RecyclerView
import com.lion.a066ex_animalmanager.viewModel.AnimalViewModel

class AnimalRecyclerViewTouchHelper(
     // 생성자의 인자로 animalList와 recyclerView를 받는다.
    private val animalList: MutableList<AnimalViewModel>, // 동물 목록
    private val recyclerView : RecyclerView // RecyclerView
) : ItemTouchHelper.Callback() {

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
        recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
    }
}