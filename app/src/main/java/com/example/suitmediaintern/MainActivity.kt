package com.example.suitmediaintern

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextSentence = findViewById<EditText>(R.id.editTextSentence)
        val btnCheck = findViewById<Button>(R.id.btnCheck)
        val btnNext = findViewById<Button>(R.id.btnNext)

        btnCheck.setOnClickListener {
            val sentence = editTextSentence.text.toString()

            if (sentence.isEmpty()) {
                showMessageDialog("Please enter a sentence.")
                return@setOnClickListener
            }

            val isPalindrome = isPalindrome(sentence)

            val message = if (isPalindrome) {
                "isPalindrome."
            } else {
                "not Palindrome."
            }

            showMessageDialog(message)
        }

        btnNext.setOnClickListener {
            val name = editTextName.text.toString()

            if (name.isEmpty()) {
                showMessageDialog("Please enter a name.")
                return@setOnClickListener
            }

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra(SecondActivity.EXTRA_NAME, name)
            startActivity(intent)
        }
    }

    private fun isPalindrome(str: String): Boolean {
        val cleanStr = str.replace("\\s".toRegex(), "")
        return cleanStr.equals(cleanStr.reversed(), ignoreCase = true)
    }

    private fun showMessageDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}
