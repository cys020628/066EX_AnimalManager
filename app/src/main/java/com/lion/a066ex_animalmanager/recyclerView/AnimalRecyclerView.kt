package com.lion.a066ex_animalmanager.recyclerView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.RowMainBinding
import com.lion.a066ex_animalmanager.util.FragmentName
import com.lion.a066ex_animalmanager.viewModel.AnimalViewModel

class AnimalRecyclerView(
    // 생성자의 인자로 MainActivity와 animalList를 받는다.
    private val mainActivity: MainActivity, // MainActivity
    private var animalList: MutableList<AnimalViewModel> // 동물 목록
) : RecyclerView.Adapter<AnimalRecyclerView.AnimalViewHolder>() {

    // ViewHolder
    inner class AnimalViewHolder(val rowMainBinding: RowMainBinding) :
        RecyclerView.ViewHolder(rowMainBinding.root), OnClickListener {
        override fun onClick(v: View?) {
            // 누른 동물의 번호를 담아준다.
            val dataBundle = Bundle()
            dataBundle.putInt("animalIdx", animalList[adapterPosition].animalIdx)
            Log.e("recyclerViewDIdx","reyclerView${animalList[adapterPosition].animalIdx}")
            // ShowFragment로 이동
            mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true, dataBundle)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimalViewHolder {
        val rowMainBinding = RowMainBinding.inflate(mainActivity.layoutInflater, parent, false)
        val holder = AnimalViewHolder(rowMainBinding)

        // 리스너 설정
        rowMainBinding.root.setOnClickListener(holder)
        return holder
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    override fun onBindViewHolder(
        holder: AnimalViewHolder,
        position: Int
    ) {
        holder.rowMainBinding.apply {
            textViewRowAnimalName.text = animalList[position].animalName

            imageViewRowAnimalType.setImageResource(when(animalList[position].animalType.str) {
                "강아지" -> R.drawable.dog_icon
                "고양이" -> R.drawable.cat_icon
                else -> R.drawable.parrot_icon
            })
        }
    }

    // animalList를 받아와 데이터를 갱신
    fun changeData(newList: MutableList<AnimalViewModel>) {
        animalList = newList
        notifyItemRangeChanged(0,newList.size)  // 데이터 갱신
    }
}

