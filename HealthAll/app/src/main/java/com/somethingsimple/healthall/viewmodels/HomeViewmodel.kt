package com.somethingsimple.healthall.viewmodels

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.somethingsimple.healthall.models.Color
import com.somethingsimple.healthall.models.HomeModel
import com.somethingsimple.healthall.models.Shape
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class HomeViewmodel : ViewModel() {

    var sourceColorsLiveData = MutableLiveData<List<Color>?>()
        private set
    var sourceShapesLiveData = MutableLiveData<List<Shape>?>()
        private set

    var responseLiveData = MutableLiveData<String>()
        private set

    private var selectedSourceColor: Color? = null
    private var selectedSourceShape: Shape? = null
    private var homeModel: HomeModel? = null

    fun fetchData(inputSource: String) {
        val moshi: Moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<HomeModel> = moshi.adapter(HomeModel::class.java)
        val inputData = adapter.fromJson(inputSource)
        homeModel = inputData
        sourceColorsLiveData.postValue(inputData?.sourceColors)
        sourceShapesLiveData.postValue(inputData?.sourceShapes)
    }

    fun createOnColorSelectedListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSourceColor = sourceColorsLiveData.value?.get(position)
                evaluateResults()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun createOnShapeSelectedListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSourceShape = sourceShapesLiveData.value?.get(position)
                evaluateResults()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun evaluateResults() {
        if (selectedSourceColor == null) return

        if (selectedSourceShape == null) return

        homeModel?.mappings?.forEach {
            if (it.sourceColor == selectedSourceColor?.color
                && it.sourceShape == selectedSourceShape?.shape) {
                responseLiveData.postValue(
                    "Result Color = ${it.destinationColor} :: Result Shape = ${it.destinationShape}"
                )
            }
        }
    }
}