package com.afurkantitiz.foodapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.afurkantitiz.foodapp.data.ApiRepository
import com.afurkantitiz.foodapp.data.entity.Categories
import com.afurkantitiz.foodapp.data.entity.restaurant.Restaurant
import com.afurkantitiz.foodapp.data.entity.restaurant.RestaurantListResponse
import com.afurkantitiz.foodapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    var savedStateHandle: SavedStateHandle,
    private var apiRepository: ApiRepository
) : ViewModel() {
    var restaurantList: List<Restaurant>? = null

    fun getRestaurants(): LiveData<Resource<RestaurantListResponse>> =
        apiRepository.getRestaurants()

    fun getRestaurantByCuisine(cuisine: String): LiveData<Resource<RestaurantListResponse>> =
        apiRepository.getRestaurantByCuisine(cuisine)

    fun getCategories(): ArrayList<Categories>{
        return arrayListOf(
            Categories("All"),
            Categories("Burger"),
            Categories("Doner"),
            Categories("Worldwide"),
            Categories("Homemade"),
            Categories("Breakfast"),
            Categories("Kebab"),
            Categories("Pizza"),
            Categories("Dessert")
        )
    }
}