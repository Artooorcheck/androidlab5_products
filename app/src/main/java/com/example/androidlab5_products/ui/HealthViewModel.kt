package com.example.androidlab5_products.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidlab5_products.models.Product
import com.example.androidlab5_products.models.DinnerModer
import com.example.androidlab5_products.network.RetrofitInstance
import com.example.androidlab5_products.pojo.Dinner
import com.example.androidlab5_products.storage.ProductRepository
import com.example.androidlab5_products.storage.DinnerRepository
import kotlinx.coroutines.launch
import java.util.Date

class HealthViewModel(private val dinnerRepository: DinnerRepository, private val productRepository: ProductRepository) : ViewModel() {
    val workouts: LiveData<List<DinnerModer>> = dinnerRepository.getAllTrainings().asLiveData()

    private val _dinner = MutableLiveData(Dinner( DinnerModer(-1, Date()), mutableListOf()))
    val dinner: LiveData<Dinner> get() = _dinner

    fun modTraining(training: Dinner) {
        viewModelScope.launch {
            if(training.dinner.id.toInt() <= 0) {
                training.dinner.id = 0
                training.dinner.id = dinnerRepository.insertTraining(training.dinner)
            } else {
                dinnerRepository.updateTraining(training.dinner)
            }

            mergeExercises(training)
        }
    }

    fun selectDinner(id: Long) {
        if(id <= 0)
            _dinner.value = Dinner( DinnerModer(-1, Date()), mutableListOf())
        else
            fetchTrainingWithExercises(id){ workout ->
                _dinner.value = workout
            }
    }

    fun fetchTrainingWithExercises(trainingId: Long, callback: (Dinner) -> Unit) {
        viewModelScope.launch {
            val result = dinnerRepository.getTrainingWithExercises(trainingId)
            callback(result)
        }
    }

    fun deleteWorkout(trainingId: Long){
        viewModelScope.launch {
            dinnerRepository.deleteTraining(DinnerModer(trainingId, Date()))
        }
    }

    private fun mergeExercises(dinner: Dinner){
        val ids = dinner.products.map { a -> a.id }
        fetchTrainingWithExercises(dinner.dinner.id) { oldWorkout ->
            oldWorkout.products.forEach { oldEx ->
                if (!ids.contains(oldEx.id)) {
                    viewModelScope.launch {
                        productRepository.deleteExercise(oldEx)
                    }
                }
            }
            dinner.products.forEach{ newEx ->
                viewModelScope.launch {
                    if(newEx.id > 0) {
                        productRepository.updateExercise(newEx)
                    } else {
                        newEx.id = 0
                        newEx.dinnerId = dinner.dinner.id
                        productRepository.insertExercise(newEx)
                    }
                }
            }
        }
    }

    fun fetchProduct(barcode: String, action: (Product?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getProduct(barcode)
                if (response.isSuccessful && response.body()?.status == 1) {
                    val product = response.body()?.product
                    action?.invoke(product)
                    return@launch
                } else {
                    Log.e("OpenFoodFacts", "Продукт не найден")
                }
            } catch (e: Exception) {
                Log.e("OpenFoodFacts", "Ошибка: ${e.localizedMessage}")
            }

            action?.invoke(null)
        }
    }

}
