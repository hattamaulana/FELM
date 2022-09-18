package com.github.hattamaulana.felm.ui.setting

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import com.github.hattamaulana.android.core.common.BaseFragment
import com.github.hattamaulana.felm.App
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.databinding.FragmentSettingBinding
import com.github.hattamaulana.felm.receiver.ReminderReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingBinding>(
    FragmentSettingBinding::inflate
), View.OnClickListener {

    private lateinit var sharedPref: App.SharedPref
    private lateinit var reminderReceiver: ReminderReceiver

    override fun initView(binding: FragmentSettingBinding) = with(binding) {
        /** Instance ReminderReceiver */
        reminderReceiver = ReminderReceiver()

        sharedPref = App.SharedPref(context as Context)
        sharedPref.notificationEarlyMorning.apply { setDataNotificationEarlyMorning(this) }
        sharedPref.notificationNewRelease.apply { setDataNotificationNewRelease(this) }

        /** Set toolbar */
        toolbar.title = "Setting"

        /** Set OnClick Listener for switch */
        switchEarlyMorning.setOnClickListener(this@SettingsFragment)
        switchNewRelease.setOnClickListener(this@SettingsFragment)
        btnChangeLanguage.setOnClickListener(this@SettingsFragment)

        /** Show Language */
        txtLanguage.text = Locale.getDefault().displayLanguage.toString()
    }

    override fun initData() {

    }

    private fun setDataNotificationEarlyMorning(boolean: Boolean) = binding?.apply {
        val textId = if (boolean) R.string.summary_on_early_morning else R.string.summary_off_early_morning

        descEarlyMorning.text = resources.getString(textId)
        switchEarlyMorning.isChecked = boolean
    }

    private fun setDataNotificationNewRelease(boolean: Boolean) = binding?.apply {
        val textId = if (boolean) R.string.summary_on_new_release else R.string.summary_off_new_release

        descNewRelease.text = resources.getString(textId)
        switchNewRelease.isChecked = boolean
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