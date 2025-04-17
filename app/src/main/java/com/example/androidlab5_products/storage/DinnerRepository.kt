package com.example.androidlab5_products.storage

import com.example.androidlab5_products.dao.DinnerDao
import com.example.androidlab5_products.models.DinnerModer
import com.example.androidlab5_products.pojo.Dinner
import kotlinx.coroutines.flow.Flow

class DinnerRepository(private val trainingDao: DinnerDao) {
    fun getAllTrainings(): Flow<List<DinnerModer>> = trainingDao.getAllDinners()
    suspend fun insertTraining(training: DinnerModer) = trainingDao.insert(training)
    suspend fun updateTraining(training: DinnerModer) = trainingDao.update(training)
    suspend fun getTrainingWithExercises(trainingId: Long): Dinner = trainingDao.getDinner(trainingId)
    suspend fun deleteTraining(training: DinnerModer) = trainingDao.delete(training)
}
