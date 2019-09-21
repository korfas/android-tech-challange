package com.korfas.marketim.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.korfas.marketim.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /**
         * Hafızadaki "remember" alanını kontrol et.
         * Eğer daha önce "Beni Hatırla" seçeneği seçilerek giriş yapılmışsa, "remember" değeri "true" dönecektir.
         * Eğer dönen değer "true" ise doğrudan "Siparişlerim" sayfasına yönlendir.
         * Eğer dönen değer "false" ise yönlendirme yapma, "Giriş" sayfasında kal.
         */
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("remember", false)) {
            startActivity(Intent(this, OrdersActivity::class.java))
        }
    }

    fun loginButtonClicked(view: View) {

        /**
         * Giriş butonu tıkandığında, girilen verileri kontrol et
         */

        if (usernameEt.text.toString().trim().equals(getString(R.string.username_key)) &&
            passwordEt.text.toString().trim().equals(getString(R.string.password_key))
        ) {

            /**
             * Eğer girilen kullanıcı adı ve şifre doğruysa, "Beni Hatırla" butonunu kontrol et.
             * Eğer hatırlama işlevi seçiliyse, hafızadaki "remember" alanına "true" değeri atanır.
             * Eğer hatırlama işlevi seçili değilse hafızadaki "remember" alanına "false" değeri atanır.
             */
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("remember", rememberMeSwitch.isChecked).apply();
            startActivity(Intent(this, OrdersActivity::class.java))
        } else {

            /**
             * Eğer girilen kullanıcı adı ve şifre yanlışsa hata mesajı içeren Snackbar göster.
             */

            showErrorSnackbar()
        }
    }

    fun showErrorSnackbar() {
        val snackbar = Snackbar.make(
            rootLayoutActLogin,
            getString(R.string.act_login_error_message),
            Snackbar.LENGTH_SHORT
        )

        /**
         * Snackbar'ın yazı rengi varayılan olarak siyah olduğu için koyu gri arka planda iyi gözükmüyor.
         * Snackbar içindeki TextView'i bir değişkene ata ve yazı rengini beyaz yap.
         */
        val snackbarTextView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snackbar.show()
    }
}
