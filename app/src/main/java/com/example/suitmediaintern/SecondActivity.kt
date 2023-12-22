package com.example.suitmediaintern

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_NAME_FROM_THIRD = "extra_name_from_third"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val textViewShowName = findViewById<TextView>(R.id.textViewShowName)
        val textViewSelectedUserName = findViewById<TextView>(R.id.textViewSelectedUserName)
        val btnChooseUser = findViewById<Button>(R.id.btnChooseUser)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val nameFromFirstScreen = intent.getStringExtra(EXTRA_NAME)
        val nameFromThirdScreen = intent.getStringExtra(EXTRA_NAME_FROM_THIRD)

        textViewShowName.text = "$nameFromFirstScreen"

        if (nameFromThirdScreen != null) {
            textViewSelectedUserName.text = nameFromThirdScreen
        } else {
            textViewSelectedUserName.setText(R.string.selected_user_name)
        }

        btnChooseUser.setOnClickListener {

            finish()

            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra(EXTRA_NAME, nameFromFirstScreen)
            intent.putExtra(EXTRA_NAME_FROM_THIRD, nameFromThirdScreen)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

    }
}
