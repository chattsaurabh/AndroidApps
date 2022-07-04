package com.somethingsimple.garments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.somethingsimple.garments.adapters.GarmentsListAdapter
import com.somethingsimple.garments.databinding.FragmentFirstBinding
import com.somethingsimple.garments.models.Garment
import com.somethingsimple.garments.utils.GarmentsSharedPreferenceManager
import com.somethingsimple.garments.viewmodels.GarmentsViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GarmentsListFragment : Fragment() {

    private val PERSISTED_GARMENTS_KEY = "persisted_garments"
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    val viewmodel by activityViewModels<GarmentsViewModel>()
    private var listView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        listView = binding.userItemsList
        listView?.layoutManager = LinearLayoutManager(requireContext())
        viewmodel.responseLiveData.observe(viewLifecycleOwner , Observer {
            listView?.adapter = buildListAdapter(it)
            listView?.adapter?.notifyDataSetChanged()
        })
        binding.btnAlphabetical.setOnClickListener {
            viewmodel.sortbyName()
        }
        binding.btnCreationTime.setOnClickListener {
            viewmodel.sortByTime()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val add = activity?.findViewById<MaterialToolbar>(R.id.toolbar)?.findViewById<ImageButton>(R.id.toolbar_add)
        add?.visibility = View.VISIBLE
        add?.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        activity?.findViewById<MaterialToolbar>(R.id.toolbar)?.findViewById<Button>(R.id.toolbar_save)?.visibility = View.GONE
    }

    private fun buildListAdapter(garments: ArrayList<Garment>): GarmentsListAdapter {
        return GarmentsListAdapter(
            ArrayList(garments)
        )
    }

    private fun initData() {
        var garments = GarmentsSharedPreferenceManager(requireActivity()).fetchPersistedGarments(PERSISTED_GARMENTS_KEY)
        if(garments.isNullOrBlank()) {
            garments = resources.openRawResource(R.raw.datasource)
                .bufferedReader().use { it.readText() }
        }
        viewmodel.fetchData(garments)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewmodel.createGarmentsForPersisting()?.let {
            GarmentsSharedPreferenceManager(requireActivity()).persistsGarmentsList(PERSISTED_GARMENTS_KEY,
                it
            )
        }
        _binding = null
    }
}