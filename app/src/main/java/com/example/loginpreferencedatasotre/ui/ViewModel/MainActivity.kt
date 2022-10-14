package com.example.loginpreferencedatasotre.ui.ViewModel

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.loginpreferencedatasotre.DataStore.Repository.DataStoreRepository
import com.example.loginpreferencedatasotre.R
import com.example.loginpreferencedatasotre.databinding.ActivityMainBinding
import com.example.loginpreferencedatasotre.databinding.SignUpWindowBinding
import com.example.loginpreferencedatasotre.ui.WelcomeActivity
import kotlinx.android.synthetic.main.sign_up_window.view.*


private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var rememberMe = false
    private lateinit var usr: String
    private lateinit var pw: String
    lateinit var btn_sign_up: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        btn_sign_up = findViewById(R.id.Registerw)

        viewModel = ViewModelProvider(this,
            LoginViewModelFactory(DataStoreRepository(dataStore)))
            .get(MainViewModel::class.java)

        viewModel.userPreferences.observe(this, { userPreferences ->
            pw = userPreferences.Password
            usr = userPreferences.Username
            rememberMe = userPreferences.Remember
            if (rememberMe ) {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
        })

        binding.Login.setOnClickListener {

            val user = binding.username.text.toString()
            val pass = binding.Password.text.toString()


            viewModel.getUser().observe(this){

            if(user.equals("") && pass.equals("") || it.Username != user && it.Password != pass) {
                viewModel.saveUserLogged(false)
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.saveUserLogged(true)
                startActivity(Intent(this, WelcomeActivity::class.java))
                Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
            }

        binding.remember.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (!compoundButton.isChecked) {
                viewModel.clearUserPreferences()
            }
        }
    }
}
        btn_sign_up.setOnClickListener{
            fun_SignUp_PopupWindow()

        }

    }




    private fun fun_SignUp_PopupWindow() {
        val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popView: View = layoutInflater.inflate(R.layout.sign_up_window, null)
        val popupWindow: PopupWindow
        popupWindow = PopupWindow(popView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT,true)
        popupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0)

        val btn_dia_submit = popView.findViewById(R.id.btn_dia_submit) as Button
        btn_dia_submit.setOnClickListener {

            val str_dia_email_id = popView.edt_dia_email_id.text.toString()
            val str_dia_password = popView.edt_dia_password.text.toString()

            if(str_dia_email_id.equals("") || str_dia_password.equals("")){
                Toast.makeText(this,"Please Enter Details.", Toast.LENGTH_SHORT).show()
            }else {
                popupWindow.dismiss()
                val name = binding.username.text.toString()
                val pass = binding.Password.text.toString()
                viewModel.saveUser(name,pass)
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this,"Data Saved Successfully.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


