package com.somethingsimple.healthall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.somethingsimple.healthall.viewmodels.HomeViewmodel
import android.R.attr.country
import android.widget.ArrayAdapter
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : AppCompatActivity() {

    val viewmodel : HomeViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initObservers()
        initData()
    }

    private fun initObservers() {
        viewmodel.sourceColorsLiveData.observe(this, Observer {
            it?.let {
                val colorsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
                colorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                fromColorSpinner.adapter = colorsAdapter
                fromColorSpinner.onItemSelectedListener = viewmodel.createOnColorSelectedListener()
            }
        })

        viewmodel.sourceShapesLiveData.observe(this, Observer {
            it?.let {
                val shapesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
                shapesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                fromShapeSpinner.adapter = shapesAdapter
                fromShapeSpinner.onItemSelectedListener = viewmodel.createOnShapeSelectedListener()
            }
        })

        viewmodel.responseLiveData.observe(this, Observer {
            it?.let {
                resultTextView.text = it
            }
        })
    }

    private fun initData() {
        val inputSource = resources.openRawResource(R.raw.datasource)
            .bufferedReader().use { it.readText() }
        viewmodel.fetchData(inputSource)
    }
}
