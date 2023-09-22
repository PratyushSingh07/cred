package com.assignment.cred.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.assignment.cred.R
import com.assignment.cred.databinding.FragmentHomeBinding
import com.assignment.cred.models.ChildItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var bundle: ChildItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (arguments != null) {
            bundle = requireArguments().getParcelable<ChildItem>("CHILD_ITEM")
            binding.ivCategory.setImageResource(R.drawable.mint_placeholder)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCategory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }

        binding.tvCategory.text = bundle?.title ?: "Placeholder Title"
        binding.tvCategorySubtitle.text = bundle?.subtitle ?: "Placeholder Subtitle Title"
    }
}