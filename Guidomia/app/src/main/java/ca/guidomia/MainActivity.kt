package ca.guidomia

import android.os.Bundle
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
            adapter.notifyItemRangeChanged(0, it.size)
        })
    }

    private fun setupList() {
        adapter = GuidomiaListAdapter(this)
        val list = binding.recyclerView
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

    }

}