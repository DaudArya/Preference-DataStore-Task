package com.example.loginpreferencedatasotre.ui.ViewModel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.loginpreferencedatasotre.R
import com.example.loginpreferencedatasotre.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        val btn_dia_submit = findViewById(R.id.btn_dia_submit) as Button
        btn_dia_submit.setOnClickListener {

            val str_dia_user_id = edt_dia_user_id.text.toString()
            val str_dia_password = edt_dia_password.text.toString()

            if(str_dia_user_id.equals("") || str_dia_password.equals("")){
                Toast.makeText(this,"Please Enter Details.", Toast.LENGTH_SHORT).show()
            }else {
                val name = str_dia_user_id.toString()
                val pass = str_dia_password.toString()
                viewModel.saveUser(name,pass)
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this,"Data Saved Successfully.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}