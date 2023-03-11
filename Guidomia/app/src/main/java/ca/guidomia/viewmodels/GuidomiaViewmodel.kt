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

    private var data: List<GuidomiaData>? = null
    private var filteredData: List<GuidomiaData>? = null

    private var currentFilteredMake: String = ANY_MAKE
    private var  currentFilteredModel: String = ANY_MODEL

    fun fetchData() {
        viewModelScope.launch {
            var inputSource = readDataFromResource()
            val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, GuidomiaData::class.java)
            val adapter: JsonAdapter<List<GuidomiaData>> = moshi.adapter(listType)

            data = adapter.fromJson(inputSource)
            buildFilterData()
            buildDataForList(data, 0)

        }
    }

    private fun buildFilterData() {
        var makeFilter = ArrayList<String>().apply {
            add(ANY_MAKE)
        }
        var modelFilter = ArrayList<String>().apply {
            add(ANY_MODEL)
        }

        data?.forEach { guidomiaData ->
            guidomiaData.make?.let { it -> makeFilter.add(it) }
            guidomiaData.model?.let { it -> modelFilter.add(it) }
        }
        makeFilterLiveData.postValue(makeFilter)
        modelFilterLiveData.postValue(modelFilter)
    }

    fun filterOnMake(make: String) {
        val subData = ArrayList<GuidomiaData>()
        if (ANY_MAKE == make) {
            buildDataForList(data, 0)
        } else {
            data?.forEach {
                if (make == it.make) {
                    subData.add(it)
                }
            }
            buildDataForList(subData, 0)
        }
    }

    fun filterOnModel(model: String) {
        val subData = ArrayList<GuidomiaData>()
        if (ANY_MODEL == model) {
            buildDataForList(data, 0)
        } else {
            data?.forEach {
                if (model == it.model) {
                    subData.add(it)
                }
            }
            buildDataForList(subData, 0)
        }
    }

    private fun buildDataForList(inputData: List<GuidomiaData>?, indexExpanded: Int) {
        var listData = ArrayList<IRecycleELement>()
        inputData?.forEachIndexed { index, carData ->
            listData.add(
                CarInfo(
                    icon = getIconForCar(carData.model),
                    guidomiaCarInfo = carData,
                    isExpanded = index == indexExpanded,
                    clickListener = {
                        buildDataForList(data, index)
                    }
                )
            )
            if(index < inputData.lastIndex) {
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

    fun readDataFromResource(): String {
        return getApplication<Application>().resources.openRawResource(R.raw.car_list)
            .bufferedReader().use { it.readText() }
    }
}