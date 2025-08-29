package com.ariftuncer.ne_yesem.presentation.ui.home.recipe

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.presentation.ui.common.VerticalSpaceItemDecoration

class StepsPageFragment : Fragment() {
    private lateinit var rv: RecyclerView
    private val adapter by lazy { StepsAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rv = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@StepsPageFragment.adapter
            setHasFixedSize(true)
            addItemDecoration(VerticalSpaceItemDecoration(8))
        }
        return rv
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val items = requireArguments().getStringArrayList(ARG_ITEMS) ?: arrayListOf()
        adapter.submitList(items)
    }

    companion object {
        private const val ARG_ITEMS = "items"
        fun newInstance(items: ArrayList<String>) = StepsPageFragment().apply {
            arguments = bundleOf(ARG_ITEMS to items)
        }
    }
}
