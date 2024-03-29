package com.github.hattamaulana.felm.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.hattamaulana.felm.App
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.receiver.ReminderReceiver
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*

class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var sharedPref: App.SharedPref
    private lateinit var reminderReceiver: ReminderReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** Instance ReminderReceiver */
        reminderReceiver = ReminderReceiver()

        sharedPref = App.SharedPref(context as Context)
        sharedPref.notificationEarlyMorning.apply { setDataNotificationEarlyMorning(this) }
        sharedPref.notificationNewRelease.apply { setDataNotificationNewRelease(this) }

        /** Set toolbar */
        toolbar.title = "Setting"

        /** Set OnClick Listener for switch */
        switch_early_morning.setOnClickListener(this)
        switch_new_release.setOnClickListener(this)
        btn_change_language.setOnClickListener(this)

        /** Show Language */
        txt_language.text = Locale.getDefault().displayLanguage.toString()
    }

    private fun setDataNotificationEarlyMorning(boolean: Boolean) {
        switch_early_morning.isChecked = boolean
        val textId = if (boolean) R.string.summary_on_early_morning else R.string.summary_off_early_morning
        desc_early_morning.text = resources.getString(textId)
    }

    private fun setDataNotificationNewRelease(boolean: Boolean) {
        switch_new_release.isChecked = boolean
        val textId = if (boolean) R.string.summary_on_new_release else R.string.summary_off_new_release
        desc_new_release.text = resources.getString(textId)
    }

    override fun onClick(v: View?) = when(v?.id) {
        R.id.switch_early_morning -> {
            val checked = !sharedPref.notificationEarlyMorning
            sharedPref.notificationEarlyMorning = checked
            Log.i("MovieDB", "onClick: $checked")
            setDataNotificationEarlyMorning(checked)
            if (checked) {
                reminderReceiver.setDailyMorning(context as Context)
            } else {
                reminderReceiver.setOff(context as Context, ReminderReceiver.TYPE_DAILY_REMAINDER)
            }
        }

        R.id.switch_new_release -> {
            val checked = !sharedPref.notificationNewRelease
            sharedPref.notificationNewRelease = checked
            Log.i("MovieDB", "onClick: $checked")
            setDataNotificationNewRelease(checked)
            if (checked) {
                reminderReceiver.setNewRelease(context as Context)
            } else {
                reminderReceiver.setOff(context as Context, ReminderReceiver.TYPE_DAILY_REMAINDER)
            }
        }

        R.id.btn_change_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))

        else -> {}
    }
}