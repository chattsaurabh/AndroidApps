package ca.guidomia

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObservers() {
        viewmodel.makeFilterLiveData.observe(this, Observer {
            makeSpinnerAdapter.addAll(it)
        })

        viewmodel.modelFilterLiveData.observe(this, Observer {
            modelSpinnerAdapter.addAll(it)
        })

        viewmodel.guidomiaLiveData.observe(this, Observer {
            binding.recyclerView.recycledViewPool.clear()
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun setupList() {
        makeSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item)
        binding.makeSpinner.adapter = makeSpinnerAdapter
        binding.makeSpinner.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                makeSpinnerAdapter.getItem(position)?.let { viewmodel.filterOnMake(it) }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        modelSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item)
        binding.modelSpinner.adapter = modelSpinnerAdapter
        binding.modelSpinner.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                modelSpinnerAdapter.getItem(position)?.let { viewmodel.filterOnModel(it) }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        adapter = GuidomiaListAdapter(this)
        val list = binding.recyclerView
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

    }

}