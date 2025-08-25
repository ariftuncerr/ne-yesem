package com.ariftuncer.ne_yesem.presentation.ui.home.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentFavoriteBinding
import com.ariftuncer.ne_yesem.presentation.viewmodel.FavoritesViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _b: FragmentFavoriteBinding? = null
    private val b get() = _b!!

    private val vm: FavoritesViewModel by viewModels()
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentFavoriteBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpComponents()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        adapter = FavoritesAdapter(
            onClick = { card -> }, // detay sayfasına git
            onHeart = { card -> // favoriler ekranından kaldır
                vm.toggle(uid, card.id, nowFavorite = false)
                // UI’dan hemen kaldır
                val newList = currentList().filterNot { it.id == card.id }
                adapter.submitList(newList)
            }
        )

        b.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        b.rvFavorites.adapter = adapter

        vm.cards.observe(viewLifecycleOwner) { list ->
            b.noRecipeLayout.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            b.rvFavorites.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
            adapter.submitList(list)
        }

        vm.refresh(uid)
    }

    private fun currentList() = adapter.currentList.toList()

    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Favoriler"
        toolbar.setTitleTextColor(R.color.primary950)
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
