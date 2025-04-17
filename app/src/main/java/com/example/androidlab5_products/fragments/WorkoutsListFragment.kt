package com.example.androidlab5_products.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlab5_products.R
import com.example.androidlab5_products.adapters.Identifier
import com.example.androidlab5_products.adapters.IdentifierAdapter
import com.example.androidlab5_products.models.DinnerModer
import com.example.androidlab5_products.ui.HealthViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WorkoutsListFragment(private val viewModel: HealthViewModel) : Fragment() {
    private var onSelectAction: ((Long) -> Unit)? = null

    private var itemList = listOf<DinnerModer>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_workouts_list, container, false)

        val addButton = view.findViewById<FloatingActionButton>(R.id.add_button)
        val adapter = IdentifierAdapter(requireActivity())
        val workoutContainer = view.findViewById<RecyclerView>(R.id.workout_container)

        viewModel.workouts.observe(requireActivity()) { workouts ->
            itemList = workouts
            adapter.updateList(workouts.map { a -> Identifier(a.id, a.date.toString()) })
        }

        adapter.setOnDeleteItemListener { id ->
            viewModel.deleteWorkout(id)
        }

        adapter.setOnClickItemListener { id ->
            onSelectAction?.invoke(id)
        }

        addButton.setOnClickListener{
            onSelectAction?.invoke(-1)
        }
        workoutContainer.layoutManager = LinearLayoutManager(context)
        workoutContainer.adapter = adapter

        return view
    }


    fun setOnSelectListener(action: (Long) -> Unit) {
        onSelectAction = action
    }
}