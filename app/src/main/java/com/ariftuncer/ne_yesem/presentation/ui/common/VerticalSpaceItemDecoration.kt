package com.ariftuncer.ne_yesem.presentation.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(private val spaceDp: Int) : RecyclerView.ItemDecoration() {
    private fun View.dp(dp: Int) = (dp * context.resources.displayMetrics.density).toInt()
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val s = view.dp(spaceDp)
        outRect.top = s
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) outRect.bottom = s
    }
}
