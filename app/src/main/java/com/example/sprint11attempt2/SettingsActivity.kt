package com.example.sprint11attempt2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.sprint11attempt2.App.Companion.DARK_THEME_SWITCHER_OFF
import com.example.sprint11attempt2.App.Companion.DARK_THEME_SWITCHER_ON
import com.example.sprint11attempt2.App.Companion.THEME_SWITCHER
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        App.PLAYLIST_MAKER_PREFERENCES
        DARK_THEME_SWITCHER_OFF
        DARK_THEME_SWITCHER_ON

        val sharedPrefs = getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE) // переменная с SP
        val share = findViewById<FrameLayout>(R.id.shareButton)
        val support = findViewById<FrameLayout>(R.id.supportButton)
        val readAgreement = findViewById<FrameLayout>(R.id.agreementButton)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = (applicationContext as App).darkTheme

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            if (sharedPrefs.getString(THEME_SWITCHER, "") == DARK_THEME_SWITCHER_ON) {
                sharedPrefs.edit().putString(THEME_SWITCHER, DARK_THEME_SWITCHER_OFF).apply()
            } else {
                sharedPrefs.edit().putString(THEME_SWITCHER, DARK_THEME_SWITCHER_ON).apply()
            }
        }

        share.setOnClickListener {
            val link = getString(R.string.share_link)
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, link)
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        support.setOnClickListener {
            val message = getString(R.string.support_message)
            val email = getString(R.string.support_email)
            val emailSubject = getString(R.string.support_subject)
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.data = Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            val supportIntent = Intent.createChooser(sendIntent, null)
            startActivity(supportIntent)
        }

        readAgreement.setOnClickListener {
            val link = getString(R.string.agreement_link)
            val readIntent = Intent(Intent.ACTION_VIEW)
            readIntent.data = Uri.parse(link)
            val readAgreementIntent = Intent.createChooser(readIntent, null)
            startActivity(readAgreementIntent)
        }
    }

 /*   companion object {
        const val THEME_SWITCHER = "THEME_SWITCHER"
        const val DARK_THEME_SWITCHER_ON = "on"
        const val DARK_THEME_SWITCHER_OFF = "off"
    }*/
}