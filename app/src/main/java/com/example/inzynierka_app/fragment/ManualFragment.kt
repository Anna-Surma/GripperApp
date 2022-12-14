package com.example.inzynierka_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.inzynierka_app.databinding.FragmentManualBinding
import com.example.inzynierka_app.model.ParamsWrite
import com.example.inzynierka_app.viewmodel.GripperViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualFragment : Fragment() {

    private var _binding: FragmentManualBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GripperViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentManualBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity()).get(GripperViewModel::class.java)

        binding.gripperViewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnStep.setOnClickListener {
            viewModel.startStep()
        }
        viewModel.manualMode.observe(viewLifecycleOwner) {
            if (it != null) {
                if (viewModel.controlActive.value == true) {
                    if (viewModel.manualMode.value == true) {
                        viewModel.writeData2(ParamsWrite("\"DB100\".mb_app_step", true))
                        viewModel.stopStep()
                    } else
                        viewModel.writeData2(ParamsWrite("\"DB100\".mb_app_step", false))
                } else
                    viewModel.writeData2(ParamsWrite("\"DB100\".mb_app_step", false))
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}