package com.lion.a066ex_animalmanager.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lion.a066ex_animalmanager.vo.AnimalVo

@Dao
interface AnimalDao {

    // 동물 정보 저장
    @Insert
    fun insertAnimalData(animalVo: AnimalVo)

    // 동물 정보를 가져오는 메서드
    @Query("""
        select * from AnimalTable
        order by animalIdx desc """)
    fun selectAnimalDataAll(): List<AnimalVo>
    // order by animalIdx desx : IDX의 순서대로 정렬

    // 동물 한마리의 정보를 가져오는 메서드
    @Query("""
        select * from AnimalTable
        where animalIdx = :animalIdx""")
    fun selectAnimalDataByAnimalIdx(animalIdx:Int):AnimalVo

    // 동물 한마리의 정보를 삭제하는 메서드
    @Delete
    fun deleteAnimalInfo(animalVo: AnimalVo)

    // 동물 한마리의 정보를 수정하는 메서드
    @Update
    fun updateAnimalData(animalVo: AnimalVo)
}