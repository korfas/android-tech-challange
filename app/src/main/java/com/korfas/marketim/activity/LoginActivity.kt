package com.korfas.marketim.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.korfas.marketim.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_login)

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("remember", false)) {
            startActivity(Intent(this, OrdersActivity::class.java))
        }
    }

    fun loginButtonClicked(view: View) {

        if (usernameEt.text.toString().trim().equals(getString(R.string.username_key)) &&
            passwordEt.text.toString().trim().equals(getString(R.string.password_key))
        ) {
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("remember", rememberMeSwitch.isChecked).apply();
            startActivity(Intent(this, OrdersActivity::class.java))
        } else {
            showErrorSnackbar()
        }
    }

    fun showErrorSnackbar() {
        val snackbar = Snackbar.make(
            rootLayoutActLogin,
            getString(R.string.act_login_error_message),
            Snackbar.LENGTH_SHORT
        )
        val snackbarTextView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snackbar.show()
    }
}
