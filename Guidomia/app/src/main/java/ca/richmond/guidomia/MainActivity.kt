package ca.richmond.guidomia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.richmond.guidomia.adapters.GuidomiaListAdapter
import ca.richmond.guidomia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupList()
    }

    private fun setupList() {
        var list = binding.recyclerView
        list.layoutManager = LinearLayoutManager(this)
        list.adapter =
            GuidomiaListAdapter()
    }
}