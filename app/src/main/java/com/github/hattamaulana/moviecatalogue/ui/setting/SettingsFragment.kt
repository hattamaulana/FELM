package com.github.hattamaulana.moviecatalogue.ui.setting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.hattamaulana.moviecatalogue.App
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.receiver.ReminderReceiver
import kotlinx.android.synthetic.main.fragment_setting.*

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

        else -> {}
    }
}