package com.example.inzynierka_app.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inzynierka_app.R
import com.example.inzynierka_app.adapter.ErrorAdapter
import com.example.inzynierka_app.databinding.FragmentErrorBinding
import com.example.inzynierka_app.ui.viewmodel.GripperViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.view.MenuInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@AndroidEntryPoint
class ErrorFragment : Fragment() {

    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GripperViewModel
    private lateinit var errorAdapter: ErrorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity())[(GripperViewModel::class.java)]

        setupRecyclerView()

        viewModel.errorsSortedByDate.observe(viewLifecycleOwner, Observer {
            errorAdapter.submitList(it)
        })

        setupMenu()
        return view
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                deleteAll()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun deleteAll() {
        val builder = context?.let { it1 -> MaterialAlertDialogBuilder(it1, R.style.MaterialAlertDialog__Delete) }
        with(builder) {
            this!!.setTitle(R.string.delete_history)
            setMessage(R.string.delete_history_desc)
            builder!!.setIcon(android.R.drawable.ic_menu_delete)
            setPositiveButton("Delete") { _: DialogInterface, _ -> viewModel.deleteErrors() }
            setNegativeButton("Cancel") { dialog: DialogInterface, _ -> dialog.cancel() }
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() = binding.rvRuns.apply {
        errorAdapter = ErrorAdapter()
        adapter = errorAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}