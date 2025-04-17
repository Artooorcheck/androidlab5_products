package com.example.androidlab5_products.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.runtime.toMutableStateMap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlab5_products.R
import com.example.androidlab5_products.adapters.Identifier
import com.example.androidlab5_products.adapters.IdentifierAdapter
import com.example.androidlab5_products.models.ProductModel
import com.example.androidlab5_products.models.Product
import com.example.androidlab5_products.models.DinnerModer
import com.example.androidlab5_products.pojo.Dinner
import com.example.androidlab5_products.ui.HealthViewModel
import java.util.Date
import kotlin.math.min


class WorkoutFragmentView(private val viewModel: HealthViewModel, private val context: Context) : Fragment() {
    private lateinit var etDate: EditText
    private lateinit var sumKcal: TextView

    private lateinit var exerciseFragment: ExerciseFragment
    private lateinit var cameraFragment: CameraFragment

    private var workout = DinnerModer(0,  Date())
    private var exerciseMap: MutableMap<Long, ProductModel> = mutableMapOf()

    private var onCloseAction: (() -> Unit)? = null

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_view, container, false)
        etDate = view.findViewById(R.id.date_filter)
        sumKcal = view.findViewById(R.id.sum_kcal)
        val scanButton = view.findViewById<Button>(R.id.scan_product_button)
        val addButton = view.findViewById<Button>(R.id.add_product_button)
        val exerciseContainer = view.findViewById<RecyclerView>(R.id.product_list)
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val closeButton = view.findViewById<Button>(R.id.close_button)

        exerciseFragment = ExerciseFragment()
        cameraFragment = CameraFragment(viewModel)
        val adapter = IdentifierAdapter(requireActivity())

        exerciseFragment.setOnSaveExercise { exercise ->
            exerciseMap[exercise.id] = exercise
            updateAdapterList(adapter)
        }

        cameraFragment.setOnSuccessScan { res ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.view_popup, exerciseFragment)
                .addToBackStack(null)
                .commit()
            exerciseFragment.showPopUp(newExerciseModel(res))
        }

        addButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.view_popup, exerciseFragment)
                .addToBackStack(null)
                .commit()
            exerciseFragment.showPopUp(newExerciseModel(null))
        }

        val action: (Dinner) -> Unit = ({ workout ->
            this.workout = workout.dinner
            this.exerciseMap = workout.products.map { it.id to it }.toMutableStateMap()
            updateAdapterList(adapter)
        })

        viewModel.dinner.observe(requireActivity(), action)

        adapter.setOnDeleteItemListener { id ->
            exerciseMap.remove(id)
            updateAdapterList(adapter)
        }

        adapter.setOnClickItemListener { id ->
            if (exerciseMap.containsKey(id))
                exerciseMap[id]?.let {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.view_popup, exerciseFragment)
                        .addToBackStack(null)
                        .commit()
                    exerciseFragment.showPopUp(it)
                }
        }

        exerciseContainer.layoutManager = LinearLayoutManager(context)
        exerciseContainer.adapter = adapter

        scanButton.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.view_popup, cameraFragment)
                .addToBackStack(null)
                .commit()
            cameraFragment.startCamera(requireActivity())
        }

        saveButton.setOnClickListener{
            addWorkout()
            viewModel.dinner.removeObserver(action)
            onCloseAction?.invoke()
        }

        closeButton.setOnClickListener{
            viewModel.dinner.removeObserver(action)
            onCloseAction?.invoke()
        }

        return view
    }

    fun updateModel(dinner: Dinner) {

    }

    private fun addWorkout() {
        viewModel.modTraining(Dinner(workout, exerciseMap.values.toList()))
    }

    private fun updateFields() {
        var kcalAmount = 0L
        etDate.setText(workout.date.toString())
        if(exerciseMap.count() > 0) {
            kcalAmount = exerciseMap.values.sumOf { item ->
                item.kcal * item.weight / 100
            }
        }
        try {
            if (kcalAmount > 2500)
                sumKcal.setTextColor(context.getColor(R.color.red))
            else
                sumKcal.setTextColor(context.getColor(R.color.green))
        } catch (ex: Exception) {}
        sumKcal.setText(kcalAmount.toString() + " kcal")
    }

    private fun updateAdapterList(adapter: IdentifierAdapter){
        adapter.updateList(exerciseMap.map { a -> Identifier(a.value.id, a.value.name) })
        updateFields()
    }

    private fun newExerciseModel(product: Product?): ProductModel {
        val id = if (exerciseMap.count() > 0) min(exerciseMap.minOf { a -> a.key }, 0) - 1 else -1
        return ProductModel(id,0, product?.product_name ?: "",(product?.nutriments?.energyKcalPer100g ?: 0).toLong(), 100, Date())
    }

    fun setOnCloseListener(action: () -> Unit) {
        onCloseAction = action
    }
}