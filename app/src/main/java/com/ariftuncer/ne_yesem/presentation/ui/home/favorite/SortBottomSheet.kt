package com.ariftuncer.ne_yesem.presentation.ui.home.favorite.sheet

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.presentation.ui.home.favorite.SortOption
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.radiobutton.MaterialRadioButton

class SortBottomSheet(
    private val preselected: SortOption?,
    private val onApply: (SortOption) -> Unit,
) : BottomSheetDialogFragment() {

    override fun getTheme() = R.style.ThemeOverlay_MyApp_BottomSheet

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.bs_sort, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val container = view.findViewById<LinearLayout>(R.id.containerSort)
        var checkedId = View.NO_ID

        SortOption.entries.forEach { opt ->
            val rb = MaterialRadioButton(requireContext()).apply {
                id = View.generateViewId()
                text = opt.label
                isChecked = (opt == preselected)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = dp(6) }
                tag = opt
            }
            if (rb.isChecked) checkedId = rb.id
            rb.setOnClickListener {
                if (checkedId != View.NO_ID && checkedId != rb.id) {
                    view.findViewById<MaterialRadioButton>(checkedId)?.isChecked = false
                }
                checkedId = rb.id
            }
            container.addView(rb)
        }

        view.findViewById<View>(R.id.btnClearSort).setOnClickListener {
            // Varsayılana dön
            onApply(SortOption.NEWEST); dismiss()
        }
        view.findViewById<View>(R.id.btnApplySort).setOnClickListener {
            val selected = view.findViewById<MaterialRadioButton>(checkedId)?.tag as? SortOption
                ?: SortOption.NEWEST
            onApply(selected); dismiss()
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
