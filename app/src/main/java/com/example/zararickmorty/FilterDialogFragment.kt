package com.example.zararickmorty

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.zararickmorty.databinding.DialogFilterBinding
import com.google.android.material.chip.Chip

class FilterDialogFragment : DialogFragment() {

    interface FilterDialogListener {
        fun onFilterSelected(status: String, species: String, gender: String)
    }

    var listener: FilterDialogListener? = null
    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!
    private var filters: MutableMap<String, String>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            _binding = DialogFilterBinding.inflate(requireActivity().layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton("Filter") { _, _ ->
                    val status = binding.statusChipGroup.findViewById<Chip>(binding.statusChipGroup.checkedChipId)?.text.toString()
                    val species = binding.speciesChipGroup.findViewById<Chip>(binding.speciesChipGroup.checkedChipId)?.text.toString()
                    val gender = binding.genderChipGroup.findViewById<Chip>(binding.genderChipGroup.checkedChipId)?.text.toString()

                    listener?.onFilterSelected(status, species, gender)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setChipStates(filters: MutableMap<String, String>) {
        val filters = this.filters ?: return

        val status = filters["status"]
        val species = filters["species"]
        val gender = filters["gender"]

        if (status != null) {
            val chip = binding.statusChipGroup.findViewWithTag<Chip>(status)
            chip?.isChecked = true
        }

        if (species != null) {
            val chip = binding.speciesChipGroup.findViewWithTag<Chip>(species)
            chip?.isChecked = true
        }

        if (gender != null) {
            val chip = binding.genderChipGroup.findViewWithTag<Chip>(gender)
            chip?.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
