package com.example.androidlab5_products

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.androidlab5_products.fragments.WorkoutsListFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.androidlab5_products.R
import com.example.androidlab5_products.fragments.WorkoutFragmentView
import com.example.androidlab5_products.ui.HealthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: HealthViewModel by viewModel()

    private lateinit var listFragment : WorkoutsListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        listFragment = WorkoutsListFragment(viewModel)

        listFragment.setOnSelectListener { id ->
            val workoutViewFragment = WorkoutFragmentView(viewModel, this)
            supportFragmentManager.beginTransaction()
                .replace(R.id.workout_items_list, workoutViewFragment)
                .addToBackStack(null)
                .commit()

            viewModel.selectDinner(id)
            workoutViewFragment.setOnCloseListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.workout_items_list, listFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }


        supportFragmentManager.beginTransaction()
            .replace(R.id.workout_items_list, listFragment)
            .commit()

        requestPermissionCamera()
    }


    private fun requestPermissionCamera() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                102
            )
        }
    }
}
