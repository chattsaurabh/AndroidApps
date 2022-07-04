package com.somethingsimple.garments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.somethingsimple.garments.databinding.FragmentSecondBinding
import com.somethingsimple.garments.viewmodels.GarmentsViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GarmentsAddFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    val viewmodel by activityViewModels<GarmentsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.findViewById<MaterialToolbar>(R.id.toolbar)?.findViewById<ImageButton>(R.id.toolbar_add)?.visibility = View.GONE
        val save = activity?.findViewById<MaterialToolbar>(R.id.toolbar)?.findViewById<Button>(R.id.toolbar_save)
        save?.visibility = View.VISIBLE
        save?.setOnClickListener {
            viewmodel.addGarment(binding.addGarmentName.text.toString())
            findNavController().navigateUp()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}