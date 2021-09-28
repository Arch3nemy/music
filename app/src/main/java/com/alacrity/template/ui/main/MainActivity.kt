package com.alacrity.template.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alacrity.template.App
import com.gmail.maystruks08.template.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {


    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        App.appComponent.inject(this)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}