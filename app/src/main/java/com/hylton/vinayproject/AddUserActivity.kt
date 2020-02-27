package com.hylton.vinayproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class AddUserActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText

    private lateinit var streetEditText: TextInputEditText
    private lateinit var apartmentEditText: TextInputEditText
    private lateinit var stateEditText: TextInputEditText
    private lateinit var zipCodeEditText: TextInputEditText

    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        firstNameEditText = findViewById(R.id.first_name_edit_text)
        lastNameEditText = findViewById(R.id.last_name_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)

        streetEditText = findViewById(R.id.street_edit_text)
        apartmentEditText = findViewById(R.id.apt_number_edit_text)
        stateEditText = findViewById(R.id.state_edit_text)
        zipCodeEditText = findViewById(R.id.zipcode_edit_text)

        saveButton = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            val replyIntent = Intent()

            if (TextUtils.isEmpty(firstNameEditText.text) ||
                    TextUtils.isEmpty(lastNameEditText.text) ||
                    TextUtils.isEmpty(emailEditText.text) ||
                    TextUtils.isEmpty(streetEditText.text) ||
                    TextUtils.isEmpty(apartmentEditText.text) ||
                    TextUtils.isEmpty(stateEditText.text) ||
                    TextUtils.isEmpty(zipCodeEditText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            else
            {
                val firstName = firstNameEditText.text.toString()
                val lastName = lastNameEditText.text.toString()
                val email = emailEditText.text.toString()

                val street = streetEditText.text.toString()
                val apt = apartmentEditText.text.toString()
                val state = stateEditText.text.toString()
                val zipCode = zipCodeEditText.text.toString()

                val extras = arrayListOf<String>()
                extras.addAll(listOf(firstName, lastName, email, street, apt, state, zipCode))

                replyIntent.putExtra(EXTRA_REPLY, extras)
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.hylton.android.userlistsql.REPLY"
    }
}
