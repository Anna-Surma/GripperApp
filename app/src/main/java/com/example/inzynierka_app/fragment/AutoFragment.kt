package com.example.inzynierka_app.fragment

import android.content.RestrictionEntry.TYPE_INTEGER
import android.content.RestrictionEntry.TYPE_NULL
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.inzynierka_app.Timer
import com.example.inzynierka_app.databinding.FragmentAutoBinding
import com.example.inzynierka_app.model.ParamsRead
import com.example.inzynierka_app.model.ParamsWrite
import com.example.inzynierka_app.viewmodel.GripperViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AutoFragment : Fragment() {

    private var _binding: FragmentAutoBinding? = null
    private val binding get() = _binding!!
    private var timer = Timer()
    private var counter: CountDownTimer? = null

    private lateinit var viewModel: GripperViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAutoBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity()).get(GripperViewModel::class.java)

        binding.etCycles.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                viewModel.cycleOrTimeCheck()
            }
        }
        binding.etTime.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                viewModel.cycleOrTimeCheck()
            }
        }

        binding.gripperViewModel = viewModel
        binding.lifecycleOwner = this

        binding.sStartPoint.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                viewModel.writeDataParallel(selectedItem)
            }
        }

        binding.btnStartButton.setOnClickListener {
            if (viewModel.controlActive.value == true) {
                if (viewModel.autoMode.value == false) {
                    if (viewModel.isPause.value == false) {
                        viewModel.resetCycles()
                        timer.offset = 0
                        binding.chStopWatch.base = timer.setBaseTime()
                        counter = viewModel.startCountDown(false, "")
                        counter?.start()
                    } else {
                        counter =
                            viewModel.startCountDown(true, binding.tvTimeWatch.text.toString())
                        counter?.start()
                    }
                    viewModel.startAuto()
                    viewModel.startReadCycles(ParamsRead("\"Data\".mw_cycles"))

                    binding.chStopWatch.base = timer.setBaseTime()
                    binding.chStopWatch.start()

                    binding.etCycles.inputType = TYPE_NULL
                    binding.etTime.inputType = TYPE_NULL
                }
            }
        }

        binding.btnStopButton.setOnClickListener {
            viewModel.stopAuto()

            binding.chStopWatch.stop()
            viewModel.stopReadCycles()
            counter?.cancel()
            binding.etCycles.inputType = TYPE_INTEGER
            binding.etTime.inputType = TYPE_INTEGER
        }

        binding.btnPauseButton.setOnClickListener {
            if (viewModel.isRunning.value == true && viewModel.isPause.value == false) {
                viewModel.pause()
                timer.offset = timer.getElapsedRealtime() - binding.chStopWatch.base
                binding.chStopWatch.stop()
                counter?.cancel()
            }
        }

        viewModel.controlActive.observe(viewLifecycleOwner) {
            if (it != null) {
                if (viewModel.controlActive.value == true) {
                    viewModel.writeData(ParamsWrite("\"Data\".mb_app_control", true))
                } else {
                    viewModel.writeData(ParamsWrite("\"Data\".mb_app_control", false))
                    viewModel.writeData(ParamsWrite("\"Data\".mb_app_auto", false))
                    binding.etCycles.inputType = TYPE_INTEGER
                    binding.etTime.inputType = TYPE_INTEGER
                }
            }
        }
        viewModel.autoMode.observe(viewLifecycleOwner) {
            if (it != null) {
                if (viewModel.controlActive.value == true) {
                    if (viewModel.autoMode.value == true) {
                        viewModel.writeData(ParamsWrite("\"Data\".mb_app_auto", true))
                    } else
                        viewModel.writeData(ParamsWrite("\"Data\".mb_app_auto", false))
                } else {
                    viewModel.writeData(ParamsWrite("\"Data\".mb_app_auto", false))
                }
            }
        }

        viewModel.readData.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.toString() != binding.tvCyclesNumber.text) {
                    binding.tvCyclesNumber.text = it.toString()
                }
            }
        }

        viewModel.reachSetCycles.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it == true)
                    binding.chStopWatch.stop()
            }
        }
        return view
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopReadCycles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}