package ca.richmond.guidomia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ca.richmond.guidomia.adapters.GuidomiaData
import ca.richmond.guidomia.adapters.GuidomiaListAdapter
import ca.richmond.guidomia.databinding.ActivityMainBinding
import ca.richmond.guidomia.viewmodels.GuidomiaViewmodel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var adapter = GuidomiaListAdapter(this)
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
        viewmodel.guidomiaLiveData.observe(this, Observer {
            adapter.setData(it)
            adapter.notifyItemMoved(0, it.size)
        })
    }

    private fun setupList() {
        val list = binding.recyclerView
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

    }

}