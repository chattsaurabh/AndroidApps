package ca.guidomia.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ca.guidomia.R
import ca.guidomia.adapters.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch

const val ANY_MAKE = "Any make"
const val ANY_MODEL = "Any model"

class GuidomiaViewmodel(application: Application) : AndroidViewModel(application) {

    var guidomiaLiveData = MutableLiveData<ArrayList<IRecycleELement>>()
        private set

    var makeFilterLiveData = MutableLiveData<ArrayList<String>>()
        private set

    var modelFilterLiveData = MutableLiveData<ArrayList<String>>()
        private set

    private var data = ArrayList<GuidomiaData>()

    private var currentFilteredMake: String = ANY_MAKE
    private var currentFilteredModel: String = ANY_MODEL

    fun fetchData() {
        viewModelScope.launch {
            val inputSource = readDataFromResource()
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(MoshiArrayListCustomAdapter())
                .build()
            val listType =
                Types.newParameterizedType(ArrayList::class.java, GuidomiaData::class.java)
            val adapter: JsonAdapter<List<GuidomiaData>> = moshi.adapter(listType)

            val inputData = adapter.fromJson(inputSource)
            inputData?.let { data.addAll(it) }
            buildFilterData()
            buildDataForList(data, 0)

        }
    }

    private fun buildFilterData() {
        val makeFilter = ArrayList<String>().apply {
            add(ANY_MAKE)
        }
        val modelFilter = ArrayList<String>().apply {
            add(ANY_MODEL)
        }

        data.forEach { guidomiaData ->
            guidomiaData.make?.let { it -> makeFilter.add(it) }
            guidomiaData.model?.let { it -> modelFilter.add(it) }
        }
        makeFilterLiveData.postValue(makeFilter)
        modelFilterLiveData.postValue(modelFilter)
    }

    fun filter(make: String, model: String) {
        var subData = ArrayList<GuidomiaData>()
        if (make == ANY_MAKE && model == ANY_MODEL) {
            subData = data.clone() as ArrayList<GuidomiaData>
        } else if (make == ANY_MAKE) {
            data.forEach {
                if (model == it.model) {
                    subData.add(it)
                }
            }
        } else if (model == ANY_MODEL) {
            data.forEach {
                if (make == it.make) {
                    subData.add(it)
                }
            }
        } else {
            data.forEach {
                if (it.make == make && it.model == model) {
                    subData.add(it)
                }
            }
        }
        buildDataForList(subData, 0)
    }

    private fun buildDataForList(inputData: List<GuidomiaData>?, indexExpanded: Int) {
        val listData = ArrayList<IRecycleELement>()
        inputData?.forEachIndexed { index, carData ->
            listData.add(
                CarInfo(
                    icon = getIconForCar(carData.model),
                    guidomiaCarInfo = carData,
                    isExpanded = index == indexExpanded,
                    clickListener = {
                        buildDataForList(inputData, index)
                    }
                )
            )
            if (index < inputData.lastIndex) {
                listData.add(
                    Divider(
                        isDivider = true
                    )
                )
            }
        }
        guidomiaLiveData.postValue(listData)
    }

    private fun getIconForCar(model: String?): Int? {
        return when (model) {
            "Range Rover" -> R.drawable.range_rover
            "Roadster" -> R.drawable.alpine_roadster
            "3300i" -> R.drawable.bmw_330i
            "GLE coupe" -> R.drawable.mercedez_benz_glc
            else -> null
        }
    }

    private fun readDataFromResource(): String {
        return getApplication<Application>().resources.openRawResource(R.raw.car_list)
            .bufferedReader().use { it.readText() }
    }
}