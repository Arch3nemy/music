package com.alacrity.alacritybet.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alacrity.alacritybet.App
import com.gmail.maystruks08.template.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        App.appComponent.inject(this)

        viewModel.testFlow.onEach {
            when(it) {
                TestData.Loading -> {
                    Log.d(TAG, "Loading")
                }
                else -> {

                }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        const val TAG = "MainActivityTAG"
    }

}