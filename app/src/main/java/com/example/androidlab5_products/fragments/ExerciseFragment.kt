package com.example.androidlab5_products.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.androidlab5_products.R
import com.example.androidlab5_products.additional.State
import com.example.androidlab5_products.models.ProductModel
import java.util.Date


class ExerciseFragment : Fragment() {

    private var isVisible = State(false)

    private lateinit var titleArea: EditText
    private lateinit var kcalArea: EditText
    private lateinit var weightArea: EditText
    private lateinit var editDate: TextView
    private lateinit var saveButton: Button
    private lateinit var closeButton: Button
    private lateinit var view: View

    private var productModel: ProductModel =
        ProductModel(0, 0, "", 100, 100, Date())

    private var onSaveExercise: ((ProductModel)->Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_exercise, container, false)
        titleArea = view.findViewById(R.id.product_name_area)
        kcalArea = view.findViewById(R.id.product_kcal_area)
        weightArea = view.findViewById(R.id.product_weight_area)
        editDate = view.findViewById(R.id.last_edit_date)
        saveButton = view.findViewById(R.id.save_product)
        closeButton = view.findViewById(R.id.close_product)

        saveButton.setOnClickListener{
            productModel.name = titleArea.text.toString()
            productModel.kcal = kcalArea.text.toString().toLong()
            productModel.weight = weightArea.text.toString().toLong()
            productModel.lastEdited = Date()

            onSaveExercise?.invoke(productModel)
            closePopUp()
        }

        closeButton.setOnClickListener {
            closePopUp()
        }

        isVisible.onChange { newValue ->
            view.visibility = if (newValue) VISIBLE else INVISIBLE
            titleArea.setText(productModel.name)
            kcalArea.setText(productModel.kcal.toString())
            weightArea.setText(productModel.weight.toString())
            editDate.text = productModel.lastEdited.toString()
        }

        showPopUp(productModel)

        return view
    }

    fun showPopUp(exercise: ProductModel) {
        productModel = exercise
        isVisible.value = true
    }

    private fun closePopUp() {
        isVisible.value = false
    }

    fun setOnSaveExercise(action: (ProductModel)->Unit){
        onSaveExercise = action
    }
}