package com.example.loginpreferencedatasotre.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.loginpreferencedatasotre.DataStore.Repository.DataStoreRepository
import com.example.loginpreferencedatasotre.R
import com.example.loginpreferencedatasotre.databinding.ActivityWelcomeBinding
import com.example.loginpreferencedatasotre.ui.ViewModel.LoginViewModelFactory
import com.example.loginpreferencedatasotre.ui.ViewModel.MainActivity
import com.example.loginpreferencedatasotre.ui.ViewModel.MainViewModel
import com.example.loginpreferencedatasotre.ui.ViewModel.dataStore

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this,
            LoginViewModelFactory(DataStoreRepository(dataStore))
        )
            .get(MainViewModel::class.java)

        viewModel.userPreferences.observe(this, { userPreferences ->
            val username = userPreferences.Username
            if (username.isNotEmpty()) {
                binding.textView.text = String.format(getString(R.string.Hai), username)
            }
        })

        binding.buttonlogout.setOnClickListener {
            viewModel.clearUserPreferences()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}