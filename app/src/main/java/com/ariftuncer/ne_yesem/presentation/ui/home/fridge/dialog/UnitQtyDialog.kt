package com.ariftuncer.ne_yesem.presentation.ui.home.fridge.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.DialogUnitQtyBinding
import android.widget.ArrayAdapter

class UnitQtyDialog(
    private val unitOptions: List<String>,
    private val defaultUnit: String? = null,
    private val defaultQty: Int = 1,
    private val onDone: (String, Int) -> Unit
) : DialogFragment() {

    private var _binding: DialogUnitQtyBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), com.google.android.material.R.style.Theme_MaterialComponents_Dialog_Alert)
        _binding = DialogUnitQtyBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        val unitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, unitOptions)
        binding.unitEditTxt.setAdapter(unitAdapter)

        defaultUnit?.let { binding.unitEditTxt.setText(it, false) }
        binding.amountEditTxt.setText(defaultQty.toString())

        binding.addPantryButton.setOnClickListener {
            val unitLabel = binding.unitEditTxt.text?.toString()?.ifBlank { defaultUnit ?: unitOptions.first() } ?: unitOptions.first()
            val qty = binding.amountEditTxt.text?.toString()?.toDoubleOrNull()?.toInt() ?: defaultQty
            onDone(unitLabel, qty.coerceAtLeast(1))
            dismiss()
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
