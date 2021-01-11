package com.reyad.psychology.dashboard.home.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reyad.psychology.dashboard.HomeViewModel
import com.reyad.psychology.databinding.FragmentBatch14Binding

class Batch14 : Fragment() {

    private lateinit var viewModel : HomeViewModel
    private lateinit var recyclerView: RecyclerView

    private lateinit var _binding: FragmentBatch14Binding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatch14Binding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.recyclerViewBatch14
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.b14Data.observe(viewLifecycleOwner, Observer {
            val adapter = StudentAdapter(requireContext(), it)
            recyclerView.adapter = adapter
        })

        return view
    }
}