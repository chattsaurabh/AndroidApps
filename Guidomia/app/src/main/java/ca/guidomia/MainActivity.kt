package ca.guidomia

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ca.guidomia.adapters.GuidomiaListAdapter
import ca.guidomia.databinding.ActivityMainBinding
import ca.guidomia.viewmodels.GuidomiaViewmodel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : GuidomiaListAdapter
    private lateinit var makeSpinnerAdapter: ArrayAdapter<String>
    private lateinit var modelSpinnerAdapter: ArrayAdapter<String>
    private val viewmodel: GuidomiaViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setupList()
        viewmodel.fetchData()
    }

    private fun setupObservers() {
        viewmodel.makeFilterLiveData.observe(this, Observer {
            makeSpinnerAdapter.addAll(it)
        })

        viewmodel.modelFilterLiveData.observe(this, Observer {
            modelSpinnerAdapter.addAll(it)
        })

        viewmodel.guidomiaLiveData.observe(this, Observer {
            adapter.setData(it)
            adapter.notifyItemRangeChanged(0, it.size)
        })
    }

    private fun setupList() {
        makeSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item)
        binding.makeSpinner.adapter = makeSpinnerAdapter

        modelSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item)
        binding.modelSpinner.adapter = modelSpinnerAdapter

        adapter = GuidomiaListAdapter(this)
        val list = binding.recyclerView
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

    }

}