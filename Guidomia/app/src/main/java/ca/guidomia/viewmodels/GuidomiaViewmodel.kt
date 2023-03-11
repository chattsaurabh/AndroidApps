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

class GuidomiaViewmodel(application: Application) : AndroidViewModel(application) {

    var guidomiaLiveData = MutableLiveData<List<IRecycleELement>>()
    fun fetchData() {
        viewModelScope.launch {
            var inputSource = readDataFromResource()
            val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, GuidomiaData::class.java)
            val adapter: JsonAdapter<List<GuidomiaData>> = moshi.adapter(listType)

            val inputData = adapter.fromJson(inputSource)
            buildDataForList(inputData)

        }
    }

    private fun buildDataForList(inputData: List<GuidomiaData>?) {
        var listData = mutableListOf<IRecycleELement>()
        inputData?.forEachIndexed { index, carData ->
            listData.add(
                CarInfo(
                    icon = getIconForCar(carData.model),
                    guidomiaCarInfo = carData
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