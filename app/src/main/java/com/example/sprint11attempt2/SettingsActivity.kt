package com.example.sprint11attempt2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val share = findViewById<FrameLayout>(R.id.shareButton)
        val support = findViewById<FrameLayout>(R.id.supportButton)
        val readAgreement = findViewById<FrameLayout>(R.id.agreementButton)

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
}