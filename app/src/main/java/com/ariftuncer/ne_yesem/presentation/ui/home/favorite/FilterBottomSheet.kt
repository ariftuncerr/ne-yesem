package com.ariftuncer.ne_yesem.presentation.ui.home.favorite.sheet

import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.presentation.ui.home.favorite.FilterOption
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.checkbox.MaterialCheckBox

class FilterBottomSheet(
    private val preselected: Set<FilterOption>,
    private val onApply: (Set<FilterOption>) -> Unit,
) : BottomSheetDialogFragment() {

    private val boxes = mutableMapOf<FilterOption, MaterialCheckBox>()

    override fun getTheme() = R.style.ThemeOverlay_MyApp_BottomSheet
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.bs_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val grid = view.findViewById<GridLayout>(R.id.gridFilters)

        FilterOption.entries.forEach { fo ->
            val cb = MaterialCheckBox(requireContext()).apply {
                text = fo.label
                isChecked = preselected.contains(fo)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0; height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(dp(4), dp(6), dp(4), dp(6))
                }
            }
            boxes[fo] = cb
            grid.addView(cb)
        }

        view.findViewById<View>(R.id.btnClearFilters).setOnClickListener {
            boxes.values.forEach { it.isChecked = false }
        }
        view.findViewById<View>(R.id.btnApplyFilters).setOnClickListener {
            onApply(boxes.filter { it.value.isChecked }.keys); dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        (dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet) as? View)?.let {
            it.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_bottomsheet)
            BottomSheetBehavior.from(it).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
    }
}
private fun Fragment.dp(v: Int) = (v * resources.displayMetrics.density).toInt()
