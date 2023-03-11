package ca.richmond.guidomia.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ca.richmond.guidomia.R
import ca.richmond.guidomia.adapters.CarInfo
import ca.richmond.guidomia.adapters.Divider
import ca.richmond.guidomia.adapters.GuidomiaDataList
import ca.richmond.guidomia.adapters.IRecycleELement
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch

class GuidomiaViewmodel(application: Application) : AndroidViewModel(application) {

    var guidomiaLiveData = MutableLiveData<List<IRecycleELement>> ()
    fun fetchData() {
        viewModelScope.launch {
                var inputSource = readDataFromResource()
                val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val adapter : JsonAdapter<GuidomiaDataList> = moshi.adapter(GuidomiaDataList::class.java)
                val inputData = adapter.fromJson(inputSource)
                buildDataForList(inputData)

            }
    }

    private fun buildDataForList(inputData: GuidomiaDataList?) {
        var listData = mutableListOf<IRecycleELement>()
        inputData?.list?.forEach { carData ->
            listData.add(
                CarInfo(
                    icon = getIconForCar(carData.model),
                    guidomiaCarInfo = carData
                )
            )
            listData.add(
                Divider(
                    isDivider = true
                )
            )
        }
        guidomiaLiveData.postValue(listData)
    }

    private fun getIconForCar(model: String?): Int? {
        return when(model) {
            "Range Rover" -> R.drawable.Range_Rover
            "Roadster" -> R.drawable.Alpine_roadster
            else -> null
        }
    }

    fun readDataFromResource(): String {
        return getApplication<Application>().resources.openRawResource(R.raw.car_list).bufferedReader().use { it.readText() }
    }
}