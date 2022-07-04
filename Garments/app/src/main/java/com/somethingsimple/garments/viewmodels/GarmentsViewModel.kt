package com.somethingsimple.garments.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.somethingsimple.garments.models.Garment
import com.somethingsimple.garments.models.GarmentsDataModel
import com.somethingsimple.garments.models.MoshiArrayListCustomAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.util.*
import kotlin.collections.ArrayList

class GarmentsViewModel : ViewModel() {

    var sortMode: SortMode = SortMode.ALPHA

    private var garmentsModel :ArrayList<Garment>? = null

    var responseLiveData = MutableLiveData<ArrayList<Garment>>()
        private set

    fun fetchData(inputSource: String) {
        val moshi: Moshi = Moshi.Builder().add(MoshiArrayListCustomAdapter()).build()
        val adapter: JsonAdapter<GarmentsDataModel> = moshi.adapter(GarmentsDataModel::class.java)
        val inputData = adapter.fromJson(inputSource)

        garmentsModel = inputData?.garments
        updateForSortMode()
    }

    private fun updateForSortMode() {
        when (sortMode) {
            SortMode.ALPHA -> {
                sortbyName()
            }
            SortMode.TIME -> {
                sortByTime()
            }
        }
    }

    fun addGarment(garmentName: String) {
        garmentsModel?.add(Garment(
            garmentName,
            System.currentTimeMillis().toString()
        ))
        updateForSortMode()
    }

    fun createGarmentsForPersisting(): String? {
        val moshi: Moshi = Moshi.Builder().add(MoshiArrayListCustomAdapter()).build()
        val adapter: JsonAdapter<GarmentsDataModel> = moshi.adapter(GarmentsDataModel::class.java)
        return adapter.toJson(garmentsModel?.let { GarmentsDataModel(it) })
    }

    fun sortbyName() {
        sortMode = SortMode.ALPHA
        garmentsModel?.let {
            it.sortWith(Comparator { firstGarment, secondGarment ->
                firstGarment.name.compareTo(secondGarment.name)
            })
        }
        responseLiveData.postValue(garmentsModel)
    }

    fun sortByTime() {
        sortMode = SortMode.TIME
        garmentsModel?.let {
            it.sortWith(Comparator { firstGarment, secondGarment ->
                firstGarment.timestamp.compareTo(secondGarment.timestamp)
            })
        }
        responseLiveData.postValue(garmentsModel)
    }

    enum class SortMode {
        ALPHA,
        TIME
    }
}