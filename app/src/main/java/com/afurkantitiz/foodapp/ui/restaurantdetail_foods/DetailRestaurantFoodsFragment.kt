package com.afurkantitiz.foodapp.ui.restaurantdetail_foods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.afurkantitiz.foodapp.data.entity.food.Meal
import com.afurkantitiz.foodapp.databinding.FragmentDetailRestaurantBinding
import com.afurkantitiz.foodapp.utils.Resource
import com.afurkantitiz.foodapp.utils.gone
import com.afurkantitiz.foodapp.utils.show
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import androidx.fragment.app.DialogFragment
import com.afurkantitiz.foodapp.R
import com.afurkantitiz.foodapp.ui.home.RestaurantAddFragment

@AndroidEntryPoint
class DetailRestaurantFoodsFragment : Fragment() {
    private var _binding: FragmentDetailRestaurantBinding? = null
    private val binding get() = _binding!!

    private val foodsViewModel: DetailRestaurantFoodsViewModel by viewModels()
    private val args: DetailRestaurantFoodsFragmentArgs by navArgs()

    private var adapter: DetailRestaurantFoodsAdapter = DetailRestaurantFoodsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mealsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        getRestaurantDetail()
        onClick()
    }

    private fun onClick() {
        binding.backButton.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.addFoodButton.setOnClickListener {
            val foodAddFragment = FoodAddFragment(restaurantId = args.restaurantId)
            foodAddFragment.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.ThemeOverlay_Demo_BottomSheetDialog)
            foodAddFragment.show(requireActivity().supportFragmentManager, "RestaurantAddBottomSheet")
        }
    }

    private fun getRestaurantDetail() {
        foodsViewModel.getRestaurantDetail(args.restaurantId).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.show()
                    binding.collapseLayout.gone()
                }

                Resource.Status.SUCCESS -> {
                    binding.progressBar.gone()
                    binding.collapseLayout.show()

                    val restaurant = it.data!!.data
                    setMealsAdapter(restaurant.meals, restaurant.id, restaurant.name)

                    Glide.with(binding.restaurantImageView.context)
                        .load(restaurant.image)
                        .into(binding.restaurantImageView)

                    binding.restaurantNameTextView.text = restaurant.name
                    binding.deliveryFee.text = restaurant.minimumDeliveryFee
                    binding.deliveryInfo.text = restaurant.deliveryInfo
                    binding.payments.text = restaurant.paymentMethods
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.show()
                    binding.collapseLayout.gone()
                }
            }
        })
    }


    private fun setMealsAdapter(mealList: List<Meal>, restaurantId: String, restaurantName: String) {
        adapter.setMealList(mealList, restaurantId, restaurantName)
        binding.mealsRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}