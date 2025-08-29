package com.ariftuncer.ne_yesem.presentation.ui.home.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.data.remote.firestore.UserRemoteDataSource
import com.ariftuncer.ne_yesem.databinding.FragmentRecipeDetailBinding
import com.ariftuncer.ne_yesem.presentation.viewmodel.FavoritesViewModel
import com.ariftuncer.ne_yesem.presentation.viewmodel.recipe.RecipeDetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {
    @Inject lateinit var userRemoteDataSource: UserRemoteDataSource
    private val favVm: FavoritesViewModel by viewModels()
    private var currentId: Int = -1
    private lateinit var binding: FragmentRecipeDetailBinding
    private val vm: RecipeDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        setUpComponents()
        setUpDetails()
        return binding.root

    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val bottomBar = requireActivity().findViewById<View>(R.id.bottomNavigationView)
        val recipeBtn = requireActivity().findViewById<View>(R.id.createRecipeFabBtn)
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = ""
        toolbar.isTitleCentered = false
        toolbar.subtitle = ""
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.back_24)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack()
            bottomBar.visibility = View.GONE
            recipeBtn.visibility = View.GONE
            toolbar.navigationIcon = null

        }
        bottomBar.visibility = View.GONE
        recipeBtn.visibility = View.GONE

    }

    private fun setUpDetails() {
        val id = requireArguments().getInt("recipeId")
        currentId = id
        setupFavoriteHeart(id)

        vm.load(id)
        println("Recipe ID: $id")

        // Kullanıcıya ait son görüntülenenler koleksiyonuna ekle
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                userRemoteDataSource.addLastView(uid, id)
            }
        }

        vm.detail.observe(viewLifecycleOwner) { d ->

            println("Recipe detail: $d")
            if (d == null) { /* error state göster */ return@observe }
            /*binding.preaparingView.visibility = View.GONE

            (binding.root as ViewGroup).findViewById<NestedScrollView>(R.id.nestedScrollView)?.visibility = View.VISIBLE*/
            Glide.with(this).load(d.image).into(binding.recipeImg)
            binding.recipeTt.text = d.title
            binding.recipeDetailTxt.text = d.summaryPlain
            binding.timeTxt.text = d.readyInMinutes?.let { "$it dk" } ?: "-"
            binding.personsTxt.text = d.servings?.let { "$it kişilik" } ?: "-"
            binding.coloriesTxt.text = d.caloriesText ?: "-"

            val ingredients = ArrayList(d.ingredients)
            val steps = ArrayList(d.steps)

            binding.ingOrPreparationViewPager.adapter =
                IngredientsStepsPagerAdapter(this, ingredients, steps)
            binding.ingOrPreparationViewPager.offscreenPageLimit = 2

            com.google.android.material.tabs.TabLayoutMediator(
                binding.ingOrPreparationTab,
                binding.ingOrPreparationViewPager
            ) { tab, pos ->
                tab.text = if (pos == 0) "Malzemeler" else "Yapılışı"
            }.attach()
        }


    }
    private fun renderHeart(isFav: Boolean) {
        binding.recipeBtnLike.setImageResource(
            if (isFav) R.drawable.baseline_favorite_24 else R.drawable.no_fav_24
        )
    }
    private fun setupFavoriteHeart(recipeId: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // İlk açılışta favori ID'lerini çek (ekranlar arası paylaşılan VM ise bir kez çeker)
        if (favVm.ids.value.isNullOrEmpty()) favVm.refresh(uid)

        // Başlangıç görünümü
        var isFav = favVm.ids.value?.contains(recipeId) == true
        renderHeart(isFav)

        // Kalp tıklandığında → optimistik toggle + Firestore yaz/sil
        binding.recipeBtnLike.setOnClickListener {
            isFav = !isFav
            renderHeart(isFav)
            favVm.toggle(uid, recipeId, nowFavorite = isFav)
        }

        // Dışarıdan (başka ekrandan) değişirse, kalbi senkron tut
        favVm.ids.observe(viewLifecycleOwner) { ids ->
            renderHeart(ids.contains(recipeId))
        }
    }



}